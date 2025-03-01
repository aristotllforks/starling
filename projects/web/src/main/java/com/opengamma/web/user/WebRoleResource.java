/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.user;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.beans.impl.flexi.FlexiBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.master.user.ManageableRole;
import com.opengamma.master.user.RoleEventHistoryRequest;
import com.opengamma.master.user.RoleEventHistoryResult;
import com.opengamma.master.user.RoleForm;
import com.opengamma.master.user.RoleFormError;
import com.opengamma.master.user.RoleFormException;

/**
 * RESTful resource for a role.
 */
@Path("/roles/name/{roleName}")
public class WebRoleResource extends AbstractWebRoleResource {

  /** Logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(WebRoleResource.class);
  /**
   * The ftl file.
   */
  private static final String ROLE_PAGE = HTML_DIR + "role.ftl";
  /**
   * The ftl file.
   */
  private static final String ROLE_UPDATE_PAGE = HTML_DIR + "role-update.ftl";

  /**
   * Creates the resource.
   * @param parent  the parent resource, not null
   */
  public WebRoleResource(final AbstractWebRoleResource parent) {
    super(parent);
  }

  //-------------------------------------------------------------------------
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getHTML() {
    final FlexiBean out = createRootData();
    return getFreemarker().build(ROLE_PAGE, out);
  }

  //-------------------------------------------------------------------------
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response postHTML(
      @FormParam("rolename") final String roleName,
      @FormParam("description") final String description,
      @FormParam("addroles") final String addRoles,
      @FormParam("removeroles") final String removeRoles,
      @FormParam("addperms") final String addPerms,
      @FormParam("removeperms") final String removePerms,
      @FormParam("addusers") final String addUsers,
      @FormParam("removeusers") final String removeUsers) {
    try {
      final RoleForm form = new RoleForm(data().getRole());
      form.setRoleName(roleName);
      form.setDescription(description);
      form.setAddRoles(addRoles);
      form.setRemoveRoles(removeRoles);
      form.setAddPermissions(addPerms);
      form.setRemovePermissions(removePerms);
      form.setAddUsers(addUsers);
      form.setRemoveUsers(removeUsers);
      final ManageableRole updated = form.update(data().getUserMaster());
      final URI uri = WebRoleResource.uri(data(), updated.getRoleName());
      return Response.seeOther(uri).build();

    } catch (final RoleFormException ex) {
      ex.logUnexpected(LOGGER);
      final FlexiBean out = createRootData();
      out.put("rolename", roleName);
      out.put("description", description);
      out.put("addroles", addRoles);
      out.put("removeroles", removeRoles);
      out.put("addperms", addPerms);
      out.put("removeperms", removePerms);
      out.put("addusers", addUsers);
      out.put("removeusers", removeUsers);
      for (final RoleFormError error : ex.getErrors()) {
        out.put("err_" + error.toLowerCamel(), true);
      }
      return Response.ok(getFreemarker().build(ROLE_UPDATE_PAGE, out)).build();
    }
  }

  //-------------------------------------------------------------------------
  @DELETE
  @Produces(MediaType.TEXT_HTML)
  public Response deleteHTML() {
    final ManageableRole role = data().getRole();
    data().getRoleMaster().removeById(role.getUniqueId().getObjectId());
    final URI uri = WebRoleResource.uri(data());
    return Response.seeOther(uri).build();
  }

  //-------------------------------------------------------------------------
  /**
   * Creates the output root data.
   * @return the output root data, not null
   */
  @Override
  protected FlexiBean createRootData() {
    final FlexiBean out = super.createRootData();
    final ManageableRole role = data().getRole();
    out.put("role", role);
    out.put("deleted", role.getObjectId() == null);
    out.put("description", role.getDescription());
    final RoleEventHistoryRequest historyRequest = new RoleEventHistoryRequest(data().getRole().getRoleName());
    final RoleEventHistoryResult history = data().getRoleMaster().eventHistory(historyRequest);
    out.put("eventHistory", history);
    return out;
  }

  //-------------------------------------------------------------------------
  /**
   * Builds a URI for this resource.
   * @param data  the data, not null
   * @return the URI, not null
   */
  public static URI uri(final WebRoleData data) {
    return uri(data, null);
  }

  /**
   * Builds a URI for this resource.
   * @param data  the data, not null
   * @param overrideRoleName  the override role name, null uses information from data
   * @return the URI, not null
   */
  public static URI uri(final WebRoleData data, final String overrideRoleName) {
    final String roleName = data.getBestRoleUriName(overrideRoleName);
    return data.getUriInfo().getBaseUriBuilder().path(WebRoleResource.class).build(roleName);
  }

}
