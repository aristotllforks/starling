/**
 * Copyright (C) 2019 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.core.convention.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Instant;

import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.test.TestGroup;

/**
 * Tests for {@link DataConventionSourceUris}.
 */
@Test(groups = TestGroup.UNIT)
public class DataConventionSourceUrisTest {
  private static final VersionCorrection VC = VersionCorrection.of(Instant.ofEpochSecond(10000), Instant.ofEpochSecond(20000));
  private static final UniqueId UID_1 = UniqueId.of("conv", "1");
  private static final UniqueId UID_2 = UniqueId.of("conv", "2");
  private static final ObjectId OID = UID_1.getObjectId();
  private static final ExternalIdBundle EIDS = ExternalIdBundle.of(ExternalId.of("eid", "1"), ExternalId.of("eid", "2"));
  private static final Class<?> TYPE = Object.class;
  private URI _baseUri;

  /**
   * Sets up the URI
   *
   * @throws URISyntaxException
   *           if the path is wrong
   */
  @BeforeMethod
  public void createUri() throws URISyntaxException {
    _baseUri = new URI("path/to/");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriGetNullUri1() {
    DataConventionSourceUris.uriGet(null, OID, VC);
  }

  /**
   * Tests that the object id cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriGetNullObjectId() {
    DataConventionSourceUris.uriGet(_baseUri, null, VC);
  }

  /**
   * Tests the URI that is built when the version correction is null (i.e.
   * LATEST is required).
   */
  public void testBuildUriOidLatest() {
    final URI uri = DataConventionSourceUris.uriGet(_baseUri, OID, null);
    assertEquals(uri.getPath(), "path/to/conventions/conv~1");
    assertNull(uri.getQuery());
  }

  /**
   * Tests the URI that is built.
   */
  public void testBuildUriOidVersion() {
    final URI uri = DataConventionSourceUris.uriGet(_baseUri, OID, VC);
    assertEquals(uri.getPath(), "path/to/conventions/conv~1");
    assertEquals(uri.getQuery(), "versionAsOf=1970-01-01T02:46:40Z&correctedTo=1970-01-01T05:33:20Z");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriGetNullUri2() {
    DataConventionSourceUris.uriGet(null, UID_1);
  }

  /**
   * Tests that the unique id cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriGetNullUid() {
    DataConventionSourceUris.uriGet(_baseUri, null);
  }

  /**
   * Tests the URI that is built.
   */
  public void testBuildUriUid() {
    final URI uri = DataConventionSourceUris.uriGet(_baseUri, UID_1);
    assertEquals(uri.getPath(), "path/to/conventions/conv~1");
    assertNull(uri.getQuery());
  }

  /**
   * Tests the URI that is built.
   */
  public void testBuildUriUidWithVersion() {
    final UniqueId uid = UID_1.withVersion(Instant.ofEpochMilli(10).toString());
    final URI uri = DataConventionSourceUris.uriGet(_baseUri, uid);
    assertEquals(uri.getPath(), "path/to/conventions/conv~1");
    assertEquals(uri.getQuery(), "version=1970-01-01T00:00:00.010Z");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchNullUri() {
    DataConventionSourceUris.uriSearch(null, VC, EIDS);
  }

  /**
   * Tests that the bundle cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchNullBundle() {
    DataConventionSourceUris.uriSearch(_baseUri, VC, null);
  }

  /**
   * Tests the URI that is built when the version correction is null (i.e.
   * LATEST is required).
   */
  public void testSearchLatest() {
    final URI uri = DataConventionSourceUris.uriSearch(_baseUri, null, EIDS);
    assertEquals(uri.getPath(), "path/to/conventions");
    assertEquals(uri.getQuery(), "id=eid~1&id=eid~2");
  }

  /**
   * Tests the URI that is built.
   */
  public void testSearch() {
    final URI uri = DataConventionSourceUris.uriSearch(_baseUri, VC, EIDS);
    assertEquals(uri.getPath(), "path/to/conventions");
    assertEquals(uri.getQuery(), "versionAsOf=1970-01-01T02:46:40Z&correctedTo=1970-01-01T05:33:20Z&id=eid~1&id=eid~2");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriBulkNullUri() {
    DataConventionSourceUris.uriBulk(null, Collections.singleton(UID_1));
  }

  /**
   * Tests the URI when the unique ids are null.
   */
  public void testUriBulkNullUids() {
    final URI uri = DataConventionSourceUris.uriBulk(_baseUri, null);
    assertEquals(uri.getPath(), "path/to/conventionSearches/bulk");
    assertNull(uri.getQuery());
  }

  /**
   * Tests the URI for a bulk get.
   */
  public void testUriBulk() {
    final URI uri = DataConventionSourceUris.uriBulk(_baseUri, Arrays.asList(UID_1, UID_2));
    assertEquals(uri.getPath(), "path/to/conventionSearches/bulk");
    assertEquals(uri.getQuery(), "id=conv~1&id=conv~2");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchListNullUri() {
    DataConventionSourceUris.uriSearchList(null, EIDS);
  }

  /**
   * Tests the URI when the external id bundle is null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchListNullEids() {
    DataConventionSourceUris.uriSearchList(_baseUri, null);
  }

  /**
   * Tests the URI for a search list.
   */
  public void testUriSearchList() {
    final URI uri = DataConventionSourceUris.uriSearchList(_baseUri, EIDS);
    assertEquals(uri.getPath(), "path/to/conventionSearches/list");
    assertEquals(uri.getQuery(), "id=eid~1&id=eid~2");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchSingleNullUri1() {
    DataConventionSourceUris.uriSearchSingle(null, EIDS, VC);
  }

  /**
   * Tests that the bundle cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchSingleNullBundle1() {
    DataConventionSourceUris.uriSearchSingle(_baseUri, null, null);
  }

  /**
   * Tests the URI that is built when the version correction is null (i.e.
   * LATEST is required).
   */
  public void testSearchSingleLatest() {
    final URI uri = DataConventionSourceUris.uriSearchSingle(_baseUri, EIDS, null);
    assertEquals(uri.getPath(), "path/to/conventionSearches/single");
    assertEquals(uri.getQuery(), "id=eid~1&id=eid~2");
  }

  /**
   * Tests the URI that is built.
   */
  public void testSearchSingle() {
    final URI uri = DataConventionSourceUris.uriSearchSingle(_baseUri, EIDS, VC);
    assertEquals(uri.getPath(), "path/to/conventionSearches/single");
    assertEquals(uri.getQuery(), "versionAsOf=1970-01-01T02:46:40Z&correctedTo=1970-01-01T05:33:20Z&id=eid~1&id=eid~2");
  }

  /**
   * Tests that the base URI cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchSingleNullUri2() {
    DataConventionSourceUris.uriSearchSingle(null, EIDS, VC);
  }

  /**
   * Tests that the bundle cannot be null.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUriSearchSingleNullBundle2() {
    DataConventionSourceUris.uriSearchSingle(_baseUri, null, null);
  }

  /**
   * Tests the URI that is built when the version correction is null (i.e.
   * LATEST is required).
   */
  public void testSearchSingleLatestNoType() {
    final URI uri = DataConventionSourceUris.uriSearchSingle(_baseUri, EIDS, null, null);
    assertEquals(uri.getPath(), "path/to/conventionSearches/single");
    assertEquals(uri.getQuery(), "id=eid~1&id=eid~2");
  }

  /**
   * Tests the URI that is built.
   */
  public void testSearchSingleNoType() {
    final URI uri = DataConventionSourceUris.uriSearchSingle(_baseUri, EIDS, VC, null);
    assertEquals(uri.getPath(), "path/to/conventionSearches/single");
    assertEquals(uri.getQuery(), "versionAsOf=1970-01-01T02:46:40Z&correctedTo=1970-01-01T05:33:20Z&id=eid~1&id=eid~2");
  }

  /**
   * Tests the URI that is built.
   */
  public void testSearchSingleVersionType() {
    final URI uri = DataConventionSourceUris.uriSearchSingle(_baseUri, EIDS, VC, TYPE);
    assertEquals(uri.getPath(), "path/to/conventionSearches/single");
    assertEquals(uri.getQuery(), "versionAsOf=1970-01-01T02:46:40Z&correctedTo=1970-01-01T05:33:20Z&type=java.lang.Object&id=eid~1&id=eid~2");
  }
}