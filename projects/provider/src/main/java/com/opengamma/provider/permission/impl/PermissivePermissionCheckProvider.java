/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.provider.permission.impl;

import java.util.HashMap;
import java.util.Map;

import com.opengamma.provider.permission.PermissionCheckProviderRequest;
import com.opengamma.provider.permission.PermissionCheckProviderResult;
import com.opengamma.util.ArgumentChecker;

/**
 * Permission checker provider that returns true for all requests.
 */
public final class PermissivePermissionCheckProvider extends AbstractPermissionCheckProvider {

  @Override
  public PermissionCheckProviderResult isPermitted(final PermissionCheckProviderRequest request) {
    ArgumentChecker.notNull(request, "request");
    final Map<String, Boolean> result = new HashMap<>(request.getRequestedPermissions().size());
    for (final String permission : request.getRequestedPermissions()) {
      result.put(permission, true);
    }
    return PermissionCheckProviderResult.of(result);
  }

}
