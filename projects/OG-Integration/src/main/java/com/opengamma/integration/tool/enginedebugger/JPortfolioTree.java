/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.tool.enginedebugger;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.id.ExternalIdDisplayComparator;
import com.opengamma.core.id.ExternalIdDisplayComparatorUtils;
import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.core.security.Security;
import com.opengamma.id.ExternalIdBundle;

/**
 * Tree Cell Renderer for portfolio tree 
 */
public class JPortfolioTree extends JTree {
  private static final long serialVersionUID = 1L;
  private ExternalIdDisplayComparator _idBundleComparator;

  public JPortfolioTree(DefaultTreeModel defaultTreeModel, ConfigSource configSource) {
    super(defaultTreeModel);
    _idBundleComparator = new ExternalIdDisplayComparator();//.getComparator(configSource, ExternalIdDisplayComparatorUtils.DEFAULT_CONFIG_NAME);   
  }

  @Override
  public String convertValueToText(Object value, boolean selected,
                                   boolean expanded, boolean leaf, int row,
                                   boolean hasFocus) {
    if (value != null) {
      if (value instanceof PortfolioNode) {
        PortfolioNode portfolioNode = (PortfolioNode) value;
        return portfolioNode.getName();
      } else if (value instanceof Position) {
        Position position = (Position) value;
        ExternalIdBundle bundle = position.getSecurityLink().getExternalId().withCustomIdOrdering(_idBundleComparator);
        if (!bundle.isEmpty()) {
          return  bundle.iterator().next() + " (" + position.getQuantity() + ")";
        } else {
          return  position.getSecurity().getName() + " (" + position.getQuantity() + ")";
        }
      } else if (value instanceof Trade) {
        Trade trade = (Trade) value;
        return trade.getQuantity() + " on " + trade.getTradeDate();
      } else if (value instanceof Security) {
        Security security = (Security) value;
        return security.getName();
      }
    }
    return "";
  }
}
