/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.config;

import java.util.Comparator;

import com.opengamma.util.PublicSPI;

/**
 * Available sort orders for configs.
 */
@PublicSPI
public enum ConfigSearchSortOrder implements Comparator<ConfigDocument> {
  // this design is simple and perhaps not ideal, but it is effective for most use cases at the moment

  /**
   * Sort by object id ascending.
   */
  OBJECT_ID_ASC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj1.getObjectId().compareTo(obj2.getObjectId());
    }
  },
  /**
   * Sort by object id ascending.
   */
  OBJECT_ID_DESC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj2.getObjectId().compareTo(obj1.getObjectId());
    }
  },
  /**
   * Sort by version from instant ascending.
   */
  VERSION_FROM_INSTANT_ASC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj1.getVersionFromInstant().compareTo(obj2.getVersionFromInstant());
    }
  },
  /**
   * Sort by version from instant descending.
   */
  VERSION_FROM_INSTANT_DESC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj2.getVersionFromInstant().compareTo(obj1.getVersionFromInstant());
    }
  },
  /**
   * Sort by name ascending.
   */
  NAME_ASC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj1.getName().compareTo(obj2.getName());
    }
  },
  /**
   * Sort by name descending.
   */
  NAME_DESC {
    @Override
    public int compare(final ConfigDocument obj1, final ConfigDocument obj2) {
      return obj2.getName().compareTo(obj1.getName());
    }
  };

}
