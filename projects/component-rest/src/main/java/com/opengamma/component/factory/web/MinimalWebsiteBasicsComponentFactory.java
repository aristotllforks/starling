/**
 * Copyright (C) 2015 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.opengamma.component.factory.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.batch.BatchMaster;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.rest.JerseyRestResourceFactory;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.function.config.FunctionConfigurationSource;
import com.opengamma.engine.marketdata.live.LiveMarketDataProviderFactory;
import com.opengamma.engine.target.ComputationTargetTypeProvider;
import com.opengamma.engine.target.DefaultComputationTargetTypeProvider;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.id.ExternalScheme;
import com.opengamma.master.config.ConfigMaster;
import com.opengamma.master.config.impl.MasterConfigSource;
import com.opengamma.master.convention.ConventionMaster;
import com.opengamma.master.exchange.ExchangeMaster;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesLoader;
import com.opengamma.master.historicaltimeseries.HistoricalTimeSeriesMaster;
import com.opengamma.master.holiday.HolidayMaster;
import com.opengamma.master.legalentity.LegalEntityMaster;
import com.opengamma.master.marketdatasnapshot.MarketDataSnapshotMaster;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.region.RegionMaster;
import com.opengamma.master.security.SecurityLoader;
import com.opengamma.master.security.SecurityMaster;
import com.opengamma.master.user.UserMaster;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.auth.AuthUtils;
import com.opengamma.web.MinimalWebHomeResource;
import com.opengamma.web.WebAboutResource;
import com.opengamma.web.config.WebConfigsResource;
import com.opengamma.web.convention.WebConventionsResource;
import com.opengamma.web.exchange.WebExchangesResource;
import com.opengamma.web.function.WebFunctionsResource;
import com.opengamma.web.historicaltimeseries.WebAllHistoricalTimeSeriesResource;
import com.opengamma.web.holiday.HolidayLoaderResource;
import com.opengamma.web.holiday.WebHolidaysResource;
import com.opengamma.web.legalentity.WebLegalEntitiesResource;
import com.opengamma.web.portfolio.MinimalWebPortfoliosResource;
import com.opengamma.web.position.MinimalWebPositionsResource;
import com.opengamma.web.region.WebRegionsResource;
import com.opengamma.web.security.MinimalWebSecuritiesResource;
import com.opengamma.web.target.WebComputationTargetTypeResource;
import com.opengamma.web.user.WebLoginResource;
import com.opengamma.web.user.WebLogoutResource;
import com.opengamma.web.user.WebProfileResource;
import com.opengamma.web.user.WebRegisterResource;
import com.opengamma.web.user.WebRolesResource;
import com.opengamma.web.user.WebUsersResource;
import com.opengamma.web.valuerequirementname.WebValueRequirementNamesResource;

/**
 *
 */
@BeanDefinition
public class MinimalWebsiteBasicsComponentFactory extends AbstractComponentFactory {
  /**
   * The config master.
   */
  @PropertyDefinition(validate = "notNull")
  private ConfigMaster _configMaster;
  /**
   * The user master.
   */
  @PropertyDefinition
  private UserMaster _userMaster;
  /**
   * The password service.
   */
  @PropertyDefinition
  private PasswordService _passwordService;
  /**
   * The exchange master.
   */
  @PropertyDefinition
  private ExchangeMaster _exchangeMaster;
  /**
   * The holiday master.
   */
  @PropertyDefinition
  private HolidayMaster _holidayMaster;
  /**
   * The underlying master.
   */
  @PropertyDefinition
  private RegionMaster _regionMaster;
  /**
   * The security master.
   */
  @PropertyDefinition
  private SecurityMaster _securityMaster;
  /**
   * The security source.
   */
  @PropertyDefinition
  private SecuritySource _securitySource;
  /**
   * The security loader.
   */
  @PropertyDefinition
  private SecurityLoader _securityLoader;
  /**
   * The convention master.
   */
  @PropertyDefinition
  private ConventionMaster _conventionMaster;
  /**
   * The legal entity master.
   */
  @PropertyDefinition
  private LegalEntityMaster _legalEntityMaster;
  /**
   * The position master.
   */
  @PropertyDefinition
  private PositionMaster _positionMaster;
  /**
   * The portfolio master.
   */
  @PropertyDefinition
  private PortfolioMaster _portfolioMaster;
  /**
   * The time-series master.
   */
  @PropertyDefinition
  private HistoricalTimeSeriesMaster _historicalTimeSeriesMaster;
  /**
   * The time-series source.
   */
  @PropertyDefinition
  private HistoricalTimeSeriesSource _historicalTimeSeriesSource;
  /**
   * The time-series loader.
   */
  @PropertyDefinition
  private HistoricalTimeSeriesLoader _historicalTimeSeriesLoader;
  /**
   * The scheduler.
   */
  @PropertyDefinition
  private ScheduledExecutorService _scheduler;
  /**
   * The available computation target types.
   */
  @PropertyDefinition
  private ComputationTargetTypeProvider _targetTypes = new DefaultComputationTargetTypeProvider();
  /**
   * The market data snapshot master.
   */
  @PropertyDefinition
  private MarketDataSnapshotMaster _marketDataSnapshotMaster;
  /**
   * The function configuration source.
   */
  @PropertyDefinition
  private FunctionConfigurationSource _functionConfigurationSource;
  /**
   * The batch master.
   */
  @PropertyDefinition
  private BatchMaster _batchMaster;
  /**
   * For obtaining the live market data provider names.
   */
  @PropertyDefinition
  private LiveMarketDataProviderFactory _liveMarketDataProviderFactory;
  /**
   * The view processor.
   */
  @PropertyDefinition
  private ViewProcessor _viewProcessor;
  /**
   * The computation target resolver.
   */
  @PropertyDefinition
  private ComputationTargetResolver _computationTargetResolver;

  /**
   * The external Scheme configuration e.g BLOOMBERG_TICKER:Bloomberg Ticker,BLOOMBERG_TCM:Bloomberg Ticker/Coupon/Maturity.
   */
  @PropertyDefinition
  private String _externalSchemes;

  // -------------------------------------------------------------------------
  @Override
  public void init(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {
    final Set<Class<?>> publishedTypes = initMasters(repo, getExternalSchemesMap());
    initBasics(repo, publishedTypes);
    initValueRequirementNames(repo, configuration);
  }

  private Map<ExternalScheme, String> getExternalSchemesMap() {
    Map<ExternalScheme, String> externalSchemes = new HashMap<>();
    if (StringUtils.trimToNull(getExternalSchemes()) != null) {
      final String[] schemeTokens = getExternalSchemes().split(",");
      for (String keyValueStr : schemeTokens) {
        keyValueStr = StringUtils.trimToNull(keyValueStr);
        if (keyValueStr != null) {
          final String[] externalScheme = keyValueStr.split(":");
          if (externalScheme.length != 2) {
            throw new OpenGammaRuntimeException("Invalid external schemes configuration [" + getExternalSchemes() + "]");
          }
          externalSchemes.put(ExternalScheme.of(externalScheme[0]), externalScheme[1]);
        }
      }
    }
    if (externalSchemes.isEmpty()) {
      externalSchemes = getDefaultExternalSchemeMappings();
    }
    return externalSchemes;
  }

  private Map<ExternalScheme, String> getDefaultExternalSchemeMappings() {
    final Map<ExternalScheme, String> externalSchemes = new HashMap<>();
    externalSchemes.put(ExternalSchemes.BLOOMBERG_TICKER, "Bloomberg Ticker");
    externalSchemes.put(ExternalSchemes.BLOOMBERG_BUID, "Bloomberg BUID");
    externalSchemes.put(ExternalSchemes.CUSIP, "Cusip");
    externalSchemes.put(ExternalSchemes.OG_SYNTHETIC_TICKER, "OG Synthetic Ticker");
    externalSchemes.put(ExternalSchemes.ISIN, "Isin");
    externalSchemes.put(ExternalSchemes.RIC, "Ric");
    externalSchemes.put(ExternalSchemes.SEDOL1, "Sedol");
    return externalSchemes;
  }

  protected void initBasics(final ComponentRepository repo, final Set<Class<?>> publishedTypes) {
    if (!AuthUtils.isPermissive()) {
      ArgumentChecker.notNull(getUserMaster(), "UserMaster");
      ArgumentChecker.notNull(getPasswordService(), "PasswordService");
      repo.getRestComponents().publishResource(new WebLoginResource());
      repo.getRestComponents().publishResource(new WebLogoutResource());
      repo.getRestComponents().publishResource(new WebRegisterResource(getUserMaster(), getPasswordService()));
      repo.getRestComponents().publishResource(new WebProfileResource(getUserMaster(), getPasswordService()));
    }
    repo.getRestComponents().publishResource(new MinimalWebHomeResource(publishedTypes));
    repo.getRestComponents().publishResource(new WebAboutResource());
  }

  protected Set<Class<?>> initMasters(final ComponentRepository repo, final Map<ExternalScheme, String> externalSchemes) {
    final MasterConfigSource configSource = new MasterConfigSource(getConfigMaster());
    final Map<Class<?>, List<Object>> resourceParameters = extractResourceParams(externalSchemes, configSource);
    final Set<Class<?>> publishedTypes = Sets.newHashSet();
    for (final Class<?> clazz : resourceParameters.keySet()) {
      final List<Object> params = resourceParameters.get(clazz);
      final JerseyRestResourceFactory resource = createRestFactory(clazz, params);
      if (resource != null) {
        repo.getRestComponents().publishResource(resource);
        publishedTypes.add(clazz);
      }
    }
    return publishedTypes;
  }

  // builds a map of type -> constructor params
  private Map<Class<?>, List<Object>> extractResourceParams(final Map<ExternalScheme, String> externalSchemes, final MasterConfigSource configSource) {
    final Map<Class<?>, List<Object>> resourceParameters = Maps.newHashMap();
    resourceParameters.put(WebConfigsResource.class, params(getConfigMaster()));
    resourceParameters.put(WebUsersResource.class, params(getUserMaster(), getPasswordService()));
    resourceParameters.put(WebRolesResource.class, params(getUserMaster()));
    resourceParameters.put(WebExchangesResource.class, params(getExchangeMaster()));
    resourceParameters.put(WebHolidaysResource.class, params(getHolidayMaster()));
    resourceParameters.put(WebRegionsResource.class, params(getRegionMaster()));
    resourceParameters.put(WebConventionsResource.class, params(getConventionMaster()));
    resourceParameters.put(WebLegalEntitiesResource.class, params(getLegalEntityMaster(), getSecurityMaster()));
    if (getSecurityLoader() != null) {
      if (getHistoricalTimeSeriesMaster() != null) {
        // same behaviour as OpenGamma
        resourceParameters.put(MinimalWebPositionsResource.class,
            params(getPositionMaster(), getSecurityLoader(), getSecuritySource(), getHistoricalTimeSeriesSource(), externalSchemes));
        if (getLegalEntityMaster() != null) {
          // same behaviour as OpenGamma
          resourceParameters.put(MinimalWebSecuritiesResource.class,
              params(getSecurityMaster(), getSecurityLoader(), getHistoricalTimeSeriesMaster(), getLegalEntityMaster()));
        } else {
          resourceParameters.put(MinimalWebSecuritiesResource.class, params(getSecurityMaster(), getSecurityLoader()));
        }
      }
    } else {
      resourceParameters.put(MinimalWebSecuritiesResource.class, params(getSecurityMaster()));
      resourceParameters.put(MinimalWebPositionsResource.class, params(getPositionMaster(), getSecuritySource(), externalSchemes));
    }
    resourceParameters.put(MinimalWebPortfoliosResource.class, params(getPortfolioMaster(), getPositionMaster(), getSecuritySource(), getScheduler()));
    resourceParameters.put(WebAllHistoricalTimeSeriesResource.class, params(getHistoricalTimeSeriesMaster(), getHistoricalTimeSeriesLoader(), configSource));
    resourceParameters.put(WebComputationTargetTypeResource.class, params(getTargetTypes()));
    // TODO
    // resourceParameters.put(WebMarketDataSnapshotsResource.class, params(getMarketDataSnapshotMaster(), getConfigMaster(),
    // Optional.fromNullable(getLiveMarketDataProviderFactory()), configSource, getComputationTargetResolver(), getViewProcessor(),
    // getHistoricalTimeSeriesSource()));
    resourceParameters.put(WebFunctionsResource.class, params(getFunctionConfigurationSource()));
    if (getHolidayMaster() != null) {
      resourceParameters.put(HolidayLoaderResource.class, params(getHolidayMaster()));
    }
    return resourceParameters;
  }

  // needed to get generics right
  private List<Object> params(final Object... params) {
    return Lists.newArrayList(params);
  }

  private JerseyRestResourceFactory createRestFactory(final Class<?> type, final List<Object> arguments) {
    // if data not available do not build
    if (Iterables.contains(arguments, null)) {
      return null;
    }
    // unbox optional and build
    for (final ListIterator<Object> it = arguments.listIterator(); it.hasNext();) {
      final Object obj = it.next();
      if (obj instanceof Optional) {
        final Object wrappedObj = ((Optional<?>) obj).orNull();
        it.set(wrappedObj);
      }
    }
    return new JerseyRestResourceFactory(type, arguments.toArray(new Object[arguments.size()]));
  }

  protected void initValueRequirementNames(final ComponentRepository repo, final LinkedHashMap<String, String> configuration) {
    final String valueRequirementNameClasses = configuration.get(WebValueRequirementNamesResource.VALUE_REQUIREMENT_NAME_CLASSES);
    configuration.remove(WebValueRequirementNamesResource.VALUE_REQUIREMENT_NAME_CLASSES);

    if (valueRequirementNameClasses == null) {
      repo.getRestComponents().publishResource(new WebValueRequirementNamesResource());
    } else if (valueRequirementNameClasses.contains(",")) {
      repo.getRestComponents().publishResource(new WebValueRequirementNamesResource(valueRequirementNameClasses.split(",")));
    } else {
      repo.getRestComponents().publishResource(new WebValueRequirementNamesResource(new String[] { valueRequirementNameClasses }));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MinimalWebsiteBasicsComponentFactory}.
   * @return the meta-bean, not null
   */
  public static MinimalWebsiteBasicsComponentFactory.Meta meta() {
    return MinimalWebsiteBasicsComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MinimalWebsiteBasicsComponentFactory.Meta.INSTANCE);
  }

  @Override
  public MinimalWebsiteBasicsComponentFactory.Meta metaBean() {
    return MinimalWebsiteBasicsComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the config master.
   * @return the value of the property, not null
   */
  public ConfigMaster getConfigMaster() {
    return _configMaster;
  }

  /**
   * Sets the config master.
   * @param configMaster  the new value of the property, not null
   */
  public void setConfigMaster(ConfigMaster configMaster) {
    JodaBeanUtils.notNull(configMaster, "configMaster");
    this._configMaster = configMaster;
  }

  /**
   * Gets the the {@code configMaster} property.
   * @return the property, not null
   */
  public final Property<ConfigMaster> configMaster() {
    return metaBean().configMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the user master.
   * @return the value of the property
   */
  public UserMaster getUserMaster() {
    return _userMaster;
  }

  /**
   * Sets the user master.
   * @param userMaster  the new value of the property
   */
  public void setUserMaster(UserMaster userMaster) {
    this._userMaster = userMaster;
  }

  /**
   * Gets the the {@code userMaster} property.
   * @return the property, not null
   */
  public final Property<UserMaster> userMaster() {
    return metaBean().userMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the password service.
   * @return the value of the property
   */
  public PasswordService getPasswordService() {
    return _passwordService;
  }

  /**
   * Sets the password service.
   * @param passwordService  the new value of the property
   */
  public void setPasswordService(PasswordService passwordService) {
    this._passwordService = passwordService;
  }

  /**
   * Gets the the {@code passwordService} property.
   * @return the property, not null
   */
  public final Property<PasswordService> passwordService() {
    return metaBean().passwordService().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange master.
   * @return the value of the property
   */
  public ExchangeMaster getExchangeMaster() {
    return _exchangeMaster;
  }

  /**
   * Sets the exchange master.
   * @param exchangeMaster  the new value of the property
   */
  public void setExchangeMaster(ExchangeMaster exchangeMaster) {
    this._exchangeMaster = exchangeMaster;
  }

  /**
   * Gets the the {@code exchangeMaster} property.
   * @return the property, not null
   */
  public final Property<ExchangeMaster> exchangeMaster() {
    return metaBean().exchangeMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the holiday master.
   * @return the value of the property
   */
  public HolidayMaster getHolidayMaster() {
    return _holidayMaster;
  }

  /**
   * Sets the holiday master.
   * @param holidayMaster  the new value of the property
   */
  public void setHolidayMaster(HolidayMaster holidayMaster) {
    this._holidayMaster = holidayMaster;
  }

  /**
   * Gets the the {@code holidayMaster} property.
   * @return the property, not null
   */
  public final Property<HolidayMaster> holidayMaster() {
    return metaBean().holidayMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying master.
   * @return the value of the property
   */
  public RegionMaster getRegionMaster() {
    return _regionMaster;
  }

  /**
   * Sets the underlying master.
   * @param regionMaster  the new value of the property
   */
  public void setRegionMaster(RegionMaster regionMaster) {
    this._regionMaster = regionMaster;
  }

  /**
   * Gets the the {@code regionMaster} property.
   * @return the property, not null
   */
  public final Property<RegionMaster> regionMaster() {
    return metaBean().regionMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security master.
   * @return the value of the property
   */
  public SecurityMaster getSecurityMaster() {
    return _securityMaster;
  }

  /**
   * Sets the security master.
   * @param securityMaster  the new value of the property
   */
  public void setSecurityMaster(SecurityMaster securityMaster) {
    this._securityMaster = securityMaster;
  }

  /**
   * Gets the the {@code securityMaster} property.
   * @return the property, not null
   */
  public final Property<SecurityMaster> securityMaster() {
    return metaBean().securityMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security source.
   * @return the value of the property
   */
  public SecuritySource getSecuritySource() {
    return _securitySource;
  }

  /**
   * Sets the security source.
   * @param securitySource  the new value of the property
   */
  public void setSecuritySource(SecuritySource securitySource) {
    this._securitySource = securitySource;
  }

  /**
   * Gets the the {@code securitySource} property.
   * @return the property, not null
   */
  public final Property<SecuritySource> securitySource() {
    return metaBean().securitySource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the security loader.
   * @return the value of the property
   */
  public SecurityLoader getSecurityLoader() {
    return _securityLoader;
  }

  /**
   * Sets the security loader.
   * @param securityLoader  the new value of the property
   */
  public void setSecurityLoader(SecurityLoader securityLoader) {
    this._securityLoader = securityLoader;
  }

  /**
   * Gets the the {@code securityLoader} property.
   * @return the property, not null
   */
  public final Property<SecurityLoader> securityLoader() {
    return metaBean().securityLoader().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the convention master.
   * @return the value of the property
   */
  public ConventionMaster getConventionMaster() {
    return _conventionMaster;
  }

  /**
   * Sets the convention master.
   * @param conventionMaster  the new value of the property
   */
  public void setConventionMaster(ConventionMaster conventionMaster) {
    this._conventionMaster = conventionMaster;
  }

  /**
   * Gets the the {@code conventionMaster} property.
   * @return the property, not null
   */
  public final Property<ConventionMaster> conventionMaster() {
    return metaBean().conventionMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the legal entity master.
   * @return the value of the property
   */
  public LegalEntityMaster getLegalEntityMaster() {
    return _legalEntityMaster;
  }

  /**
   * Sets the legal entity master.
   * @param legalEntityMaster  the new value of the property
   */
  public void setLegalEntityMaster(LegalEntityMaster legalEntityMaster) {
    this._legalEntityMaster = legalEntityMaster;
  }

  /**
   * Gets the the {@code legalEntityMaster} property.
   * @return the property, not null
   */
  public final Property<LegalEntityMaster> legalEntityMaster() {
    return metaBean().legalEntityMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position master.
   * @return the value of the property
   */
  public PositionMaster getPositionMaster() {
    return _positionMaster;
  }

  /**
   * Sets the position master.
   * @param positionMaster  the new value of the property
   */
  public void setPositionMaster(PositionMaster positionMaster) {
    this._positionMaster = positionMaster;
  }

  /**
   * Gets the the {@code positionMaster} property.
   * @return the property, not null
   */
  public final Property<PositionMaster> positionMaster() {
    return metaBean().positionMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the portfolio master.
   * @return the value of the property
   */
  public PortfolioMaster getPortfolioMaster() {
    return _portfolioMaster;
  }

  /**
   * Sets the portfolio master.
   * @param portfolioMaster  the new value of the property
   */
  public void setPortfolioMaster(PortfolioMaster portfolioMaster) {
    this._portfolioMaster = portfolioMaster;
  }

  /**
   * Gets the the {@code portfolioMaster} property.
   * @return the property, not null
   */
  public final Property<PortfolioMaster> portfolioMaster() {
    return metaBean().portfolioMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-series master.
   * @return the value of the property
   */
  public HistoricalTimeSeriesMaster getHistoricalTimeSeriesMaster() {
    return _historicalTimeSeriesMaster;
  }

  /**
   * Sets the time-series master.
   * @param historicalTimeSeriesMaster  the new value of the property
   */
  public void setHistoricalTimeSeriesMaster(HistoricalTimeSeriesMaster historicalTimeSeriesMaster) {
    this._historicalTimeSeriesMaster = historicalTimeSeriesMaster;
  }

  /**
   * Gets the the {@code historicalTimeSeriesMaster} property.
   * @return the property, not null
   */
  public final Property<HistoricalTimeSeriesMaster> historicalTimeSeriesMaster() {
    return metaBean().historicalTimeSeriesMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-series source.
   * @return the value of the property
   */
  public HistoricalTimeSeriesSource getHistoricalTimeSeriesSource() {
    return _historicalTimeSeriesSource;
  }

  /**
   * Sets the time-series source.
   * @param historicalTimeSeriesSource  the new value of the property
   */
  public void setHistoricalTimeSeriesSource(HistoricalTimeSeriesSource historicalTimeSeriesSource) {
    this._historicalTimeSeriesSource = historicalTimeSeriesSource;
  }

  /**
   * Gets the the {@code historicalTimeSeriesSource} property.
   * @return the property, not null
   */
  public final Property<HistoricalTimeSeriesSource> historicalTimeSeriesSource() {
    return metaBean().historicalTimeSeriesSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the time-series loader.
   * @return the value of the property
   */
  public HistoricalTimeSeriesLoader getHistoricalTimeSeriesLoader() {
    return _historicalTimeSeriesLoader;
  }

  /**
   * Sets the time-series loader.
   * @param historicalTimeSeriesLoader  the new value of the property
   */
  public void setHistoricalTimeSeriesLoader(HistoricalTimeSeriesLoader historicalTimeSeriesLoader) {
    this._historicalTimeSeriesLoader = historicalTimeSeriesLoader;
  }

  /**
   * Gets the the {@code historicalTimeSeriesLoader} property.
   * @return the property, not null
   */
  public final Property<HistoricalTimeSeriesLoader> historicalTimeSeriesLoader() {
    return metaBean().historicalTimeSeriesLoader().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the scheduler.
   * @return the value of the property
   */
  public ScheduledExecutorService getScheduler() {
    return _scheduler;
  }

  /**
   * Sets the scheduler.
   * @param scheduler  the new value of the property
   */
  public void setScheduler(ScheduledExecutorService scheduler) {
    this._scheduler = scheduler;
  }

  /**
   * Gets the the {@code scheduler} property.
   * @return the property, not null
   */
  public final Property<ScheduledExecutorService> scheduler() {
    return metaBean().scheduler().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the available computation target types.
   * @return the value of the property
   */
  public ComputationTargetTypeProvider getTargetTypes() {
    return _targetTypes;
  }

  /**
   * Sets the available computation target types.
   * @param targetTypes  the new value of the property
   */
  public void setTargetTypes(ComputationTargetTypeProvider targetTypes) {
    this._targetTypes = targetTypes;
  }

  /**
   * Gets the the {@code targetTypes} property.
   * @return the property, not null
   */
  public final Property<ComputationTargetTypeProvider> targetTypes() {
    return metaBean().targetTypes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data snapshot master.
   * @return the value of the property
   */
  public MarketDataSnapshotMaster getMarketDataSnapshotMaster() {
    return _marketDataSnapshotMaster;
  }

  /**
   * Sets the market data snapshot master.
   * @param marketDataSnapshotMaster  the new value of the property
   */
  public void setMarketDataSnapshotMaster(MarketDataSnapshotMaster marketDataSnapshotMaster) {
    this._marketDataSnapshotMaster = marketDataSnapshotMaster;
  }

  /**
   * Gets the the {@code marketDataSnapshotMaster} property.
   * @return the property, not null
   */
  public final Property<MarketDataSnapshotMaster> marketDataSnapshotMaster() {
    return metaBean().marketDataSnapshotMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the function configuration source.
   * @return the value of the property
   */
  public FunctionConfigurationSource getFunctionConfigurationSource() {
    return _functionConfigurationSource;
  }

  /**
   * Sets the function configuration source.
   * @param functionConfigurationSource  the new value of the property
   */
  public void setFunctionConfigurationSource(FunctionConfigurationSource functionConfigurationSource) {
    this._functionConfigurationSource = functionConfigurationSource;
  }

  /**
   * Gets the the {@code functionConfigurationSource} property.
   * @return the property, not null
   */
  public final Property<FunctionConfigurationSource> functionConfigurationSource() {
    return metaBean().functionConfigurationSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the batch master.
   * @return the value of the property
   */
  public BatchMaster getBatchMaster() {
    return _batchMaster;
  }

  /**
   * Sets the batch master.
   * @param batchMaster  the new value of the property
   */
  public void setBatchMaster(BatchMaster batchMaster) {
    this._batchMaster = batchMaster;
  }

  /**
   * Gets the the {@code batchMaster} property.
   * @return the property, not null
   */
  public final Property<BatchMaster> batchMaster() {
    return metaBean().batchMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets for obtaining the live market data provider names.
   * @return the value of the property
   */
  public LiveMarketDataProviderFactory getLiveMarketDataProviderFactory() {
    return _liveMarketDataProviderFactory;
  }

  /**
   * Sets for obtaining the live market data provider names.
   * @param liveMarketDataProviderFactory  the new value of the property
   */
  public void setLiveMarketDataProviderFactory(LiveMarketDataProviderFactory liveMarketDataProviderFactory) {
    this._liveMarketDataProviderFactory = liveMarketDataProviderFactory;
  }

  /**
   * Gets the the {@code liveMarketDataProviderFactory} property.
   * @return the property, not null
   */
  public final Property<LiveMarketDataProviderFactory> liveMarketDataProviderFactory() {
    return metaBean().liveMarketDataProviderFactory().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the view processor.
   * @return the value of the property
   */
  public ViewProcessor getViewProcessor() {
    return _viewProcessor;
  }

  /**
   * Sets the view processor.
   * @param viewProcessor  the new value of the property
   */
  public void setViewProcessor(ViewProcessor viewProcessor) {
    this._viewProcessor = viewProcessor;
  }

  /**
   * Gets the the {@code viewProcessor} property.
   * @return the property, not null
   */
  public final Property<ViewProcessor> viewProcessor() {
    return metaBean().viewProcessor().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the computation target resolver.
   * @return the value of the property
   */
  public ComputationTargetResolver getComputationTargetResolver() {
    return _computationTargetResolver;
  }

  /**
   * Sets the computation target resolver.
   * @param computationTargetResolver  the new value of the property
   */
  public void setComputationTargetResolver(ComputationTargetResolver computationTargetResolver) {
    this._computationTargetResolver = computationTargetResolver;
  }

  /**
   * Gets the the {@code computationTargetResolver} property.
   * @return the property, not null
   */
  public final Property<ComputationTargetResolver> computationTargetResolver() {
    return metaBean().computationTargetResolver().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the external Scheme configuration e.g BLOOMBERG_TICKER:Bloomberg Ticker,BLOOMBERG_TCM:Bloomberg Ticker/Coupon/Maturity.
   * @return the value of the property
   */
  public String getExternalSchemes() {
    return _externalSchemes;
  }

  /**
   * Sets the external Scheme configuration e.g BLOOMBERG_TICKER:Bloomberg Ticker,BLOOMBERG_TCM:Bloomberg Ticker/Coupon/Maturity.
   * @param externalSchemes  the new value of the property
   */
  public void setExternalSchemes(String externalSchemes) {
    this._externalSchemes = externalSchemes;
  }

  /**
   * Gets the the {@code externalSchemes} property.
   * @return the property, not null
   */
  public final Property<String> externalSchemes() {
    return metaBean().externalSchemes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MinimalWebsiteBasicsComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MinimalWebsiteBasicsComponentFactory other = (MinimalWebsiteBasicsComponentFactory) obj;
      return JodaBeanUtils.equal(getConfigMaster(), other.getConfigMaster()) &&
          JodaBeanUtils.equal(getUserMaster(), other.getUserMaster()) &&
          JodaBeanUtils.equal(getPasswordService(), other.getPasswordService()) &&
          JodaBeanUtils.equal(getExchangeMaster(), other.getExchangeMaster()) &&
          JodaBeanUtils.equal(getHolidayMaster(), other.getHolidayMaster()) &&
          JodaBeanUtils.equal(getRegionMaster(), other.getRegionMaster()) &&
          JodaBeanUtils.equal(getSecurityMaster(), other.getSecurityMaster()) &&
          JodaBeanUtils.equal(getSecuritySource(), other.getSecuritySource()) &&
          JodaBeanUtils.equal(getSecurityLoader(), other.getSecurityLoader()) &&
          JodaBeanUtils.equal(getConventionMaster(), other.getConventionMaster()) &&
          JodaBeanUtils.equal(getLegalEntityMaster(), other.getLegalEntityMaster()) &&
          JodaBeanUtils.equal(getPositionMaster(), other.getPositionMaster()) &&
          JodaBeanUtils.equal(getPortfolioMaster(), other.getPortfolioMaster()) &&
          JodaBeanUtils.equal(getHistoricalTimeSeriesMaster(), other.getHistoricalTimeSeriesMaster()) &&
          JodaBeanUtils.equal(getHistoricalTimeSeriesSource(), other.getHistoricalTimeSeriesSource()) &&
          JodaBeanUtils.equal(getHistoricalTimeSeriesLoader(), other.getHistoricalTimeSeriesLoader()) &&
          JodaBeanUtils.equal(getScheduler(), other.getScheduler()) &&
          JodaBeanUtils.equal(getTargetTypes(), other.getTargetTypes()) &&
          JodaBeanUtils.equal(getMarketDataSnapshotMaster(), other.getMarketDataSnapshotMaster()) &&
          JodaBeanUtils.equal(getFunctionConfigurationSource(), other.getFunctionConfigurationSource()) &&
          JodaBeanUtils.equal(getBatchMaster(), other.getBatchMaster()) &&
          JodaBeanUtils.equal(getLiveMarketDataProviderFactory(), other.getLiveMarketDataProviderFactory()) &&
          JodaBeanUtils.equal(getViewProcessor(), other.getViewProcessor()) &&
          JodaBeanUtils.equal(getComputationTargetResolver(), other.getComputationTargetResolver()) &&
          JodaBeanUtils.equal(getExternalSchemes(), other.getExternalSchemes()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getConfigMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUserMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPasswordService());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExchangeMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHolidayMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRegionMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecuritySource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSecurityLoader());
    hash = hash * 31 + JodaBeanUtils.hashCode(getConventionMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLegalEntityMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPositionMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPortfolioMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHistoricalTimeSeriesMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHistoricalTimeSeriesSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getHistoricalTimeSeriesLoader());
    hash = hash * 31 + JodaBeanUtils.hashCode(getScheduler());
    hash = hash * 31 + JodaBeanUtils.hashCode(getTargetTypes());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMarketDataSnapshotMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFunctionConfigurationSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getBatchMaster());
    hash = hash * 31 + JodaBeanUtils.hashCode(getLiveMarketDataProviderFactory());
    hash = hash * 31 + JodaBeanUtils.hashCode(getViewProcessor());
    hash = hash * 31 + JodaBeanUtils.hashCode(getComputationTargetResolver());
    hash = hash * 31 + JodaBeanUtils.hashCode(getExternalSchemes());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(832);
    buf.append("MinimalWebsiteBasicsComponentFactory{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("configMaster").append('=').append(JodaBeanUtils.toString(getConfigMaster())).append(',').append(' ');
    buf.append("userMaster").append('=').append(JodaBeanUtils.toString(getUserMaster())).append(',').append(' ');
    buf.append("passwordService").append('=').append(JodaBeanUtils.toString(getPasswordService())).append(',').append(' ');
    buf.append("exchangeMaster").append('=').append(JodaBeanUtils.toString(getExchangeMaster())).append(',').append(' ');
    buf.append("holidayMaster").append('=').append(JodaBeanUtils.toString(getHolidayMaster())).append(',').append(' ');
    buf.append("regionMaster").append('=').append(JodaBeanUtils.toString(getRegionMaster())).append(',').append(' ');
    buf.append("securityMaster").append('=').append(JodaBeanUtils.toString(getSecurityMaster())).append(',').append(' ');
    buf.append("securitySource").append('=').append(JodaBeanUtils.toString(getSecuritySource())).append(',').append(' ');
    buf.append("securityLoader").append('=').append(JodaBeanUtils.toString(getSecurityLoader())).append(',').append(' ');
    buf.append("conventionMaster").append('=').append(JodaBeanUtils.toString(getConventionMaster())).append(',').append(' ');
    buf.append("legalEntityMaster").append('=').append(JodaBeanUtils.toString(getLegalEntityMaster())).append(',').append(' ');
    buf.append("positionMaster").append('=').append(JodaBeanUtils.toString(getPositionMaster())).append(',').append(' ');
    buf.append("portfolioMaster").append('=').append(JodaBeanUtils.toString(getPortfolioMaster())).append(',').append(' ');
    buf.append("historicalTimeSeriesMaster").append('=').append(JodaBeanUtils.toString(getHistoricalTimeSeriesMaster())).append(',').append(' ');
    buf.append("historicalTimeSeriesSource").append('=').append(JodaBeanUtils.toString(getHistoricalTimeSeriesSource())).append(',').append(' ');
    buf.append("historicalTimeSeriesLoader").append('=').append(JodaBeanUtils.toString(getHistoricalTimeSeriesLoader())).append(',').append(' ');
    buf.append("scheduler").append('=').append(JodaBeanUtils.toString(getScheduler())).append(',').append(' ');
    buf.append("targetTypes").append('=').append(JodaBeanUtils.toString(getTargetTypes())).append(',').append(' ');
    buf.append("marketDataSnapshotMaster").append('=').append(JodaBeanUtils.toString(getMarketDataSnapshotMaster())).append(',').append(' ');
    buf.append("functionConfigurationSource").append('=').append(JodaBeanUtils.toString(getFunctionConfigurationSource())).append(',').append(' ');
    buf.append("batchMaster").append('=').append(JodaBeanUtils.toString(getBatchMaster())).append(',').append(' ');
    buf.append("liveMarketDataProviderFactory").append('=').append(JodaBeanUtils.toString(getLiveMarketDataProviderFactory())).append(',').append(' ');
    buf.append("viewProcessor").append('=').append(JodaBeanUtils.toString(getViewProcessor())).append(',').append(' ');
    buf.append("computationTargetResolver").append('=').append(JodaBeanUtils.toString(getComputationTargetResolver())).append(',').append(' ');
    buf.append("externalSchemes").append('=').append(JodaBeanUtils.toString(getExternalSchemes())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MinimalWebsiteBasicsComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code configMaster} property.
     */
    private final MetaProperty<ConfigMaster> _configMaster = DirectMetaProperty.ofReadWrite(
        this, "configMaster", MinimalWebsiteBasicsComponentFactory.class, ConfigMaster.class);
    /**
     * The meta-property for the {@code userMaster} property.
     */
    private final MetaProperty<UserMaster> _userMaster = DirectMetaProperty.ofReadWrite(
        this, "userMaster", MinimalWebsiteBasicsComponentFactory.class, UserMaster.class);
    /**
     * The meta-property for the {@code passwordService} property.
     */
    private final MetaProperty<PasswordService> _passwordService = DirectMetaProperty.ofReadWrite(
        this, "passwordService", MinimalWebsiteBasicsComponentFactory.class, PasswordService.class);
    /**
     * The meta-property for the {@code exchangeMaster} property.
     */
    private final MetaProperty<ExchangeMaster> _exchangeMaster = DirectMetaProperty.ofReadWrite(
        this, "exchangeMaster", MinimalWebsiteBasicsComponentFactory.class, ExchangeMaster.class);
    /**
     * The meta-property for the {@code holidayMaster} property.
     */
    private final MetaProperty<HolidayMaster> _holidayMaster = DirectMetaProperty.ofReadWrite(
        this, "holidayMaster", MinimalWebsiteBasicsComponentFactory.class, HolidayMaster.class);
    /**
     * The meta-property for the {@code regionMaster} property.
     */
    private final MetaProperty<RegionMaster> _regionMaster = DirectMetaProperty.ofReadWrite(
        this, "regionMaster", MinimalWebsiteBasicsComponentFactory.class, RegionMaster.class);
    /**
     * The meta-property for the {@code securityMaster} property.
     */
    private final MetaProperty<SecurityMaster> _securityMaster = DirectMetaProperty.ofReadWrite(
        this, "securityMaster", MinimalWebsiteBasicsComponentFactory.class, SecurityMaster.class);
    /**
     * The meta-property for the {@code securitySource} property.
     */
    private final MetaProperty<SecuritySource> _securitySource = DirectMetaProperty.ofReadWrite(
        this, "securitySource", MinimalWebsiteBasicsComponentFactory.class, SecuritySource.class);
    /**
     * The meta-property for the {@code securityLoader} property.
     */
    private final MetaProperty<SecurityLoader> _securityLoader = DirectMetaProperty.ofReadWrite(
        this, "securityLoader", MinimalWebsiteBasicsComponentFactory.class, SecurityLoader.class);
    /**
     * The meta-property for the {@code conventionMaster} property.
     */
    private final MetaProperty<ConventionMaster> _conventionMaster = DirectMetaProperty.ofReadWrite(
        this, "conventionMaster", MinimalWebsiteBasicsComponentFactory.class, ConventionMaster.class);
    /**
     * The meta-property for the {@code legalEntityMaster} property.
     */
    private final MetaProperty<LegalEntityMaster> _legalEntityMaster = DirectMetaProperty.ofReadWrite(
        this, "legalEntityMaster", MinimalWebsiteBasicsComponentFactory.class, LegalEntityMaster.class);
    /**
     * The meta-property for the {@code positionMaster} property.
     */
    private final MetaProperty<PositionMaster> _positionMaster = DirectMetaProperty.ofReadWrite(
        this, "positionMaster", MinimalWebsiteBasicsComponentFactory.class, PositionMaster.class);
    /**
     * The meta-property for the {@code portfolioMaster} property.
     */
    private final MetaProperty<PortfolioMaster> _portfolioMaster = DirectMetaProperty.ofReadWrite(
        this, "portfolioMaster", MinimalWebsiteBasicsComponentFactory.class, PortfolioMaster.class);
    /**
     * The meta-property for the {@code historicalTimeSeriesMaster} property.
     */
    private final MetaProperty<HistoricalTimeSeriesMaster> _historicalTimeSeriesMaster = DirectMetaProperty.ofReadWrite(
        this, "historicalTimeSeriesMaster", MinimalWebsiteBasicsComponentFactory.class, HistoricalTimeSeriesMaster.class);
    /**
     * The meta-property for the {@code historicalTimeSeriesSource} property.
     */
    private final MetaProperty<HistoricalTimeSeriesSource> _historicalTimeSeriesSource = DirectMetaProperty.ofReadWrite(
        this, "historicalTimeSeriesSource", MinimalWebsiteBasicsComponentFactory.class, HistoricalTimeSeriesSource.class);
    /**
     * The meta-property for the {@code historicalTimeSeriesLoader} property.
     */
    private final MetaProperty<HistoricalTimeSeriesLoader> _historicalTimeSeriesLoader = DirectMetaProperty.ofReadWrite(
        this, "historicalTimeSeriesLoader", MinimalWebsiteBasicsComponentFactory.class, HistoricalTimeSeriesLoader.class);
    /**
     * The meta-property for the {@code scheduler} property.
     */
    private final MetaProperty<ScheduledExecutorService> _scheduler = DirectMetaProperty.ofReadWrite(
        this, "scheduler", MinimalWebsiteBasicsComponentFactory.class, ScheduledExecutorService.class);
    /**
     * The meta-property for the {@code targetTypes} property.
     */
    private final MetaProperty<ComputationTargetTypeProvider> _targetTypes = DirectMetaProperty.ofReadWrite(
        this, "targetTypes", MinimalWebsiteBasicsComponentFactory.class, ComputationTargetTypeProvider.class);
    /**
     * The meta-property for the {@code marketDataSnapshotMaster} property.
     */
    private final MetaProperty<MarketDataSnapshotMaster> _marketDataSnapshotMaster = DirectMetaProperty.ofReadWrite(
        this, "marketDataSnapshotMaster", MinimalWebsiteBasicsComponentFactory.class, MarketDataSnapshotMaster.class);
    /**
     * The meta-property for the {@code functionConfigurationSource} property.
     */
    private final MetaProperty<FunctionConfigurationSource> _functionConfigurationSource = DirectMetaProperty.ofReadWrite(
        this, "functionConfigurationSource", MinimalWebsiteBasicsComponentFactory.class, FunctionConfigurationSource.class);
    /**
     * The meta-property for the {@code batchMaster} property.
     */
    private final MetaProperty<BatchMaster> _batchMaster = DirectMetaProperty.ofReadWrite(
        this, "batchMaster", MinimalWebsiteBasicsComponentFactory.class, BatchMaster.class);
    /**
     * The meta-property for the {@code liveMarketDataProviderFactory} property.
     */
    private final MetaProperty<LiveMarketDataProviderFactory> _liveMarketDataProviderFactory = DirectMetaProperty.ofReadWrite(
        this, "liveMarketDataProviderFactory", MinimalWebsiteBasicsComponentFactory.class, LiveMarketDataProviderFactory.class);
    /**
     * The meta-property for the {@code viewProcessor} property.
     */
    private final MetaProperty<ViewProcessor> _viewProcessor = DirectMetaProperty.ofReadWrite(
        this, "viewProcessor", MinimalWebsiteBasicsComponentFactory.class, ViewProcessor.class);
    /**
     * The meta-property for the {@code computationTargetResolver} property.
     */
    private final MetaProperty<ComputationTargetResolver> _computationTargetResolver = DirectMetaProperty.ofReadWrite(
        this, "computationTargetResolver", MinimalWebsiteBasicsComponentFactory.class, ComputationTargetResolver.class);
    /**
     * The meta-property for the {@code externalSchemes} property.
     */
    private final MetaProperty<String> _externalSchemes = DirectMetaProperty.ofReadWrite(
        this, "externalSchemes", MinimalWebsiteBasicsComponentFactory.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "configMaster",
        "userMaster",
        "passwordService",
        "exchangeMaster",
        "holidayMaster",
        "regionMaster",
        "securityMaster",
        "securitySource",
        "securityLoader",
        "conventionMaster",
        "legalEntityMaster",
        "positionMaster",
        "portfolioMaster",
        "historicalTimeSeriesMaster",
        "historicalTimeSeriesSource",
        "historicalTimeSeriesLoader",
        "scheduler",
        "targetTypes",
        "marketDataSnapshotMaster",
        "functionConfigurationSource",
        "batchMaster",
        "liveMarketDataProviderFactory",
        "viewProcessor",
        "computationTargetResolver",
        "externalSchemes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 10395716:  // configMaster
          return _configMaster;
        case 1402846733:  // userMaster
          return _userMaster;
        case 348360602:  // passwordService
          return _passwordService;
        case -652001691:  // exchangeMaster
          return _exchangeMaster;
        case 246258906:  // holidayMaster
          return _holidayMaster;
        case -1820969354:  // regionMaster
          return _regionMaster;
        case -887218750:  // securityMaster
          return _securityMaster;
        case -702456965:  // securitySource
          return _securitySource;
        case -903470221:  // securityLoader
          return _securityLoader;
        case 41113907:  // conventionMaster
          return _conventionMaster;
        case -1944474242:  // legalEntityMaster
          return _legalEntityMaster;
        case -1840419605:  // positionMaster
          return _positionMaster;
        case -772274742:  // portfolioMaster
          return _portfolioMaster;
        case 173967376:  // historicalTimeSeriesMaster
          return _historicalTimeSeriesMaster;
        case 358729161:  // historicalTimeSeriesSource
          return _historicalTimeSeriesSource;
        case 157715905:  // historicalTimeSeriesLoader
          return _historicalTimeSeriesLoader;
        case -160710469:  // scheduler
          return _scheduler;
        case -2094577304:  // targetTypes
          return _targetTypes;
        case 2090650860:  // marketDataSnapshotMaster
          return _marketDataSnapshotMaster;
        case -1059254855:  // functionConfigurationSource
          return _functionConfigurationSource;
        case -252634564:  // batchMaster
          return _batchMaster;
        case -301472921:  // liveMarketDataProviderFactory
          return _liveMarketDataProviderFactory;
        case -1697555603:  // viewProcessor
          return _viewProcessor;
        case 1562222174:  // computationTargetResolver
          return _computationTargetResolver;
        case -1949439709:  // externalSchemes
          return _externalSchemes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MinimalWebsiteBasicsComponentFactory> builder() {
      return new DirectBeanBuilder<MinimalWebsiteBasicsComponentFactory>(new MinimalWebsiteBasicsComponentFactory());
    }

    @Override
    public Class<? extends MinimalWebsiteBasicsComponentFactory> beanType() {
      return MinimalWebsiteBasicsComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code configMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConfigMaster> configMaster() {
      return _configMaster;
    }

    /**
     * The meta-property for the {@code userMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UserMaster> userMaster() {
      return _userMaster;
    }

    /**
     * The meta-property for the {@code passwordService} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PasswordService> passwordService() {
      return _passwordService;
    }

    /**
     * The meta-property for the {@code exchangeMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExchangeMaster> exchangeMaster() {
      return _exchangeMaster;
    }

    /**
     * The meta-property for the {@code holidayMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HolidayMaster> holidayMaster() {
      return _holidayMaster;
    }

    /**
     * The meta-property for the {@code regionMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<RegionMaster> regionMaster() {
      return _regionMaster;
    }

    /**
     * The meta-property for the {@code securityMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityMaster> securityMaster() {
      return _securityMaster;
    }

    /**
     * The meta-property for the {@code securitySource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecuritySource> securitySource() {
      return _securitySource;
    }

    /**
     * The meta-property for the {@code securityLoader} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SecurityLoader> securityLoader() {
      return _securityLoader;
    }

    /**
     * The meta-property for the {@code conventionMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ConventionMaster> conventionMaster() {
      return _conventionMaster;
    }

    /**
     * The meta-property for the {@code legalEntityMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LegalEntityMaster> legalEntityMaster() {
      return _legalEntityMaster;
    }

    /**
     * The meta-property for the {@code positionMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PositionMaster> positionMaster() {
      return _positionMaster;
    }

    /**
     * The meta-property for the {@code portfolioMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PortfolioMaster> portfolioMaster() {
      return _portfolioMaster;
    }

    /**
     * The meta-property for the {@code historicalTimeSeriesMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HistoricalTimeSeriesMaster> historicalTimeSeriesMaster() {
      return _historicalTimeSeriesMaster;
    }

    /**
     * The meta-property for the {@code historicalTimeSeriesSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HistoricalTimeSeriesSource> historicalTimeSeriesSource() {
      return _historicalTimeSeriesSource;
    }

    /**
     * The meta-property for the {@code historicalTimeSeriesLoader} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<HistoricalTimeSeriesLoader> historicalTimeSeriesLoader() {
      return _historicalTimeSeriesLoader;
    }

    /**
     * The meta-property for the {@code scheduler} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ScheduledExecutorService> scheduler() {
      return _scheduler;
    }

    /**
     * The meta-property for the {@code targetTypes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ComputationTargetTypeProvider> targetTypes() {
      return _targetTypes;
    }

    /**
     * The meta-property for the {@code marketDataSnapshotMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MarketDataSnapshotMaster> marketDataSnapshotMaster() {
      return _marketDataSnapshotMaster;
    }

    /**
     * The meta-property for the {@code functionConfigurationSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<FunctionConfigurationSource> functionConfigurationSource() {
      return _functionConfigurationSource;
    }

    /**
     * The meta-property for the {@code batchMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BatchMaster> batchMaster() {
      return _batchMaster;
    }

    /**
     * The meta-property for the {@code liveMarketDataProviderFactory} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LiveMarketDataProviderFactory> liveMarketDataProviderFactory() {
      return _liveMarketDataProviderFactory;
    }

    /**
     * The meta-property for the {@code viewProcessor} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ViewProcessor> viewProcessor() {
      return _viewProcessor;
    }

    /**
     * The meta-property for the {@code computationTargetResolver} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ComputationTargetResolver> computationTargetResolver() {
      return _computationTargetResolver;
    }

    /**
     * The meta-property for the {@code externalSchemes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> externalSchemes() {
      return _externalSchemes;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 10395716:  // configMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getConfigMaster();
        case 1402846733:  // userMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getUserMaster();
        case 348360602:  // passwordService
          return ((MinimalWebsiteBasicsComponentFactory) bean).getPasswordService();
        case -652001691:  // exchangeMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getExchangeMaster();
        case 246258906:  // holidayMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getHolidayMaster();
        case -1820969354:  // regionMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getRegionMaster();
        case -887218750:  // securityMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getSecurityMaster();
        case -702456965:  // securitySource
          return ((MinimalWebsiteBasicsComponentFactory) bean).getSecuritySource();
        case -903470221:  // securityLoader
          return ((MinimalWebsiteBasicsComponentFactory) bean).getSecurityLoader();
        case 41113907:  // conventionMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getConventionMaster();
        case -1944474242:  // legalEntityMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getLegalEntityMaster();
        case -1840419605:  // positionMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getPositionMaster();
        case -772274742:  // portfolioMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getPortfolioMaster();
        case 173967376:  // historicalTimeSeriesMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getHistoricalTimeSeriesMaster();
        case 358729161:  // historicalTimeSeriesSource
          return ((MinimalWebsiteBasicsComponentFactory) bean).getHistoricalTimeSeriesSource();
        case 157715905:  // historicalTimeSeriesLoader
          return ((MinimalWebsiteBasicsComponentFactory) bean).getHistoricalTimeSeriesLoader();
        case -160710469:  // scheduler
          return ((MinimalWebsiteBasicsComponentFactory) bean).getScheduler();
        case -2094577304:  // targetTypes
          return ((MinimalWebsiteBasicsComponentFactory) bean).getTargetTypes();
        case 2090650860:  // marketDataSnapshotMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getMarketDataSnapshotMaster();
        case -1059254855:  // functionConfigurationSource
          return ((MinimalWebsiteBasicsComponentFactory) bean).getFunctionConfigurationSource();
        case -252634564:  // batchMaster
          return ((MinimalWebsiteBasicsComponentFactory) bean).getBatchMaster();
        case -301472921:  // liveMarketDataProviderFactory
          return ((MinimalWebsiteBasicsComponentFactory) bean).getLiveMarketDataProviderFactory();
        case -1697555603:  // viewProcessor
          return ((MinimalWebsiteBasicsComponentFactory) bean).getViewProcessor();
        case 1562222174:  // computationTargetResolver
          return ((MinimalWebsiteBasicsComponentFactory) bean).getComputationTargetResolver();
        case -1949439709:  // externalSchemes
          return ((MinimalWebsiteBasicsComponentFactory) bean).getExternalSchemes();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 10395716:  // configMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setConfigMaster((ConfigMaster) newValue);
          return;
        case 1402846733:  // userMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setUserMaster((UserMaster) newValue);
          return;
        case 348360602:  // passwordService
          ((MinimalWebsiteBasicsComponentFactory) bean).setPasswordService((PasswordService) newValue);
          return;
        case -652001691:  // exchangeMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setExchangeMaster((ExchangeMaster) newValue);
          return;
        case 246258906:  // holidayMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setHolidayMaster((HolidayMaster) newValue);
          return;
        case -1820969354:  // regionMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setRegionMaster((RegionMaster) newValue);
          return;
        case -887218750:  // securityMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setSecurityMaster((SecurityMaster) newValue);
          return;
        case -702456965:  // securitySource
          ((MinimalWebsiteBasicsComponentFactory) bean).setSecuritySource((SecuritySource) newValue);
          return;
        case -903470221:  // securityLoader
          ((MinimalWebsiteBasicsComponentFactory) bean).setSecurityLoader((SecurityLoader) newValue);
          return;
        case 41113907:  // conventionMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setConventionMaster((ConventionMaster) newValue);
          return;
        case -1944474242:  // legalEntityMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setLegalEntityMaster((LegalEntityMaster) newValue);
          return;
        case -1840419605:  // positionMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setPositionMaster((PositionMaster) newValue);
          return;
        case -772274742:  // portfolioMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setPortfolioMaster((PortfolioMaster) newValue);
          return;
        case 173967376:  // historicalTimeSeriesMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setHistoricalTimeSeriesMaster((HistoricalTimeSeriesMaster) newValue);
          return;
        case 358729161:  // historicalTimeSeriesSource
          ((MinimalWebsiteBasicsComponentFactory) bean).setHistoricalTimeSeriesSource((HistoricalTimeSeriesSource) newValue);
          return;
        case 157715905:  // historicalTimeSeriesLoader
          ((MinimalWebsiteBasicsComponentFactory) bean).setHistoricalTimeSeriesLoader((HistoricalTimeSeriesLoader) newValue);
          return;
        case -160710469:  // scheduler
          ((MinimalWebsiteBasicsComponentFactory) bean).setScheduler((ScheduledExecutorService) newValue);
          return;
        case -2094577304:  // targetTypes
          ((MinimalWebsiteBasicsComponentFactory) bean).setTargetTypes((ComputationTargetTypeProvider) newValue);
          return;
        case 2090650860:  // marketDataSnapshotMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setMarketDataSnapshotMaster((MarketDataSnapshotMaster) newValue);
          return;
        case -1059254855:  // functionConfigurationSource
          ((MinimalWebsiteBasicsComponentFactory) bean).setFunctionConfigurationSource((FunctionConfigurationSource) newValue);
          return;
        case -252634564:  // batchMaster
          ((MinimalWebsiteBasicsComponentFactory) bean).setBatchMaster((BatchMaster) newValue);
          return;
        case -301472921:  // liveMarketDataProviderFactory
          ((MinimalWebsiteBasicsComponentFactory) bean).setLiveMarketDataProviderFactory((LiveMarketDataProviderFactory) newValue);
          return;
        case -1697555603:  // viewProcessor
          ((MinimalWebsiteBasicsComponentFactory) bean).setViewProcessor((ViewProcessor) newValue);
          return;
        case 1562222174:  // computationTargetResolver
          ((MinimalWebsiteBasicsComponentFactory) bean).setComputationTargetResolver((ComputationTargetResolver) newValue);
          return;
        case -1949439709:  // externalSchemes
          ((MinimalWebsiteBasicsComponentFactory) bean).setExternalSchemes((String) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((MinimalWebsiteBasicsComponentFactory) bean)._configMaster, "configMaster");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
