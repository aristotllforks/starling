/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.region.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import com.opengamma.DataNotFoundException;
import com.opengamma.core.region.Region;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.ObjectId;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.region.ManageableRegion;
import com.opengamma.master.region.RegionDocument;
import com.opengamma.master.region.RegionMaster;
import com.opengamma.master.region.RegionSearchRequest;
import com.opengamma.master.region.RegionSearchResult;
import com.opengamma.util.i18n.Country;
import com.opengamma.util.money.Currency;
import com.opengamma.util.paging.PagingRequest;
import com.opengamma.util.test.TestGroup;

/**
 * Test {@link MasterRegionSource}.
 */
@Test(groups = TestGroup.UNIT)
public class MasterRegionSourceTest {

  private static final ObjectId OID = ObjectId.of("A", "B");
  private static final UniqueId UID = UniqueId.of("A", "B", "V");
  private static final ExternalId ID = ExternalId.of("C", "D");
  private static final ExternalIdBundle BUNDLE = ExternalIdBundle.of(ID);
  private static final Instant NOW = Instant.now();
  private static final VersionCorrection VC = VersionCorrection.of(NOW.minusSeconds(2), NOW.minusSeconds(1));

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testConstructorNullMaster() {
    new MasterRegionSource(null);
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  public void testGetRegionUniqueIdNoOverrideFound() {
    final RegionMaster mock = mock(RegionMaster.class);

    final RegionDocument doc = new RegionDocument(example());
    when(mock.get(UID)).thenReturn(doc);
    final MasterRegionSource test = new MasterRegionSource(mock);
    final Region testResult = test.get(UID);
    verify(mock, times(1)).get(UID);

    assertEquals(example(), testResult);
  }

  /**
   *
   */
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetRegionUniqueIdNotFound() {
    final RegionMaster mock = mock(RegionMaster.class);

    when(mock.get(UID)).thenThrow(new DataNotFoundException(""));
    final MasterRegionSource test = new MasterRegionSource(mock);
    try {
      test.get(UID);
    } finally {
      verify(mock, times(1)).get(UID);
    }
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  public void testGetRegionObjectIdFound() {
    final RegionMaster mock = mock(RegionMaster.class);

    final RegionDocument doc = new RegionDocument(example());
    when(mock.get(OID, VC)).thenReturn(doc);
    final MasterRegionSource test = new MasterRegionSource(mock);
    final Region testResult = test.get(OID, VC);
    verify(mock, times(1)).get(OID, VC);

    assertEquals(example(), testResult);
  }

  /**
   *
   */
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetRegionObjectIdNotFound() {
    final RegionMaster mock = mock(RegionMaster.class);

    when(mock.get(OID, VC)).thenThrow(new DataNotFoundException(""));
    final MasterRegionSource test = new MasterRegionSource(mock);
    try {
      test.get(OID, VC);
    } finally {
      verify(mock, times(1)).get(OID, VC);
    }
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  public void testGetHighestLevelRegionExternalIdFound() {
    final RegionMaster mock = mock(RegionMaster.class);
    final RegionSearchRequest request = new RegionSearchRequest(ID);
    request.setPagingRequest(PagingRequest.ONE);

    final RegionSearchResult result = new RegionSearchResult();
    result.getDocuments().add(new RegionDocument(example()));

    when(mock.search(request)).thenReturn(result);
    final MasterRegionSource test = new MasterRegionSource(mock);
    final Region testResult = test.getHighestLevelRegion(ID);
    verify(mock, times(1)).search(request);

    assertEquals(example(), testResult);
  }

  /**
   *
   */
  public void testGetHighestLevelRegionExternalIdNotFound() {
    final RegionMaster mock = mock(RegionMaster.class);
    final RegionSearchRequest request = new RegionSearchRequest(ID);
    request.setPagingRequest(PagingRequest.ONE);

    final RegionSearchResult result = new RegionSearchResult();

    when(mock.search(request)).thenReturn(result);
    final MasterRegionSource test = new MasterRegionSource(mock);
    final Region testResult = test.getHighestLevelRegion(ID);
    verify(mock, times(1)).search(request);

    assertEquals(null, testResult);
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  public void testGetHighestLevelRegionExternalIdBundleFound() {
    final RegionMaster mock = mock(RegionMaster.class);
    final RegionSearchRequest request = new RegionSearchRequest(BUNDLE);
    request.setPagingRequest(PagingRequest.ONE);

    final RegionSearchResult result = new RegionSearchResult();
    result.getDocuments().add(new RegionDocument(example()));

    when(mock.search(request)).thenReturn(result);
    final MasterRegionSource test = new MasterRegionSource(mock);
    final Region testResult = test.getHighestLevelRegion(BUNDLE);
    verify(mock, times(1)).search(request);

    assertEquals(example(), testResult);
  }

  // -------------------------------------------------------------------------
  /**
   * @return an example region
   */
  protected ManageableRegion example() {
    final ManageableRegion region = new ManageableRegion();
    region.setUniqueId(UID);
    region.setName("United Kingdom");
    region.setCurrency(Currency.GBP);
    region.setCountry(Country.GB);
    region.setTimeZone(ZoneId.of("Europe/London"));
    return region;
  }

}
