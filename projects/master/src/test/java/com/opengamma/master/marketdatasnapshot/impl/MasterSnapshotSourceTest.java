/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.marketdatasnapshot.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.DataNotFoundException;
import com.opengamma.core.marketdatasnapshot.NamedSnapshot;
import com.opengamma.core.marketdatasnapshot.StructuredMarketDataSnapshot;
import com.opengamma.core.marketdatasnapshot.impl.ManageableMarketDataSnapshot;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotDocument;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotMaster;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotSearchRequest;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotSearchResult;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.test.TestGroup;

/**
 * Test {@link MasterSnapshotSource}.
 */
@Test(groups = TestGroup.UNIT)
public class MasterSnapshotSourceTest {

  private static final UniqueId UID = UniqueId.of("A", "B");
  private final MasterSnapshotSource _populatedSource = createPopulatedSource();

  /**
   *
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testConstructor1argNullMaster() {
    new MasterSnapshotSource(null);
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  public void testGetSnapshotNoOverrideFound() {
    final MarketDataSnapshotMaster mock = mock(MarketDataSnapshotMaster.class);

    final MarketDataSnapshotDocument doc = new MarketDataSnapshotDocument(example());
    when(mock.get(UID)).thenReturn(doc);
    final MasterSnapshotSource test = new MasterSnapshotSource(mock);
    final StructuredMarketDataSnapshot testResult = test.get(UID);
    verify(mock, times(1)).get(UID);

    assertEquals(example(), testResult);
  }

  // -------------------------------------------------------------------------
  /**
   *
   */
  @Test(expectedExceptions = DataNotFoundException.class, expectedExceptionsMessageRegExp = "Some message")
  public void testGetSnapshotNoOverrideNotFound() {
    final MarketDataSnapshotMaster mock = mock(MarketDataSnapshotMaster.class);

    new MarketDataSnapshotDocument(example());
    when(mock.get(UID)).thenThrow(new DataNotFoundException("Some message"));
    final MasterSnapshotSource test = new MasterSnapshotSource(mock);
    test.get(UID);
  }

  // -------------------------------------------------------------------------
  /**
   * @return an example snapshot
   */
  protected ManageableMarketDataSnapshot example() {
    final ManageableMarketDataSnapshot snapshotDocument = new ManageableMarketDataSnapshot();
    snapshotDocument.setUniqueId(UID);
    return snapshotDocument;
  }

  /**
   *
   */
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testGetSingleWithNoMatchGivesException() {

    final MarketDataSnapshotMaster mock = mock(MarketDataSnapshotMaster.class);
    when(mock.search(Matchers.<MarketDataSnapshotSearchRequest> anyObject()))
        .thenReturn(new MarketDataSnapshotSearchResult(ImmutableList.<MarketDataSnapshotDocument> of()));

    new MasterSnapshotSource(mock).getSingle(NamedSnapshot.class, "I dont exist", VersionCorrection.LATEST);
  }

  /**
   *
   */
  @Test
  public void testMatchByTypeAndNameSucceeds() {
    final NamedSnapshot snapshot = _populatedSource.getSingle(NamedSnapshot.class, "snap1", VersionCorrection.LATEST);
    assertThat(snapshot.getName(), is("snap1"));
  }

  /**
   *
   */
  @Test
  public void testTypeUnmatchedAndNameMatchedStillSucceeds() {
    // Snap 2 is referenced as type SpecialSnapshot so type name won't match
    final NamedSnapshot snapshot = _populatedSource.getSingle(NamedSnapshot.class, "snap2", VersionCorrection.LATEST);
    assertThat(snapshot.getName(), is("snap2"));
  }

  /**
   *
   */
  @Test
  public void testSnapshotHasCorrectType() {
    // Snap 3 is referenced as type SpecialSnapshot so type name won't match
    final ExtraSpecialSnapshot snapshot = _populatedSource.getSingle(ExtraSpecialSnapshot.class, "snap3", VersionCorrection.LATEST);
    assertThat(snapshot.getName(), is("snap3"));
  }

  /**
   *
   */
  @Test
  public void testWrongTypeIsAvoided() {
    // Snap 4 is referenced twice with different types
    final ExtraSpecialSnapshot snapshot = _populatedSource.getSingle(ExtraSpecialSnapshot.class, "snap4", VersionCorrection.LATEST);
    assertThat(snapshot.getName(), is("snap4"));
    assertThat(snapshot.getId(), is(42));
  }

  /**
   *
   */
  @Test
  public void testWithMultipleMatchesFirstIsReturned() {
    // Snap 4 is referenced twice with different types
    final SpecialSnapshot snapshot = _populatedSource.getSingle(SpecialSnapshot.class, "snap4", VersionCorrection.LATEST);
    assertThat(snapshot.getName(), is("snap4"));
    // We get the first one (41 not 42)
    assertThat(snapshot.getId(), is(41));
  }

  /**
   * Show that even if the master has been told the type is X, if the actual object is not compatible with X it is not returned.
   */
  @Test(expectedExceptions = DataNotFoundException.class)
  public void testObjectMatchingCriteriaIsNotReturnedIfWrongType() {
    // Snap 5 is mistyped in the master
    _populatedSource.getSingle(ExtraSpecialSnapshot.class, "snap5", VersionCorrection.LATEST);
  }

  private MasterSnapshotSource createPopulatedSource() {
    return new MasterSnapshotSource(createPopulatedMock());
  }

  private MarketDataSnapshotMaster createPopulatedMock() {

    final SearchResponder responder = new SearchResponder();
    responder.add(NamedSnapshot.class, createNamedSnapshot("snap1"));
    responder.add(SpecialSnapshot.class, new SpecialSnapshot("snap2", 2));
    responder.add(SpecialSnapshot.class, new ExtraSpecialSnapshot("snap3", 3));
    responder.add(SpecialSnapshot.class, new SpecialSnapshot("snap4", 41));
    responder.add(SpecialSnapshot.class, new ExtraSpecialSnapshot("snap4", 42));
    // Entry is deliberately wrong - master has been misinformed about the type
    responder.add(SpecialSnapshot.class, createNamedSnapshot("snap5"));

    final MarketDataSnapshotMaster mock = mock(MarketDataSnapshotMaster.class);
    when(mock.search(Matchers.<MarketDataSnapshotSearchRequest> anyObject())).thenAnswer(new Answer<MarketDataSnapshotSearchResult>() {
      @Override
      public MarketDataSnapshotSearchResult answer(final InvocationOnMock invocation) throws Throwable {
        // Examine the request and use the responder to get matching data
        final MarketDataSnapshotSearchRequest request = (MarketDataSnapshotSearchRequest) invocation.getArguments()[0];
        final List<MarketDataSnapshotDocument> docs = new ArrayList<>();
        final List<NamedSnapshot> matches = responder.getMatches(request.getType(), request.getName());
        for (final NamedSnapshot match : matches) {
          docs.add(new MarketDataSnapshotDocument(match));
        }
        return new MarketDataSnapshotSearchResult(docs);
      }
    });

    return mock;
  }

  private static NamedSnapshot createNamedSnapshot(final String name) {
    return new NamedSnapshot() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public UniqueId getUniqueId() {
        return null;
      }

      @Override
      public NamedSnapshot withUniqueId(final UniqueId uniqueId) {
        return this;
      }
    };
  }

  /**
   * Holds a set of snapshots and responds appropriately as a master would.
   */
  private class SearchResponder {

    /**
     * The values which are held.
     */
    private final List<SnapshotRecord> _values = new ArrayList<>();

    /**
     * Add a new snapshot, recording a type and name.
     *
     * @param type
     *          the type held for the snapshot, not null
     * @param snapshot
     *          the snapshot, not null
     */
    public void add(final Class<? extends NamedSnapshot> type, final NamedSnapshot snapshot) {
      _values.add(new SnapshotRecord(type, snapshot));
    }

    public List<NamedSnapshot> getMatches(final Class<? extends NamedSnapshot> type, final String name) {

      final List<NamedSnapshot> matches = new ArrayList<>();
      for (final SnapshotRecord value : _values) {
        if (value._name.equals(name) && (type == null || value._type.equals(type))) {
          matches.add(value._snapshot);
        }
      }
      return matches;
    }
  }

  /**
   * Simple value class representing a stored snapshot with its name and type.
   */
  private static final class SnapshotRecord {
    public final Class<? extends NamedSnapshot> _type;
    public final String _name;
    public final NamedSnapshot _snapshot;

    private SnapshotRecord(final Class<? extends NamedSnapshot> type, final NamedSnapshot snapshot) {
      this._type = ArgumentChecker.notNull(type, "type");
      this._snapshot = ArgumentChecker.notNull(snapshot, "snapshot");
      this._name = snapshot.getName();
    }
  }

  /**
   * Test implementation of a snapshot.
   */
  class SpecialSnapshot implements NamedSnapshot {

    private final String _name;
    private final int _id;

    /**
     * @param name
     *          the name
     * @param id
     *          the id
     */
    SpecialSnapshot(final String name, final int id) {
      _name = name;
      _id = id;
    }

    @Override
    public String getName() {
      return _name;
    }

    /**
     * @return the id
     */
    public int getId() {
      return _id;
    }

    @Override
    public UniqueId getUniqueId() {
      return null;
    }

    @Override
    public NamedSnapshot withUniqueId(final UniqueId uniqueId) {
      return this;
    }
  }

  /**
   * Test subclass implementation of a snapshot.
   */
  private final class ExtraSpecialSnapshot extends SpecialSnapshot {

    private ExtraSpecialSnapshot(final String name, final int id) {
      super(name, id);
    }
  }

}
