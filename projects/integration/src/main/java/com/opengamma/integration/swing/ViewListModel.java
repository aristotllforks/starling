/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.opengamma.core.change.ChangeEvent;
import com.opengamma.core.change.ChangeListener;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.master.config.ConfigDocument;
import com.opengamma.master.config.ConfigMaster;
import com.opengamma.master.config.ConfigSearchRequest;
import com.opengamma.master.config.ConfigSearchResult;

/**
 * List model holding list of views - updates when underlying config master updates.
 */
public class ViewListModel extends AbstractListModel<ViewEntry> implements ComboBoxModel<ViewEntry> {
  /**
   * Version UID
   */
  private static final long serialVersionUID = 1L;
  private final Object _viewsLock = new Object();
  private volatile List<ViewEntry> _views = new ArrayList<>();
  private volatile List<ViewEntry> _filteredViews = new ArrayList<>();
  private ConfigMaster _configMaster;
  private ViewEntry _selectedItem;
  private String _filter;

  public ViewListModel(final ConfigMaster configMaster) {
    _configMaster = configMaster;
    registerListener();
    final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        pullItems();
        runFilter();
        return null;
      }
    };
    worker.execute();
  }

  public void setFilter(final String filter) {
    _filter = filter;
    final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        runFilter();
        return null;
      }
    };
    worker.execute();
  }

  private void runFilter() {
    synchronized (_viewsLock) {
      final List<ViewEntry> filtered = new ArrayList<>();
      for (final ViewEntry entry : _views) {
        if (_filter == null || entry.getName().contains(_filter)) {
          filtered.add(entry);
        }
      }
      _filteredViews = filtered;
    }
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        fireContentsChanged(this, 0, getSize());
      }
    });
  }

  private void pullItems() {
    final ConfigSearchRequest<ViewDefinition> searchReq = new ConfigSearchRequest<>();
    searchReq.setType(ViewDefinition.class);
    final ConfigSearchResult<ViewDefinition> search = _configMaster.search(searchReq);
    synchronized (_viewsLock) {
      _views.clear();
      for (final ConfigDocument document : search.getDocuments()) {
        _views.add(ViewEntry.of(document.getUniqueId(), document.getName()));
      }
      Collections.sort(_views, ViewEntryComparator.getInstance());
    }
  }

  private void registerListener() {
    _configMaster.changeManager().addChangeListener(new ChangeListener() {
      @Override
      public void entityChanged(final ChangeEvent event) {
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override
          protected Void doInBackground() throws Exception {
            pullItems();
            runFilter();
            return null;
          }
        };
        worker.execute();
      }
    });
  }

  @Override
  public int getSize() {
    return _filteredViews.size();
  }

  @Override
  public ViewEntry getElementAt(final int index) {
    return _filteredViews.get(index);
  }

  @Override
  public void setSelectedItem(final Object anItem) {
    _selectedItem = (ViewEntry) anItem;
  }

  @Override
  public Object getSelectedItem() {
    return _selectedItem;
  }

}
