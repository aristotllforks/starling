/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.config.Ini;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.mgt.WebSecurityManager;

import com.opengamma.util.auth.AuthUtils;

/**
 * Servlet context listener that sets up the web environment.
 * <p>
 * This extends Apache Shiro to force it to use the shared {@code SecurityUtils} manager.
 */
public final class ShiroSecurityEnvironmentLoader extends EnvironmentLoaderListener {

  @Override
  protected WebEnvironment createEnvironment(final ServletContext servletContext) {
    final ShiroWebEnvironment environment = new ShiroWebEnvironment();
    environment.setServletContext(servletContext);
    final String configLocations = StringUtils.trimToNull(servletContext.getInitParameter(CONFIG_LOCATIONS_PARAM));
    if (configLocations != null) {
      environment.setConfigLocations(configLocations);
    }
    environment.init();
    return environment;
  }

  //-------------------------------------------------------------------------
  /**
   * Apache Shiro web environment that re-uses the static manager.
   */
  private final class ShiroWebEnvironment extends IniWebEnvironment {
    @Override
    protected WebSecurityManager createWebSecurityManager() {
      final ShiroFactory factory = new ShiroFactory();
      final Ini ini = getIni();
      if (AuthUtils.isPermissive()) {
        ini.addSection("main").put("shiro.enabled", "false");
      }
      factory.setIni(ini);
      final WebSecurityManager wsm = (WebSecurityManager) factory.getInstance();
      final Map<String, ?> beans = factory.getBeans();
      if (!CollectionUtils.isEmpty(beans)) {
        this.objects.putAll(beans);
      }
      this.objects.put(FILTER_CHAIN_RESOLVER_NAME, new IniFilterChainResolverFactory());
      return wsm;
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Apache Shiro factory that re-uses the static manager.
   */
  private class ShiroFactory extends WebIniSecurityManagerFactory {
    @Override
    protected SecurityManager createDefaultInstance() {
      try {
        final SecurityManager sm = AuthUtils.getSecurityManager();
        if (sm instanceof WebSecurityManager) {
          return sm;
        }
        return super.createDefaultInstance();

      } catch (final UnavailableSecurityManagerException ex) {
        return super.createDefaultInstance();
      }
    }
  }

}
