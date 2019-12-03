/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.opengamma.core.position.Portfolio;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.view.ViewResultModel;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.engine.view.cycle.ViewCycle;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.web.analytics.formatting.TypeFormatter.Format;

/**
 * Wraps another {@link AnalyticsView} and protects it from concurrent access. The methods that can mutate the state of the underlying view are locked with a
 * write lock, the getters are locked with a read lock.
 * 
 * @see com.opengamma.web.analytics Package concurrency notes
 */
/* package */ class LockingAnalyticsView implements AnalyticsView {

  private final AnalyticsView _delegate;
  private final ReadWriteLock _lock = new ReentrantReadWriteLock();

  /**
   * @param delegate
   *          The delegate view (presumably not a thread safe implementation)
   */
  /* package */ LockingAnalyticsView(final AnalyticsView delegate) {
    ArgumentChecker.notNull(delegate, "delegate");
    _delegate = delegate;
  }

  @Override
  public List<String> updateStructure(final CompiledViewDefinition compiledViewDefinition, final Portfolio portfolio) {
    try {
      _lock.writeLock().lock();
      return _delegate.updateStructure(compiledViewDefinition, portfolio);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public String viewCompilationFailed(final Throwable t) {
    try {
      _lock.writeLock().lock();
      return _delegate.viewCompilationFailed(t);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public List<String> updateResults(final ViewResultModel results, final ViewCycle viewCycle) {
    try {
      _lock.writeLock().lock();
      return _delegate.updateResults(results, viewCycle);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public GridStructure getGridStructure(final GridType gridType, final int viewportId) {
    try {
      _lock.readLock().lock();
      return _delegate.getGridStructure(gridType, viewportId);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public GridStructure getInitialGridStructure(final GridType gridType) {
    try {
      _lock.readLock().lock();
      return _delegate.getInitialGridStructure(gridType);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public boolean createViewport(final int requestId, final GridType gridType, final int viewportId, final String callbackId, final String structureCallbackId,
      final ViewportDefinition viewportDefinition) {
    try {
      _lock.writeLock().lock();
      return _delegate.createViewport(requestId, gridType, viewportId, callbackId, structureCallbackId, viewportDefinition);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public String updateViewport(final GridType gridType, final int viewportId, final ViewportDefinition viewportDefinition) {
    try {
      _lock.writeLock().lock();
      return _delegate.updateViewport(gridType, viewportId, viewportDefinition);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public void deleteViewport(final GridType gridType, final int viewportId) {
    try {
      _lock.writeLock().lock();
      _delegate.deleteViewport(gridType, viewportId);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public ViewportResults getData(final GridType gridType, final int viewportId) {
    try {
      _lock.readLock().lock();
      return _delegate.getData(gridType, viewportId);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public void openDependencyGraph(final int requestId, final GridType gridType, final int graphId, final String callbackId, final int row, final int col) {
    try {
      _lock.writeLock().lock();
      _delegate.openDependencyGraph(requestId, gridType, graphId, callbackId, row, col);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public void openDependencyGraph(final int requestId,
      final GridType gridType,
      final int graphId,
      final String callbackId,
      final String calcConfigName,
      final ValueRequirement valueRequirement) {
    try {
      _lock.writeLock().lock();
      _delegate.openDependencyGraph(requestId, gridType, graphId, callbackId, calcConfigName, valueRequirement);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public void closeDependencyGraph(final GridType gridType, final int graphId) {
    try {
      _lock.writeLock().lock();
      _delegate.closeDependencyGraph(gridType, graphId);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public GridStructure getGridStructure(final GridType gridType, final int graphId, final int viewportId) {
    try {
      _lock.readLock().lock();
      return _delegate.getGridStructure(gridType, graphId, viewportId);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public GridStructure getInitialGridStructure(final GridType gridType, final int graphId) {
    try {
      _lock.readLock().lock();
      return _delegate.getInitialGridStructure(gridType, graphId);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public boolean createViewport(final int requestId, final GridType gridType, final int graphId, final int viewportId, final String callbackId,
      final String structureCallbackId, final ViewportDefinition viewportDefinition) {
    try {
      _lock.writeLock().lock();
      return _delegate.createViewport(requestId, gridType, graphId, viewportId, callbackId, structureCallbackId, viewportDefinition);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public String updateViewport(final GridType gridType, final int graphId, final int viewportId, final ViewportDefinition viewportDefinition) {
    try {
      _lock.writeLock().lock();
      return _delegate.updateViewport(gridType, graphId, viewportId, viewportDefinition);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public void deleteViewport(final GridType gridType, final int graphId, final int viewportId) {
    try {
      _lock.writeLock().lock();
      _delegate.deleteViewport(gridType, graphId, viewportId);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public ViewportResults getData(final GridType gridType, final int graphId, final int viewportId) {
    try {
      _lock.readLock().lock();
      return _delegate.getData(gridType, graphId, viewportId);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public List<String> entityChanged(final MasterChangeNotification<?> notification) {
    try {
      _lock.writeLock().lock();
      return _delegate.entityChanged(notification);
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public List<String> portfolioChanged() {
    try {
      _lock.writeLock().lock();
      return _delegate.portfolioChanged();
    } finally {
      _lock.writeLock().unlock();
    }
  }

  @Override
  public ViewportResults getAllGridData(final GridType gridType, final Format format) {
    try {
      _lock.readLock().lock();
      return _delegate.getAllGridData(gridType, format);
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public UniqueId getViewDefinitionId() {
    try {
      _lock.readLock().lock();
      return _delegate.getViewDefinitionId();
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public List<ErrorInfo> getErrors() {
    try {
      _lock.readLock().lock();
      return _delegate.getErrors();
    } finally {
      _lock.readLock().unlock();
    }
  }

  @Override
  public void deleteError(final long id) {
    try {
      _lock.writeLock().lock();
      _delegate.deleteError(id);
    } finally {
      _lock.writeLock().unlock();
    }
  }
}
