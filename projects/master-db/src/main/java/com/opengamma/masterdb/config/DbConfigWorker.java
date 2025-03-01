/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.config;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.fudgemsg.mapping.FudgeDeserializer;
import org.fudgemsg.mapping.FudgeObjectReader;
import org.fudgemsg.mapping.FudgeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.LobHandler;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.elsql.ElSqlBundle;
import com.opengamma.id.IdUtils;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.ObjectId;
import com.opengamma.id.ObjectIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.AbstractHistoryRequest;
import com.opengamma.master.AbstractHistoryResult;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.master.config.ConfigHistoryRequest;
import com.opengamma.master.config.ConfigHistoryResult;
import com.opengamma.master.config.ConfigMetaDataRequest;
import com.opengamma.master.config.ConfigMetaDataResult;
import com.opengamma.master.config.ConfigSearchRequest;
import com.opengamma.master.config.ConfigSearchResult;
import com.opengamma.master.config.ConfigSearchSortOrder;
import com.opengamma.masterdb.AbstractDocumentDbMaster;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.ClassUtils;
import com.opengamma.util.db.DbConnector;
import com.opengamma.util.db.DbDateUtils;
import com.opengamma.util.db.DbMapSqlParameterSource;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;
import com.opengamma.util.paging.Paging;
import com.opengamma.util.paging.PagingRequest;

/**
 *
 */
/* package */class DbConfigWorker extends AbstractDocumentDbMaster<ConfigDocument> {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigWorker.class);

  /**
   * The Fudge context.
   */
  protected static final FudgeContext FUDGE_CONTEXT = OpenGammaFudgeContext.getInstance();

  // -----------------------------------------------------------------
  // TIMERS FOR METRICS GATHERING
  // By default these do nothing. Registration will replace them
  // so that they actually do something.
  // -----------------------------------------------------------------
  private Timer _searchTimer = new Timer();
  private Timer _metaDataTimer = new Timer();
  private Timer _insertTimer = new Timer();

  /**
   * SQL order by.
   */
  protected static final EnumMap<ConfigSearchSortOrder, String> ORDER_BY_MAP = new EnumMap<>(ConfigSearchSortOrder.class);
  static {
    ORDER_BY_MAP.put(ConfigSearchSortOrder.OBJECT_ID_ASC, "oid ASC");
    ORDER_BY_MAP.put(ConfigSearchSortOrder.OBJECT_ID_DESC, "oid DESC");
    ORDER_BY_MAP.put(ConfigSearchSortOrder.VERSION_FROM_INSTANT_ASC, "ver_from_instant ASC");
    ORDER_BY_MAP.put(ConfigSearchSortOrder.VERSION_FROM_INSTANT_DESC, "ver_from_instant DESC");
    ORDER_BY_MAP.put(ConfigSearchSortOrder.NAME_ASC, "name ASC");
    ORDER_BY_MAP.put(ConfigSearchSortOrder.NAME_DESC, "name DESC");
  }

  /**
   * Creates an instance.
   *
   * @param dbConnector
   *          the database connector, not null
   * @param defaultScheme
   *          the default scheme, not null
   */
  DbConfigWorker(final DbConnector dbConnector, final String defaultScheme) {
    super(dbConnector, defaultScheme);
    setElSqlBundle(ElSqlBundle.of(dbConnector.getDialect().getElSqlConfig(), DbConfigMaster.class));
  }

  @Override
  public void registerMetrics(final MetricRegistry summaryRegistry, final MetricRegistry detailedRegistry, final String namePrefix) {
    super.registerMetrics(summaryRegistry, detailedRegistry, namePrefix);
    _insertTimer = summaryRegistry.timer(namePrefix + ".insert");
    _metaDataTimer = summaryRegistry.timer(namePrefix + ".metaData");
    _searchTimer = summaryRegistry.timer(namePrefix + ".search");
  }

  // -------------------------------------------------------------------------
  @Override
  public ConfigDocument get(final UniqueId uniqueId) {
    return doGet(uniqueId, new ConfigDocumentExtractor(), "Config");
  }

  @Override
  public ConfigDocument get(final ObjectIdentifiable objectId, final VersionCorrection versionCorrection) {
    return doGetByOidInstants(objectId, versionCorrection, new ConfigDocumentExtractor(), "Config");
  }

  @Override
  protected void mergeNonUpdatedFields(final ConfigDocument newDocument, final ConfigDocument oldDocument) {
    if (newDocument.getConfig() == null) {
      final ConfigDocument hackGenerics = newDocument;
      hackGenerics.setConfig(oldDocument.getConfig());
    }
  }

  @Override
  protected ConfigDocument insert(final ConfigDocument document) {
    ArgumentChecker.notNull(document.getName(), "document.name");
    ArgumentChecker.notNull(document.getConfig(), "document.value");
    ArgumentChecker.notNull(document.getType(), "document.type");

    final Timer.Context context = _insertTimer.time();
    try {
      final Object value = document.getConfig().getValue();
      final long docId = nextId("cfg_config_seq");
      final long docOid = document.getUniqueId() != null ? extractOid(document.getUniqueId()) : docId;
      // set the uniqueId
      final UniqueId uniqueId = createUniqueId(docOid, docId);
      document.setUniqueId(uniqueId);
      if (value instanceof MutableUniqueIdentifiable) {
        ((MutableUniqueIdentifiable) value).setUniqueId(uniqueId);
      }

      final byte[] bytes = serializeToFudge(value);

      // the arguments for inserting into the config table
      final DbMapSqlParameterSource docArgs = createParameterSource()
          .addValue("doc_id", docId)
          .addValue("doc_oid", docOid)
          .addTimestamp("ver_from_instant", document.getVersionFromInstant())
          .addTimestampNullFuture("ver_to_instant", document.getVersionToInstant())
          .addTimestamp("corr_from_instant", document.getCorrectionFromInstant())
          .addTimestampNullFuture("corr_to_instant", document.getCorrectionToInstant())
          .addValue("name", document.getName())
          .addValue("config_type", document.getType().getName())
          .addValue("config", new SqlLobValue(bytes, getDialect().getLobHandler()), Types.BLOB);
      final String sqlDoc = getElSqlBundle().getSql("Insert", docArgs);
      getJdbcTemplate().update(sqlDoc, docArgs);
      return document;
    } finally {
      context.stop();
    }
  }

  private byte[] serializeToFudge(final Object configObj) {
    // serialize the configuration value
    final FudgeSerializer serializer = new FudgeSerializer(FUDGE_CONTEXT);
    final MutableFudgeMsg objectToFudgeMsg = serializer.objectToFudgeMsg(configObj);
    return FUDGE_CONTEXT.toByteArray(objectToFudgeMsg);
  }

  // -------------------------------------------------------------------------
  public ConfigMetaDataResult metaData(final ConfigMetaDataRequest request) {
    ArgumentChecker.notNull(request, "request");

    final Timer.Context context = _metaDataTimer.time();
    try {
      final ConfigMetaDataResult result = new ConfigMetaDataResult();
      if (request.isConfigTypes()) {
        final String sql = getElSqlBundle().getSql("SelectTypes");
        final List<String> configTypes = getJdbcTemplate().getJdbcOperations().queryForList(sql, String.class);
        for (final String configType : configTypes) {
          try {
            result.getConfigTypes().add(ClassUtils.loadClass(configType));
          } catch (final ClassNotFoundException ex) {
            LOGGER.warn("Unable to load class", ex);
          }
        }
      }
      return result;
    } finally {
      context.stop();
    }
  }

  // -------------------------------------------------------------------------
  public <T> ConfigSearchResult<T> search(final ConfigSearchRequest<T> request) {
    ArgumentChecker.notNull(request, "request");
    ArgumentChecker.notNull(request.getType(), "request.type");
    ArgumentChecker.notNull(request.getPagingRequest(), "request.pagingRequest");
    ArgumentChecker.notNull(request.getVersionCorrection(), "request.versionCorrection");
    LOGGER.debug("search {}", request);

    final Timer.Context context = _searchTimer.time();
    try {
      VersionCorrection vc = request.getVersionCorrection();
      if (vc.containsLatest()) {
        vc = vc.withLatestFixed(now());
      }
      final ConfigSearchResult<T> result = new ConfigSearchResult<>(vc);

      final List<ObjectId> objectIds = request.getConfigIds();
      if (objectIds != null && objectIds.size() == 0) {
        result.setPaging(Paging.of(request.getPagingRequest(), 0));
        return result;
      }

      final DbMapSqlParameterSource args = createParameterSource()
          .addTimestamp("version_as_of_instant", vc.getVersionAsOf())
          .addTimestamp("corrected_to_instant", vc.getCorrectedTo())
          .addValueNullIgnored("name", getDialect().sqlWildcardAdjustValue(request.getName()));

      if (!request.getType().isInstance(Object.class)) {
        args.addValue("config_type", request.getType().getName());
      }
      if (objectIds != null) {
        final StringBuilder buf = new StringBuilder(objectIds.size() * 10);
        for (final ObjectId objectId : objectIds) {
          checkScheme(objectId);
          buf.append(extractOid(objectId)).append(", ");
        }
        buf.setLength(buf.length() - 2);
        args.addValue("sql_search_object_ids", buf.toString());
      }
      args.addValue("sort_order", ORDER_BY_MAP.get(request.getSortOrder()));
      args.addValue("paging_offset", request.getPagingRequest().getFirstItem());
      args.addValue("paging_fetch", request.getPagingRequest().getPagingSize());

      final String[] sql = { getElSqlBundle().getSql("Search", args), getElSqlBundle().getSql("SearchCount", args) };

      final NamedParameterJdbcOperations namedJdbc = getDbConnector().getJdbcTemplate();
      final ConfigDocumentExtractor configDocumentExtractor = new ConfigDocumentExtractor();
      if (request.getPagingRequest().equals(PagingRequest.ALL)) {
        final List<ConfigDocument> queryResult = namedJdbc.query(sql[0], args, configDocumentExtractor);
        for (final ConfigDocument configDocument : queryResult) {
          if (request.getType().isInstance(configDocument.getConfig().getValue())) {
            result.getDocuments().add(configDocument);
          }
        }
        result.setPaging(Paging.of(request.getPagingRequest(), result.getDocuments()));
      } else {
        final int count = namedJdbc.queryForObject(sql[1], args, Integer.class);
        result.setPaging(Paging.of(request.getPagingRequest(), count));
        if (count > 0 && !request.getPagingRequest().equals(PagingRequest.NONE)) {
          final List<ConfigDocument> queryResult = namedJdbc.query(sql[0], args, configDocumentExtractor);
          for (final ConfigDocument configDocument : queryResult) {
            if (request.getType().isInstance(configDocument.getConfig().getValue())) {
              result.getDocuments().add(configDocument);
            }
          }
        }
      }
      return result;
    } finally {
      context.stop();
    }
  }

  // -------------------------------------------------------------------------
  public <T> ConfigHistoryResult<T> history(final ConfigHistoryRequest<T> request) {
    ArgumentChecker.notNull(request, "request");
    // ArgumentChecker.notNull(request.getType(), "request.type");
    ArgumentChecker.notNull(request.getObjectId(), "request.objectId");
    checkScheme(request.getObjectId());
    LOGGER.debug("history {}", request);

    final ConfigHistoryResult<T> result = new ConfigHistoryResult<>();
    final ConfigDocumentExtractor extractor = new ConfigDocumentExtractor();
    final DbMapSqlParameterSource args = argsHistory(request);
    final String[] sql = { getElSqlBundle().getSql("History", args), getElSqlBundle().getSql("HistoryCount", args) };

    final NamedParameterJdbcOperations namedJdbc = getDbConnector().getJdbcTemplate();
    if (request.getPagingRequest().equals(PagingRequest.ALL)) {
      final List<ConfigDocument> queryResult = namedJdbc.query(sql[0], args, extractor);
      for (final ConfigDocument configDocument : queryResult) {
        if (request.getType() == null || request.getType().isInstance(configDocument.getConfig().getValue())) {
          result.getDocuments().add(configDocument);
        }
      }
      result.setPaging(Paging.of(request.getPagingRequest(), result.getDocuments()));
    } else {
      final int count = namedJdbc.queryForObject(sql[1], args, Integer.class);
      result.setPaging(Paging.of(request.getPagingRequest(), count));
      if (count > 0 && !request.getPagingRequest().equals(PagingRequest.NONE)) {
        final List<ConfigDocument> queryResult = namedJdbc.query(sql[0], args, extractor);
        for (final ConfigDocument configDocument : queryResult) {
          if (request.getType() == null || request.getType().isInstance(configDocument.getConfig().getValue())) {
            result.getDocuments().add(configDocument);
          }
        }
      }
    }
    return result;
  }

  // -------------------------------------------------------------------------
  /**
   * Mapper from SQL rows to a ConfigDocument.
   */
  private final class ConfigDocumentExtractor implements ResultSetExtractor<List<ConfigDocument>> {

    private long _lastDocId = -1;
    private final List<ConfigDocument> _documents = new ArrayList<>();

    @Override
    public List<ConfigDocument> extractData(final ResultSet rs) throws SQLException, DataAccessException {
      while (rs.next()) {
        final long docId = rs.getLong("DOC_ID");
        if (_lastDocId != docId) {
          _lastDocId = docId;
          buildConfig(rs, docId);
        }
      }
      return _documents;
    }

    private void buildConfig(final ResultSet rs, final long docId) throws SQLException {
      final long docOid = rs.getLong("DOC_OID");
      final Timestamp versionFrom = rs.getTimestamp("VER_FROM_INSTANT");
      final Timestamp versionTo = rs.getTimestamp("VER_TO_INSTANT");
      final Timestamp correctionFrom = rs.getTimestamp("CORR_FROM_INSTANT");
      final Timestamp correctionTo = rs.getTimestamp("CORR_TO_INSTANT");
      final String name = rs.getString("NAME");
      final String configType = rs.getString("CONFIG_TYPE");
      final LobHandler lob = getDialect().getLobHandler();
      final byte[] bytes = lob.getBlobAsBytes(rs, "CONFIG");
      Class<?> reifiedType = null;
      try {
        reifiedType = ClassUtils.loadClass(configType);
      } catch (final ClassNotFoundException ex) {
        LOGGER.warn("ConfigType: {} class can not be found for docOid: {}", configType, docOid);
        return;
      }

      final FudgeObjectReader objReader = FUDGE_CONTEXT.createObjectReader(new ByteArrayInputStream(bytes));
      final FudgeMsg fudgeMsg = objReader.getMessageReader().nextMessage();
      try {

        final FudgeDeserializer deserializer = new FudgeDeserializer(FUDGE_CONTEXT);
        final Object configObj = deserializer.fudgeMsgToObject(reifiedType, fudgeMsg);
        final ConfigItem<?> item = ConfigItem.of(configObj);
        item.setName(name);
        item.setType(reifiedType);
        final ConfigDocument doc = new ConfigDocument(item);
        final UniqueId uniqueId = createUniqueId(docOid, docId);
        doc.setUniqueId(uniqueId);
        IdUtils.setInto(configObj, uniqueId);
        doc.setVersionFromInstant(DbDateUtils.fromSqlTimestamp(versionFrom));
        doc.setVersionToInstant(DbDateUtils.fromSqlTimestampNullFarFuture(versionTo));
        doc.setCorrectionFromInstant(DbDateUtils.fromSqlTimestamp(correctionFrom));
        doc.setCorrectionToInstant(DbDateUtils.fromSqlTimestampNullFarFuture(correctionTo));
        _documents.add(doc);

      } catch (final Exception ex) {
        LOGGER.warn("Bad fudge message in database, unable to deserialise docOid:" + docOid + " " + fudgeMsg
            + " to " + configType, ex);
      }
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  protected AbstractHistoryResult<ConfigDocument> historyByVersionsCorrections(final AbstractHistoryRequest request) {
    final ConfigHistoryRequest historyRequest = new ConfigHistoryRequest();
    historyRequest.setCorrectionsFromInstant(request.getCorrectionsFromInstant());
    historyRequest.setCorrectionsToInstant(request.getCorrectionsToInstant());
    historyRequest.setVersionsFromInstant(request.getVersionsFromInstant());
    historyRequest.setVersionsToInstant(request.getVersionsToInstant());
    historyRequest.setObjectId(request.getObjectId());
    return history(historyRequest);
  }

}
