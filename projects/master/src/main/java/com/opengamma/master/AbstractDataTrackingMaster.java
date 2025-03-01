/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.opengamma.core.change.ChangeManager;
import com.opengamma.id.ObjectIdentifiable;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;

/**
 * A master which provides the ability to track which records are read from a master.
 * (Writes are ignored).
 * @param <D> the document type
 * @param <M> the master
 */
public abstract class AbstractDataTrackingMaster<D extends AbstractDocument, M extends AbstractChangeProvidingMaster<D>>
implements AbstractChangeProvidingMaster<D> {

  private final M _delegate;
  private final Set<UniqueId> _ids = Sets.newHashSet();


  /**
   * @param delegate the master to delegate calls to
   */
  protected AbstractDataTrackingMaster(final M delegate) {
    _delegate = ArgumentChecker.notNull(delegate, "delegate");
  }

  /**
   * @return the delegate used by this master
   */
  protected M delegate() {
    return _delegate;
  }

  /**
   * Track access to the given doc (via its {@link UniqueId}).
   * @param doc the document to track
   * @return the document passed
   */
  protected synchronized D trackDoc(final D doc) {
    if (doc == null) {
      return null;
    }
    trackDocs(Collections.singleton(doc));
    return doc;
  }

  /**
   * Track access to the given docs (via their {@link UniqueId}s).
   * @param docs the documents to track
   * @return the documents passed
   */
  protected synchronized Iterable<D> trackDocs(final Iterable<D> docs) {
    if (docs == null) {
      return null;
    }
    for (final D doc : docs) {
      final UniqueId uniqueId = doc.getUniqueId();
      trackId(uniqueId);
    }
    return docs;
  }

  /**
   * Track access to the given ids.
   * @param ids the ids to track
   * @param <T> the type of iterable used
   * @return the ids passed
   */
  protected synchronized <T extends Iterable<UniqueId>> T trackIds(final T ids) {
    if (ids == null) {
      return null;
    }
    for (final UniqueId id : ids) {
      trackId(id);
    }
    return ids;
  }

  /**
   * Track access to the given id.
   * @param id the id to track
   * @return the id passed
   */
  protected synchronized UniqueId trackId(final UniqueId id) {
    if (id == null) {
      return null;
    }
    _ids.add(id);
    return id;
  }



  /**
   * Returns an immutable set of the ids accessed in this master's lifetime.
   * @return the ids accessed from this master
   */
  public synchronized ImmutableSet<UniqueId> getIdsAccessed() {
    return ImmutableSet.copyOf(_ids);
  }

  //--------------------------------------------------------------------------------
  // common master method implementations
  //--------------------------------------------------------------------------------

  @Override
  public D get(final UniqueId uniqueId) {
    return trackDoc(delegate().get(uniqueId));
  }

  @Override
  public D get(final ObjectIdentifiable objectId, final VersionCorrection versionCorrection) {
    return trackDoc(delegate().get(objectId, versionCorrection));
  }

  @Override
  public Map<UniqueId, D> get(final Collection<UniqueId> uniqueIds) {
    final Map<UniqueId, D> result = delegate().get(uniqueIds);
    trackDocs(result.values());
    return result;
  }

  @Override
  public D add(final D document) {
    return trackDoc(delegate().add(document));
  }

  @Override
  public D update(final D document) {
    return trackDoc(delegate().update(document));
  }

  @Override
  public void remove(final ObjectIdentifiable oid) {
    //can't track here
    delegate().remove(oid);
  }

  @Override
  public D correct(final D document) {
    return trackDoc(delegate().correct(document));
  }

  @Override
  public List<UniqueId> replaceVersion(final UniqueId uniqueId, final List<D> replacementDocuments) {
    return trackIds(delegate().replaceVersion(uniqueId, replacementDocuments));
  }

  @Override
  public List<UniqueId> replaceAllVersions(final ObjectIdentifiable objectId, final List<D> replacementDocuments) {
    return trackIds(delegate().replaceAllVersions(objectId, replacementDocuments));
  }

  @Override
  public List<UniqueId> replaceVersions(final ObjectIdentifiable objectId, final List<D> replacementDocuments) {
    return trackIds(delegate().replaceVersions(objectId, replacementDocuments));
  }

  @Override
  public UniqueId replaceVersion(final D replacementDocument) {
    return trackId(delegate().replaceVersion(replacementDocument));
  }

  @Override
  public void removeVersion(final UniqueId uniqueId) {
    //ignore this
    delegate().removeVersion(uniqueId);
  }

  @Override
  public UniqueId addVersion(final ObjectIdentifiable objectId, final D documentToAdd) {
    return trackId(delegate().addVersion(objectId, documentToAdd));
  }

  @Override
  public ChangeManager changeManager() {
    return delegate().changeManager();
  }


}
