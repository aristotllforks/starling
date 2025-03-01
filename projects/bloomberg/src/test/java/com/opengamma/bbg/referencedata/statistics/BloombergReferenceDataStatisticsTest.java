package com.opengamma.bbg.referencedata.statistics;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.tuple.Pair;
import com.opengamma.util.tuple.Pairs;

/**
 * Test.
 */
public class BloombergReferenceDataStatisticsTest {

  @Test(groups = TestGroup.UNIT)
  public void singleTest() {
    final MapBloombergReferenceDataStatistics stats = new MapBloombergReferenceDataStatistics();
    stats.recordStatistics(Collections.singleton("1"), Collections.singleton("A"));
    assertEquals(1, stats.getTotalGetsCount());
    final Snapshot snap1 = stats.getSnapshot();
    assertEquals(1, snap1.getLookupsByField().size());
    assertEquals(1, snap1.getLookupsBySecurity().size());
    assertEquals(1, snap1.getTotalLookups());
    assertEquals(1, snap1.getLookupsByField().get(0).getFirst().longValue());
    assertEquals(1, snap1.getLookupsBySecurity().get(0).getFirst().longValue());

    stats.recordStatistics(Collections.singleton("1"), Collections.singleton("A"));
    assertEquals(2, stats.getTotalGetsCount());
    final Snapshot snap2 = stats.getSnapshot();
    assertEquals(1, snap2.getLookupsByField().size());
    assertEquals(1, snap2.getLookupsBySecurity().size());

    assertEquals(2, snap2.getTotalLookups());
    assertEquals(2, snap2.getLookupsByField().get(0).getFirst().longValue());
    assertEquals(2, snap2.getLookupsBySecurity().get(0).getFirst().longValue());
  }

  @Test(groups = TestGroup.UNIT)
  public void multiTest() {
    final MapBloombergReferenceDataStatistics stats = new MapBloombergReferenceDataStatistics();
    stats.recordStatistics(Collections.singleton("1"), Collections.singleton("A"));
    assertEquals(1, stats.getTotalGetsCount());
    final Snapshot snap1 = stats.getSnapshot();
    assertEquals(1, snap1.getLookupsByField().size());
    assertEquals(1, snap1.getLookupsBySecurity().size());
    assertEquals(1, snap1.getTotalLookups());
    assertEquals(1, snap1.getLookupsByField().get(0).getFirst().longValue());
    assertEquals(1, snap1.getLookupsBySecurity().get(0).getFirst().longValue());

    stats.recordStatistics(Sets.newHashSet("1", "2"), Collections.singleton("B"));
    assertEquals(3, stats.getTotalGetsCount());
    final Snapshot snap2 = stats.getSnapshot();
    assertEquals(2, snap2.getLookupsByField().size());
    assertEquals(2, snap2.getLookupsBySecurity().size());

    assertEquals(3, snap2.getTotalLookups());
    assertEquals(2, snap2.getLookupsByField().size());
    assertEquals(2, snap2.getLookupsBySecurity().size());

    assertPairEquals(Pairs.of(2, "1"), snap2.getLookupsBySecurity().get(0));
    assertPairEquals(Pairs.of(1, "2"), snap2.getLookupsBySecurity().get(1));

    assertPairEquals(Pairs.of(2, "B"), snap2.getLookupsByField().get(0));
    assertPairEquals(Pairs.of(1, "A"), snap2.getLookupsByField().get(1));
  }

  @Test(groups = TestGroup.UNIT_SLOW)
  public void bigTest() throws InterruptedException {
    assertSmall(new Supplier<MapBloombergReferenceDataStatistics>() {
      @Override
      public MapBloombergReferenceDataStatistics get() {
        return getBigStats();
      }
    }, 10 * 1024 * 1024, "Stats");
  }

  @Test(groups = TestGroup.INTEGRATION)  // mark as integration because its a bit random
  public void bigIncrementTest() throws InterruptedException {
    final MapBloombergReferenceDataStatistics stats = getBigStats();
    assertSmall(new Supplier<Object>() {
      @Override
      public Object get() {
        incrementBigStats(stats);
        return null;
      }
    }, 1 * 1024 * 1024, "Increment");
  }

  @Test(groups = TestGroup.INTEGRATION)  // mark as integration because its a bit random
  public void bigSnapshotTest() throws InterruptedException {
    final MapBloombergReferenceDataStatistics stats = getBigStats();
    assertSmall(new Supplier<Snapshot>() {
      @Override
      public Snapshot get() {
        return stats.getSnapshot();
      }
    }, 10 * 1024 * 1024, "Snapshot");
  }

  @Test(enabled = false)
  public void fastTest() throws InterruptedException {
    final MapBloombergReferenceDataStatistics stats = getBigStats();

    for (int i = 0; i < 10; i++) {
      final long start = System.currentTimeMillis();
      final int repeats = 20;
      for (int n = 0; n < repeats; n++) {
        stats.getSnapshot();
      }
      final long elapsed = System.currentTimeMillis() - start;
      assertTrue(elapsed < repeats * 1000);
    }
  }

  private <T> void assertSmall(final Supplier<T> factory, final long maxSize, final String name) throws InterruptedException {
    assertSmallAndFast(factory, maxSize, null, name);
  }

  private <T> void assertSmallAndFast(final Supplier<T> factory, final Long maxSize, final Long maxTime, final String name) throws InterruptedException {
    final int blocks = 3;
    final int blockSize = 10;

    final List<T> list = new ArrayList<>(blocks * blockSize);
    final long baseline = getUsedMemory();
    for (int i = 0; i < blocks; i++) {
      final long start = System.currentTimeMillis();
      for (int n = 0; n < blockSize; n++) {
        list.add(factory.get());
      }
      final long elapsed = System.currentTimeMillis() - start;
      final long used = getUsedMemory() - baseline;
      final long usedPerUnit = used / ((i + 1) * blockSize);
      final long elapsedPerUnit = elapsed / blockSize;
      System.out.println("Used ~" + usedPerUnit + "bytes and " + elapsedPerUnit + "ms per " + name);
      if (maxSize != null) {
        assertLessThan(maxSize, usedPerUnit);
      }
      if (maxTime != null) {
        assertLessThan(maxTime, elapsedPerUnit);
      }
    }
  }

  private void assertLessThan(final long expected, final long actual) {
    if (actual > expected) {
      throw new AssertionError(actual + " > " + expected);
    }
  }

  private long getUsedMemory() {
    System.gc();
    System.gc();
    return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  private MapBloombergReferenceDataStatistics getBigStats() {
    final MapBloombergReferenceDataStatistics stats = new MapBloombergReferenceDataStatistics();
    return incrementBigStats(stats);
  }

  private static final Set<String> SECURITIES = getInts(100000);
  private static final Set<String> FIELDS = getInts(100);

  private MapBloombergReferenceDataStatistics incrementBigStats(final MapBloombergReferenceDataStatistics stats) {
    stats.recordStatistics(SECURITIES, FIELDS);
    return stats;
  }

  private static Set<String> getInts(final int count) {
    final HashSet<String> ret = new HashSet<>(count);
    for (int n = 0; n < count; n++) {
      ret.add(Integer.toString(n));
    }
    return ret;
  }

  private <TValue> void assertPairEquals(final Pair<? extends Number, TValue> expected, final Pair<? extends Number, TValue> actual) {
    assertEquals(expected.getFirst().longValue(), actual.getFirst().longValue());
    assertEquals(expected.getSecond(), actual.getSecond());
  }

}
