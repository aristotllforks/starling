/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.blotter;

import java.util.Map;
import java.util.Set;

import org.joda.beans.MetaBean;
import org.joda.convert.StringConvert;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.opengamma.DataNotFoundException;
import com.opengamma.id.ExternalScheme;
import com.opengamma.id.UniqueId;
import com.opengamma.master.portfolio.ManageablePortfolio;
import com.opengamma.master.portfolio.ManageablePortfolioNode;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
/* package */ abstract class AbstractTradeBuilder {

  /** Scheme for counterparty external IDs. */
  /* package */ static final ExternalScheme CPTY_SCHEME = ExternalScheme.of("Cpty");
  /** JSON key for the counterparty name. */
  /* package */ static final String COUNTERPARTY = "counterparty";
  /** Counterparty name used if one isn't supplied. */
  /* package */ static final String DEFAULT_COUNTERPARTY = "Default Counterparty";

  /** For loading and saving securities */
  private final SecurityMaster _securityMaster;
  /** For loading and saving positions and trades */
  private final PositionMaster _positionMaster;
  /** For looking up {@link MetaBean}s to build securities */
  @SuppressWarnings("unused")
  private final MetaBeanFactory _metaBeanFactory;
  /** For converting JSON values when building beans */
  private final StringConvert _stringConvert;
  /** For loading and saving portfolios and nodes */
  private final PortfolioMaster _portfolioMaster;

  /* package */ AbstractTradeBuilder(final PositionMaster positionMaster,
      final PortfolioMaster portfolioMaster,
      final SecurityMaster securityMaster,
      final Set<MetaBean> metaBeans,
      final StringConvert stringConvert) {
    ArgumentChecker.notNull(securityMaster, "securityManager");
    ArgumentChecker.notNull(positionMaster, "positionMaster");
    ArgumentChecker.notEmpty(metaBeans, "metaBeans");
    ArgumentChecker.notNull(stringConvert, "stringConvert");
    ArgumentChecker.notNull(portfolioMaster, "portfolioMaster");
    _portfolioMaster = portfolioMaster;
    _positionMaster = positionMaster;
    _securityMaster = securityMaster;
    _metaBeanFactory = new MapMetaBeanFactory(metaBeans);
    _stringConvert = stringConvert;
  }

  protected static Map<String, Object> property(final String name,
      final boolean optional,
      final boolean readOnly,
      final Map<String, Object> typeInfo) {
    return ImmutableMap.<String, Object> of("name", name,
        "type", "single",
        "optional", optional,
        "readOnly", readOnly,
        "types", ImmutableList.of(typeInfo));
  }

  protected static Map<String, Object> attributesProperty() {
    final Map<String, Object> map = Maps.newHashMap();
    map.put("name", "attributes");
    map.put("type", "map");
    map.put("optional", true); // can't be null but have a default value so client doesn't need to specify
    map.put("readOnly", false);
    map.put("types", ImmutableList.of(typeInfo("string", "")));
    map.put("valueTypes", ImmutableList.of(typeInfo("string", "")));
    return map;
  }

  protected static Map<String, Object> typeInfo(final String expectedType, final String actualType) {
    return ImmutableMap.<String, Object> of("beanType", false, "expectedType", expectedType, "actualType", actualType);
  }

  /**
   * Performs a depth first search to find a node with a specified ID.
   * 
   * @param portfolio
   *          The portfolio to search
   * @param nodeId
   *          The node ID
   * @return The node with specified ID, not null
   * @throws DataNotFoundException
   *           If the node can't be found
   */
  /* package */ static ManageablePortfolioNode findNode(final ManageablePortfolio portfolio, final UniqueId nodeId) {
    final ManageablePortfolioNode node = findNode(portfolio.getRootNode(), nodeId);
    if (node != null) {
      return node;
    }
    throw new DataNotFoundException("Node " + nodeId + " not found");
  }

  private static ManageablePortfolioNode findNode(final ManageablePortfolioNode node, final UniqueId nodeId) {
    if (node.getUniqueId().equalObjectId(nodeId)) {
      return node;
    }
    for (final ManageablePortfolioNode childNode : node.getChildNodes()) {
      final ManageablePortfolioNode node1 = findNode(childNode, nodeId);
      if (node1 != null) {
        return node1;
      }
    }
    return null;
  }

  /* package */ SecurityMaster getSecurityMaster() {
    return _securityMaster;
  }

  /* package */ PositionMaster getPositionMaster() {
    return _positionMaster;
  }

  /* package */ StringConvert getStringConvert() {
    return _stringConvert;
  }

  /* package */ PortfolioMaster getPortfolioMaster() {
    return _portfolioMaster;
  }
}
