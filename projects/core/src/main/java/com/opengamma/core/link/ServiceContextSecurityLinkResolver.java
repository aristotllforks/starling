/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.link;

import com.opengamma.DataNotFoundException;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.id.VersionCorrection;
import com.opengamma.service.ServiceContext;
import com.opengamma.service.VersionCorrectionProvider;

/**
 * Private link resolver to resolve links using a ServiceContext.
 *
 * @param <S> the type of security object to be resolved
 */
/* package */ final class ServiceContextSecurityLinkResolver<S extends Security>
    extends SourceLinkResolver<ExternalIdBundle, S, SecuritySource> {

  /**
   * Creates the resolver using the default service context.
   */
  /* package */ ServiceContextSecurityLinkResolver() {
    super();
  }

  /**
   * Creates the resolver using the supplied service context.
   *
   * @param serviceContext  the service context
   */
   /* package */ ServiceContextSecurityLinkResolver(final ServiceContext serviceContext) {
    super(serviceContext);
  }

  @Override
  protected Class<SecuritySource> getSourceClass() {
    return SecuritySource.class;
  }

  @Override
  protected VersionCorrection getVersionCorrection(final VersionCorrectionProvider vcProvider) {
    return vcProvider.getPortfolioVersionCorrection();
  }

  @Override
  @SuppressWarnings("unchecked")
  protected S executeQuery(final SecuritySource source,
                           final Class<S> type,
                           final ExternalIdBundle identifier,
                           final VersionCorrection versionCorrection) {
    final S result = (S) source.getSingle(identifier, versionCorrection);
    if (result != null) {
      return result;
    }
    throw new DataNotFoundException("No security found with id bundle: [" + identifier
                                    + "] and versionCorrection: [" + versionCorrection + "]");
  }
}
