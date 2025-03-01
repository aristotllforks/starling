/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.config.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.mapping.FudgeObjectReader;
import org.fudgemsg.mapping.FudgeObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.change.DummyChangeManager;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.id.MutableUniqueIdentifiable;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.GUIDGenerator;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * REDIS DATA STRUCTURES:
 * Key["ALL_CLASSES"] -> Set[ClassName]
 * Key[ClassName] -> Set[ConfigName]
 * Key[ClassName - ConfigName] -> Hash
 *     Hash["UniqueId"] -> Text-encoded unique ID for the object
 * Key[UniqueId] -> Hash
 *     Hash["DATA"] -> Fudge encoded configuration data
 *     Hash["CLASS"] -> Class name for the item
 *     Hash["NAME"] -> Item name for the item
 *
 * While this data structure is more than necessary (in that you could cut out the hash for
 * the configuration item), it allows future expansion if more data is required to be stored
 * later without reformatting the Redis instance.
 *
 */

//TODO: Fix class type handling, currently can only hold one item per name - even if different classes
// This was a solution to allowing objects to be looked up via any superclass (including Object)

/**
 * A lightweight {@link ConfigSource} that cannot handle any versioning, and
 * which stores all configuration documents as a Fudge-encoded BLOB in Redis as a
 * backing store.
 */
public class NonVersionedRedisConfigSource implements ConfigSource {
  private static final Logger LOGGER = LoggerFactory.getLogger(NonVersionedRedisConfigSource.class);
  private final JedisPool _jedisPool;
  private final FudgeContext _fudgeContext;
  private final String _redisPrefix;
  private final String _allClassesKey;

  private static final byte[] DATA_NAME_AS_BYTES = "DATA".getBytes(Charsets.UTF_8);
  private static final byte[] CLASS_NAME_AS_BYTES = "CLASS".getBytes(Charsets.UTF_8);
  private static final byte[] ITEM_NAME_AS_BYTES = "ITEM".getBytes(Charsets.UTF_8);

  /**
   * The default scheme for unique identifiers.
   */
  public static final String IDENTIFIER_SCHEME_DEFAULT = "RedisCfg";


  /**
   * Constructs a source without a prefix and that uses the default Fudge context.
   *
   * @param jedisPool  the pool, not null
   */
  public NonVersionedRedisConfigSource(final JedisPool jedisPool) {
    this(jedisPool, "");
  }

  /**
   * Constructs a source that uses the default Fudge context.
   *
   * @param jedisPool  the pool, not null
   * @param redisPrefix  the prefix, not null
   */
  public NonVersionedRedisConfigSource(final JedisPool jedisPool, final String redisPrefix) {
    this(jedisPool, redisPrefix, OpenGammaFudgeContext.getInstance());
  }

  /**
   * Constructs a source.
   *
   * @param jedisPool  the pool, not null
   * @param redisPrefix  the prefix, not null
   * @param fudgeContext  the Fudge context, not null
   */
  public NonVersionedRedisConfigSource(final JedisPool jedisPool, final String redisPrefix, final FudgeContext fudgeContext) {
    ArgumentChecker.notNull(jedisPool, "jedisPool");
    ArgumentChecker.notNull(redisPrefix, "redisPrefix");
    ArgumentChecker.notNull(fudgeContext, "fudgeContext");

    _jedisPool = jedisPool;
    _redisPrefix = redisPrefix;
    _fudgeContext = fudgeContext;

    String allClassesKey = null;
    if (redisPrefix.isEmpty()) {
      allClassesKey = "ALL_CLASSES";
    } else {
      allClassesKey = redisPrefix + "-" + "ALL_CLASSES";
    }
    _allClassesKey = allClassesKey;
  }

  /**
   * Gets the jedisPool.
   * @return the jedisPool
   */
  protected JedisPool getJedisPool() {
    return _jedisPool;
  }

  /**
   * Gets the fudgeContext.
   * @return the fudgeContext
   */
  protected FudgeContext getFudgeContext() {
    return _fudgeContext;
  }

  /**
   * Gets the redisPrefix.
   * @return the redisPrefix
   */
  protected String getRedisPrefix() {
    return _redisPrefix;
  }

  // ---------------------------------------------------------------------
  // REDIS UTILITIES AND KEY MANAGEMENT
  // ---------------------------------------------------------------------

  /**
   * Gets a key from a class name. The key is in the form <code>[PREFIX]-[CLASS NAME]</code>.
   *
   * @param clazz  the class
   * @return  the key
   * @param <R>  the type of the config
   */
  protected <R> String getClassKeyName(final Class<R> clazz) {
    final StringBuilder sb = new StringBuilder();
    if (!getRedisPrefix().isEmpty()) {
      sb.append(getRedisPrefix());
      sb.append("-");
    }
    sb.append(clazz.getName());
    return sb.toString();
  }

  /**
   * Gets a key from the class name and config name. The key is in the form <code>[PREFIX]-[CLASS NAME]-[CONFIG NAME]</code>.
   *
   * @param clazz  the class
   * @param configName  the config name
   * @return  the key
   * @param <R>  the type of the config
   */
  protected <R> String getClassNameRedisKey(final Class<R> clazz, final String configName) {
    final StringBuilder sb = new StringBuilder();
    sb.append(getClassKeyName(clazz));
    sb.append('-');
    sb.append(configName);
    final String hashKeyName = sb.toString();
    return hashKeyName;
  }

  private byte[] getUniqueIdKey(final UniqueId uniqueId) {
    final StringBuilder sb = new StringBuilder();
    if (!getRedisPrefix().isEmpty()) {
      sb.append(getRedisPrefix());
      sb.append("-");
    }
    sb.append(uniqueId);
    final String text = sb.toString();
    final byte[] bytes = text.getBytes(Charsets.UTF_8);
    return bytes;
  }

  /**
   * Converts an array of bytes to an object using the Fudge deserializer.
   *
   * @param clazz  the expected class
   * @param dataAsBytes  the data
   * @return  the object
   * @param <R>  the type of the config
   */
  protected <R> R convertBytesToConfigurationObject(final Class<R> clazz, final byte[] dataAsBytes) {
    final FudgeObjectReader objectReader = getFudgeContext().createObjectReader(new ByteArrayInputStream(dataAsBytes));
    final R object = objectReader.read(clazz);
    return object;
  }

  // ---------------------------------------------------------------------
  // DATA SETTING/UPDATING OPERATIONS
  // UNIQUE TO THIS CLASS
  // ---------------------------------------------------------------------

  /**
   * Puts a config into the database.
   *
   * @param clazz  the config class, not null
   * @param configName  the config name, not null
   * @param object  the config, not null
   * @return  the identifier of the stored object
   * @param <R>  the type of the config
   */
  public <R> UniqueId put(final Class<R> clazz, final String configName, final R object) {
    ArgumentChecker.notNull(clazz, "clazz");
    ArgumentChecker.notNull(configName, "configName");
    ArgumentChecker.notNull(object, "object");
    ArgumentChecker.isTrue(clazz.isAssignableFrom(object.getClass()), "Unable to assign " + object.getClass() + " to " + clazz);

    final UniqueId uniqueId = UniqueId.of(IDENTIFIER_SCHEME_DEFAULT, GUIDGenerator.generate().toString());

    if (object instanceof MutableUniqueIdentifiable) {
      final MutableUniqueIdentifiable identifiable = (MutableUniqueIdentifiable) object;
      identifiable.setUniqueId(uniqueId);
    }

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final FudgeObjectWriter objectWriter = getFudgeContext().createObjectWriter(baos);
    objectWriter.write(object);
    final byte[] objectAsBytes = baos.toByteArray();

    final String classKeyName = getClassKeyName(clazz);
    final String classNameRedisKey = getClassNameRedisKey(clazz, configName);
    final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);

    final Jedis jedis = getJedisPool().getResource();
    try {
      jedis.sadd(_allClassesKey, clazz.getName());
      jedis.sadd(classKeyName, configName);
      // This allows lookup for type Object to work. Note this implies the same config name cannot be used twice
      jedis.sadd(Object.class.getName(), configName);
      jedis.hset(classNameRedisKey, "UniqueId", uniqueId.toString());
      jedis.hset(getClassNameRedisKey(Object.class, configName), "UniqueId", uniqueId.toString());
      jedis.hset(uniqueIdKey, DATA_NAME_AS_BYTES, objectAsBytes);
      jedis.hset(uniqueIdKey, CLASS_NAME_AS_BYTES, clazz.getName().getBytes(Charsets.UTF_8));
      jedis.hset(uniqueIdKey, ITEM_NAME_AS_BYTES, configName.getBytes(Charsets.UTF_8));

      getJedisPool().close();
    } catch (final Exception e) {
      LOGGER.warn("Unable to persist to Redis - " + clazz + " - " + configName, e);
      getJedisPool().close();
      throw new OpenGammaRuntimeException("Unable to persist to Redis - " + clazz + " - " + configName, e);
    }
    return uniqueId;
  }

  /**
   * Deletes a config from the database.
   *
   * @param clazz  the config class, not null
   * @param configName  the config name, not null
   * @param <R>  the type of the config
   */
  public <R> void delete(final Class<R> clazz, final String configName) {
    ArgumentChecker.notNull(clazz, "clazz");
    ArgumentChecker.notNull(configName, "configName");

    final String classKeyName = getClassKeyName(clazz);
    final String classNameRedisKey = getClassNameRedisKey(clazz, configName);

    final Jedis jedis = getJedisPool().getResource();
    try {

      jedis.srem(classKeyName, configName);
      final String uniqueIdText = jedis.hget(classNameRedisKey, "UniqueId");
      if (uniqueIdText != null) {
        final UniqueId uniqueId = UniqueId.parse(uniqueIdText);
        final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);
        jedis.del(uniqueIdKey);
      }
      jedis.del(classNameRedisKey);

      getJedisPool().close();
    } catch (final Exception e) {
      LOGGER.warn("Unable to delete from Redis - " + clazz + " - " + configName, e);
      getJedisPool().close();
      throw new OpenGammaRuntimeException("Unable to persist from Redis - " + clazz + " - " + configName, e);
    }
  }

  // ---------------------------------------------------------------------
  // CORE IMPLEMENTED METHODS ON CONFIGSOURCE
  // ---------------------------------------------------------------------

  @Override
  public <R> Collection<ConfigItem<R>> get(final Class<R> clazz, final String configName, final VersionCorrection versionCorrection) {
    final ConfigItem<R> latest = getLatestItemByName(clazz, configName);
    if (latest == null) {
      return Collections.emptyList();
    }
    return Collections.singleton(latest);
  }

  @Override
  public <R> Collection<ConfigItem<R>> getAll(final Class<R> clazz, final VersionCorrection versionCorrection) {
    ArgumentChecker.notNull(clazz, "clazz");

    final String classKeyName = getClassKeyName(clazz);

    final List<ConfigItem<R>> items = new LinkedList<>();

    final Jedis jedis = getJedisPool().getResource();
    try {

      final Set<String> itemNames = jedis.smembers(classKeyName);

      for (final String itemName : itemNames) {
        final String classNameRedisKey = getClassNameRedisKey(clazz, itemName);
        final String uniqueIdText = jedis.hget(classNameRedisKey, "UniqueId");
        if (uniqueIdText != null) {
          final UniqueId uniqueId = UniqueId.parse(uniqueIdText);
          final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);
          final byte[] dataAsBytes = jedis.hget(uniqueIdKey, DATA_NAME_AS_BYTES);
          final R config = convertBytesToConfigurationObject(clazz, dataAsBytes);
          final ConfigItem<R> configItem = ConfigItem.of(config);
          configItem.setName(itemName);
          configItem.setType(clazz);
          configItem.setUniqueId(uniqueId);
          items.add(configItem);
        }
      }

      getJedisPool().close();
    } catch (final Exception e) {
      LOGGER.warn("Unable to lookup from Redis - " + clazz, e);
      getJedisPool().close();
      throw new OpenGammaRuntimeException("Unable to lookup from Redis - " + clazz, e);
    }

    return items;
  }

  @Override
  public <R> R getSingle(final Class<R> clazz, final String configName, final VersionCorrection versionCorrection) {
    return getLatestByName(clazz, configName);
  }

  @Override
  public <R> R getLatestByName(final Class<R> clazz, final String configName) {
    final ConfigItem<R> latestItem = getLatestItemByName(clazz, configName);
    if (latestItem == null) {
      return null;
    }
    return latestItem.getValue();
  }

  /**
   * Gets a config from the database.
   *
   * @param clazz  the config class, not null
   * @param configName  the config name, not null
   * @return  the config
   * @param <R>  the type of the config
   */
  public <R> ConfigItem<R> getLatestItemByName(final Class<R> clazz, final String configName) {
    ArgumentChecker.notNull(clazz, "clazz");
    ArgumentChecker.notNull(configName, "configName");

    final String classKeyName = getClassKeyName(clazz);
    final String classNameRedisKey = getClassNameRedisKey(clazz, configName);

    byte[] dataAsBytes = null;
    UniqueId uniqueId = null;

    final Jedis jedis = getJedisPool().getResource();
    try {
      if (jedis.sismember(classKeyName, configName)) {
        final String uniqueIdText = jedis.hget(classNameRedisKey, "UniqueId");
        if (uniqueIdText != null) {
          uniqueId = UniqueId.parse(uniqueIdText);
          final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);
          dataAsBytes = jedis.hget(uniqueIdKey, DATA_NAME_AS_BYTES);
        }
      } else {
        // try fallback lookup to see if known by another compatible class
        if (jedis.sismember(Object.class.getName(), configName)) {
          final String uniqueIdText = jedis.hget(getClassNameRedisKey(Object.class, configName), "UniqueId");
          if (uniqueIdText != null) {
            uniqueId = UniqueId.parse(uniqueIdText);
            final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);
            dataAsBytes = jedis.hget(uniqueIdKey, DATA_NAME_AS_BYTES);
          }
        } else {
          LOGGER.debug("No config named {} for class {}", configName, clazz);
        }
      }

      getJedisPool().close();
    } catch (final Exception e) {
      LOGGER.warn("Unable to lookup latest by name from Redis - " + clazz + " - " + configName, e);
      getJedisPool().close();
      throw new OpenGammaRuntimeException("Unable to lookup latest by name from Redis - " + clazz + " - " + configName, e);
    }

    if (dataAsBytes == null) {
      LOGGER.debug("No data for config named {} for class {}", configName, clazz);
      return null;
    }

    final R config = convertBytesToConfigurationObject(clazz, dataAsBytes);

    final ConfigItem<R> configItem = new ConfigItem<>();
    configItem.setType(clazz);
    configItem.setValue(config);
    configItem.setUniqueId(uniqueId);
    configItem.setName(configName);

    return configItem;
  }

  // ---------------------------------------------------------------------
  // UNIQUE ID OPERATIONS
  // ---------------------------------------------------------------------


  @Override
  public Map<UniqueId, ConfigItem<?>> get(final Collection<UniqueId> uniqueIds) {
    final Map<UniqueId, ConfigItem<?>> result = new HashMap<>();
    for (final UniqueId uniqueId : uniqueIds) {
      final ConfigItem<?> item = get(uniqueId);
      result.put(uniqueId, item);
    }
    return result;
  }

  @Override
  public Map<ObjectId, ConfigItem<?>> get(final Collection<ObjectId> objectIds, final VersionCorrection versionCorrection) {
    final Map<ObjectId, ConfigItem<?>> result = new HashMap<>();
    for (final ObjectId objectId : objectIds) {
      final ConfigItem<?> item = get(objectId, null);
      result.put(objectId, item);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ConfigItem<?> get(final UniqueId uniqueId) {
    ArgumentChecker.notNull(uniqueId, "uniqueId");

    final byte[] uniqueIdKey = getUniqueIdKey(uniqueId);
    byte[] dataAsBytes = null;
    String className = null;

    final Jedis jedis = getJedisPool().getResource();
    try {
      dataAsBytes = jedis.hget(uniqueIdKey, DATA_NAME_AS_BYTES);
      className = new String(jedis.hget(uniqueIdKey, CLASS_NAME_AS_BYTES), Charsets.UTF_8);
      getJedisPool().close();
    } catch (final Exception e) {
      LOGGER.warn("Unable to lookup by unique id - " + uniqueId, e);
      getJedisPool().close();
      throw new OpenGammaRuntimeException("Unable to lookup by unique id - " + uniqueId, e);
    }

    if (dataAsBytes == null) {
      return null;
    }

    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (final ClassNotFoundException ex) {
      LOGGER.warn("Found config item of type {} which we can't load.", className);
      return null;
    }

    final Object config = convertBytesToConfigurationObject(clazz, dataAsBytes);

    @SuppressWarnings("rawtypes")
    final
    ConfigItem configItem = new ConfigItem();
    configItem.setType(clazz);
    configItem.setValue(config);
    configItem.setUniqueId(uniqueId);

    return configItem;
  }

  @Override
  public ConfigItem<?> get(final ObjectId objectId, final VersionCorrection versionCorrection) {
    return get(UniqueId.of(objectId, null));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R getConfig(final Class<R> clazz, final UniqueId uniqueId) {
    final ConfigItem<?> configItem = get(uniqueId);
    if (configItem == null) {
      return null;
    }
    return (R) configItem.getValue();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <R> R getConfig(final Class<R> clazz, final ObjectId objectId, final VersionCorrection versionCorrection) {
    final ConfigItem<?> configItem = get(UniqueId.of(objectId, null));
    if (configItem == null) {
      return null;
    }
    return (R) configItem.getValue();
  }

  @Override
  public ChangeManager changeManager() {
    return DummyChangeManager.INSTANCE;
  }

}
