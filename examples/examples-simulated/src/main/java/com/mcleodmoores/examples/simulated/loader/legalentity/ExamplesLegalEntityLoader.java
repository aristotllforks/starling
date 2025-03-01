/**
 * Copyright (C) 2017 - present McLeod Moores Software Limited.  All rights reserved.
 */
package com.mcleodmoores.examples.simulated.loader.legalentity;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.component.tool.AbstractTool;
import com.opengamma.core.legalentity.Rating;
import com.opengamma.core.obligor.CreditRating;
import com.opengamma.financial.tool.ToolContext;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.master.legalentity.LegalEntityDocument;
import com.opengamma.master.legalentity.LegalEntityMaster;
import com.opengamma.master.legalentity.LegalEntitySearchRequest;
import com.opengamma.master.legalentity.LegalEntitySearchResult;
import com.opengamma.master.legalentity.ManageableLegalEntity;
import com.opengamma.masterdb.legalentity.DbLegalEntityBeanMaster;
import com.opengamma.scripts.Scriptable;
import com.opengamma.util.ArgumentChecker;

/**
 * Simple implementation of a legal entity loader.
 */
@Scriptable
public class ExamplesLegalEntityLoader extends AbstractTool<ToolContext> {
  /** The logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(ExamplesLegalEntityLoader.class);

  /**
   * Main method to run this loader.
   *
   * @param args
   *          The program arguments
   */
  public static void main(final String[] args) {
    new ExamplesLegalEntityLoader().invokeAndTerminate(args);
  }

  @Override
  protected void doRun() {
    addGovernments();
  }

  private void addGovernments() {
    final ManageableLegalEntity usGovernment = new ManageableLegalEntity("US Government",
        ExternalIdBundle.of(DbLegalEntityBeanMaster.IDENTIFIER_SCHEME_DEFAULT, "USGVT"));
    usGovernment.setRatings(Arrays.asList(new Rating("Fitch", CreditRating.AAA, null)));
    storeLegalEntity(getToolContext().getLegalEntityMaster(), usGovernment);
  }

  /**
   * Stores a legal entity in the legal entity database. If the entity is already present, updates it. Otherwise, adds a new entry.
   *
   * @param master
   *          the master in which to store the legal entity
   * @param entity
   *          The legal entity
   */
  public static void storeLegalEntity(final LegalEntityMaster master, final ManageableLegalEntity entity) {
    ArgumentChecker.notNull(master, "master");
    final LegalEntitySearchRequest request = new LegalEntitySearchRequest();
    request.setName(entity.getName());
    final LegalEntitySearchResult result = master.search(request);
    if (result.getFirstDocument() != null) {
      LOGGER.info("Updating {}", entity.getName());
      final LegalEntityDocument document = result.getFirstDocument();
      document.setLegalEntity(entity);
      master.update(document);
    } else {
      LOGGER.info("Adding {}", entity.getName());
      master.add(new LegalEntityDocument(entity));
    }
  }
}
