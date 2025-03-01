/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.position.impl;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.opengamma.core.change.AggregatingChangeManager;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.id.ObjectIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdSchemeDelegator;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.position.ManageableTrade;
import com.opengamma.master.position.PositionDocument;
import com.opengamma.master.position.PositionHistoryRequest;
import com.opengamma.master.position.PositionHistoryResult;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.position.PositionSearchRequest;
import com.opengamma.master.position.PositionSearchResult;
import com.opengamma.util.ArgumentChecker;

/**
 * A master of positions that uses the scheme of the unique identifier to determine which
 * underlying master should handle the request.
 * <p>
 * If no scheme-specific handler has been registered, a default is used.
 * <p>
 * Change events are aggregated from the different masters and presented through a single change manager.
 */
public class DelegatingPositionMaster extends UniqueIdSchemeDelegator<PositionMaster> implements PositionMaster {

  /**
   * The change manager.
   */
  private final ChangeManager _changeManager;

  /**
   * Creates an instance specifying the default delegate.
   *
   * @param defaultMaster the master to use when no scheme matches, not null
   */
  public DelegatingPositionMaster(final PositionMaster defaultMaster) {
    super(defaultMaster);
    _changeManager = defaultMaster.changeManager();
  }

  /**
   * Creates an instance specifying the default delegate.
   *
   * @param defaultMaster the master to use when no scheme matches, not null
   * @param schemePrefixToMasterMap  the map of masters by scheme to switch on, not null
   */
  public DelegatingPositionMaster(final PositionMaster defaultMaster, final Map<String, PositionMaster> schemePrefixToMasterMap) {
    super(defaultMaster, schemePrefixToMasterMap);
    final AggregatingChangeManager changeManager = new AggregatingChangeManager();

    // REVIEW jonathan 2012-08-03 -- this assumes that the delegating master lasts for the lifetime of the engine as we
    // never detach from the underlying change managers.
    changeManager.addChangeManager(defaultMaster.changeManager());
    for (final PositionMaster master : schemePrefixToMasterMap.values()) {
      changeManager.addChangeManager(master.changeManager());
    }
    _changeManager = changeManager;
  }

  //-------------------------------------------------------------------------
  @Override
  public PositionHistoryResult history(final PositionHistoryRequest request) {
    ArgumentChecker.notNull(request, "request");
    return chooseDelegate(request.getObjectId().getScheme()).history(request);
  }

  @Override
  public PositionSearchResult search(final PositionSearchRequest request) {
    ArgumentChecker.notNull(request, "request");
    final String uniqueIdScheme = request.getUniqueIdScheme();

    if (uniqueIdScheme == null) {
      return getDefaultDelegate().search(request);
    }
    return chooseDelegate(uniqueIdScheme).search(request);
  }

  @Override
  public PositionDocument get(final UniqueId uniqueId) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    return chooseDelegate(uniqueId.getScheme()).get(uniqueId);
  }

  @Override
  public PositionDocument get(final ObjectIdentifiable objectId, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(versionCorrection, "versionCorrection");
    return chooseDelegate(objectId.getObjectId().getScheme()).get(objectId, versionCorrection);
  }

  @Override
  public PositionDocument add(final PositionDocument document) {
    ArgumentChecker.notNull(document, "document");

    final UniqueId uniqueId = document.getUniqueId();
    if (uniqueId == null) {
      return getDefaultDelegate().add(document);
    }
    return chooseDelegate(uniqueId.getScheme()).add(document);
  }

  @Override
  public PositionDocument update(final PositionDocument document) {
    ArgumentChecker.notNull(document, "document");
    return chooseDelegate(document.getObjectId().getScheme()).update(document);
  }

  @Override
  public void remove(final ObjectIdentifiable objectIdentifiable) {
    ArgumentChecker.notNull(objectIdentifiable, "objectIdentifiable");
    chooseDelegate(objectIdentifiable.getObjectId().getScheme()).remove(objectIdentifiable);
  }

  @Override
  public PositionDocument correct(final PositionDocument document) {
    ArgumentChecker.notNull(document, "document");
    return chooseDelegate(document.getObjectId().getScheme()).correct(document);
  }

  @Override
  public ManageableTrade getTrade(final UniqueId uniqueId) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    return chooseDelegate(uniqueId.getScheme()).getTrade(uniqueId);
  }

  @Override
  public ChangeManager changeManager() {
    return _changeManager;
  }

  @Override
  public UniqueId addVersion(final ObjectIdentifiable objectId, final PositionDocument documentToAdd) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(documentToAdd, "documentToAdd");
    return chooseDelegate(objectId.getObjectId().getScheme()).addVersion(objectId, documentToAdd);
  }

  @Override
  public List<UniqueId> replaceVersion(final UniqueId uniqueId, final List<PositionDocument> replacementDocuments) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    ArgumentChecker.notNull(replacementDocuments, "replacementDocuments");
    return chooseDelegate(uniqueId.getScheme()).replaceVersion(uniqueId, replacementDocuments);
  }

  @Override
  public List<UniqueId> replaceAllVersions(final ObjectIdentifiable objectId, final List<PositionDocument> replacementDocuments) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(replacementDocuments, "replacementDocuments");
    return chooseDelegate(objectId.getObjectId().getScheme()).replaceAllVersions(objectId, replacementDocuments);
  }

  @Override
  public List<UniqueId> replaceVersions(final ObjectIdentifiable objectId, final List<PositionDocument> replacementDocuments) {
    ArgumentChecker.notNull(objectId, "objectId");
    ArgumentChecker.notNull(replacementDocuments, "replacementDocuments");
    return chooseDelegate(objectId.getObjectId().getScheme()).replaceVersions(objectId, replacementDocuments);
  }

  @Override
  public UniqueId replaceVersion(final PositionDocument replacementDocument) {
    ArgumentChecker.notNull(replacementDocument, "replacementDocument");
    ArgumentChecker.notNull(replacementDocument.getObjectId(), "replacementDocument.getObjectId");
    return chooseDelegate(replacementDocument.getObjectId().getScheme()).replaceVersion(replacementDocument);
  }

  @Override
  public void removeVersion(final UniqueId uniqueId) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");
    chooseDelegate(uniqueId.getScheme()).removeVersion(uniqueId);
  }

  @Override
  public Map<UniqueId, PositionDocument> get(final Collection<UniqueId> uniqueIds) {
    final Map<UniqueId, PositionDocument> resultMap = newHashMap();
    for (final UniqueId uniqueId : uniqueIds) {
      final PositionDocument doc = get(uniqueId);
      resultMap.put(uniqueId, doc);
    }
    return resultMap;
  }
}
