/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.config.impl;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.BitSet;
import java.util.Collection;

import org.testng.annotations.Test;

import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ExternalScheme;
import com.opengamma.id.UniqueId;
import com.opengamma.util.test.AbstractRedisTestCase;
import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.INTEGRATION, enabled = true)
public class NonVersionedRedisConfigSourceTest extends AbstractRedisTestCase {

  public void putDeleteGetAll() {
    final NonVersionedRedisConfigSource configSource = new NonVersionedRedisConfigSource(getJedisPool(), getRedisPrefix());

    final ExternalIdBundle bundle1 = constructIdBundle("Test", "1");
    final ExternalIdBundle bundle2 = constructIdBundle("Test", "2");
    final ExternalIdBundle bundle3 = constructIdBundle("Test", "3");
    final ExternalIdBundle bundle4 = constructIdBundle("Test", "4");
    final ExternalIdBundle bundle5 = constructIdBundle("Test", "5");
    configSource.put(ExternalIdBundle.class, "bundle-1", bundle1);
    configSource.put(ExternalIdBundle.class, "bundle-2", bundle2);
    configSource.put(ExternalIdBundle.class, "bundle-3", bundle3);
    configSource.put(ExternalIdBundle.class, "bundle-4", bundle4);
    configSource.put(ExternalIdBundle.class, "bundle-5", bundle5);

    final BitSet bitSet = new BitSet();

    Collection<ConfigItem<ExternalIdBundle>> bundles = configSource.getAll(ExternalIdBundle.class, null);
    assertNotNull(bundles);
    assertEquals(5, bundles.size());
    for (final ConfigItem<ExternalIdBundle> item : bundles) {
      assertNotNull(item.getValue());
      assertTrue(item.getName().startsWith("bundle-"));
      final Integer bundleNum = Integer.parseInt(item.getName().substring(7));
      bitSet.set(bundleNum);
      assertEquals(1, item.getValue().getExternalIds().size());
      assertEquals(bundleNum.toString(), item.getValue().getValue(ExternalScheme.of("Test")));
    }
    assertTrue(bitSet.get(1));
    assertTrue(bitSet.get(2));
    assertTrue(bitSet.get(3));
    assertTrue(bitSet.get(4));
    assertTrue(bitSet.get(5));

    bitSet.clear();
    configSource.delete(ExternalIdBundle.class, "bundle-4");
    bundles = configSource.getAll(ExternalIdBundle.class, null);
    assertNotNull(bundles);
    assertEquals(4, bundles.size());
    for (final ConfigItem<ExternalIdBundle> item : bundles) {
      final Integer bundleNum = Integer.parseInt(item.getName().substring(7));
      bitSet.set(bundleNum);
    }
    assertTrue(bitSet.get(1));
    assertTrue(bitSet.get(2));
    assertTrue(bitSet.get(3));
    assertFalse(bitSet.get(4));
    assertTrue(bitSet.get(5));
  }

  public void putGet() {
    final NonVersionedRedisConfigSource configSource = new NonVersionedRedisConfigSource(getJedisPool(), getRedisPrefix());

    final ExternalIdBundle bundle1 = constructIdBundle("Test", "1");
    final ExternalIdBundle bundle2 = constructIdBundle("Test", "2");
    final ExternalIdBundle bundle3 = constructIdBundle("Test", "3");
    final ExternalIdBundle bundle4 = constructIdBundle("Test", "4");
    final ExternalIdBundle bundle5 = constructIdBundle("Test", "5");
    configSource.put(ExternalIdBundle.class, "bundle-1", bundle1);
    configSource.put(ExternalIdBundle.class, "bundle-2", bundle2);
    configSource.put(ExternalIdBundle.class, "bundle-3", bundle3);
    configSource.put(ExternalIdBundle.class, "bundle-4", bundle4);
    configSource.put(ExternalIdBundle.class, "bundle-5", bundle5);

    ExternalIdBundle result = null;

    result = configSource.getLatestByName(ExternalIdBundle.class, "bundle-2");
    assertNotNull(result);
    assertEquals("2", result.getValue(ExternalScheme.of("Test")));
  }

  public void uniqueIdTest() {
    final NonVersionedRedisConfigSource configSource = new NonVersionedRedisConfigSource(getJedisPool(), getRedisPrefix());
    final ExternalIdBundle bundle1 = constructIdBundle("Test", "1");
    final UniqueId uniqueId = configSource.put(ExternalIdBundle.class, "uniqueIdTest", bundle1);
    assertNotNull(uniqueId);
    assertEquals(NonVersionedRedisConfigSource.IDENTIFIER_SCHEME_DEFAULT, uniqueId.getScheme());

    final ConfigItem<?> configItem = configSource.get(uniqueId);
    assertNotNull(configItem);
    assertEquals(bundle1, configItem.getValue());
    assertEquals(ExternalIdBundle.class, configItem.getType());
    assertEquals(uniqueId, configItem.getUniqueId());
  }

  private static ExternalIdBundle constructIdBundle(final String scheme, final String value) {
    final ExternalId id = ExternalId.of(scheme, value);
    final ExternalIdBundle bundle = ExternalIdBundle.of(id);
    return bundle;
  }

}
