/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.user.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.user.UserAccount;
import com.opengamma.core.user.UserAccountStatus;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;
import org.joda.beans.MetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;

/**
 * Simple implementation of {@code UserAccount}.
 * <p>
 * This is the simplest possible implementation of the {@link UserAccount} interface.
 * <p>
 * This class is mutable and not thread-safe.
 * It is intended to primarily be used via the read-only {@code UserAccount} interface.
 */
@BeanDefinition
public class SimpleUserAccount implements Bean, UserAccount, Serializable {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The user name that uniquely identifies the user.
   * This is used with the password to authenticate.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private String _userName;
  /**
   * The hashed version of the user password.
   * May be null or empty, particularly if the user is disabled.
   */
  @PropertyDefinition(overrideGet = true)
  private String _passwordHash;
  /**
   * The account status, determining if the user is allowed to login.
   */
  @PropertyDefinition(overrideGet = true)
  private UserAccountStatus _status = UserAccountStatus.ENABLED;
  /**
   * The bundle of alternate user identifiers.
   * <p>
   * This allows the user identifiers of external systems to be associated with the account
   * Some of these may be unique within the external system, others may be more descriptive.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private ExternalIdBundle _alternateIds = ExternalIdBundle.EMPTY;
  /**
   * The roles that the user belongs to.
   * Roles are used to manage groups of multiple users.
   * This is the combined set of all roles that the user has, expressed as strings.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final Set<String> _roles = new TreeSet<>();
  /**
   * The permissions that the user has.
   * Permissions are used to define access control.
   * This is the combined set of all permissions that the user has, expressed as strings.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final Set<String> _permissions = new TreeSet<>();
  /**
   * The primary email address associated with the account.
   */
  @PropertyDefinition(overrideGet = true)
  private String _emailAddress;
  /**
   * The user profile, containing user settings.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private SimpleUserProfile _profile = new SimpleUserProfile();

  //-------------------------------------------------------------------------
  /**
   * Creates a {@code SimpleUserAccount} from another account.
   *
   * @param accountToCopy  the account to copy, not null
   * @return the new account, not null
   */
  public static SimpleUserAccount from(final UserAccount accountToCopy) {
    ArgumentChecker.notNull(accountToCopy, "profileToCopy");
    final SimpleUserAccount copy = new SimpleUserAccount(accountToCopy.getUserName());
    copy.setPasswordHash(accountToCopy.getPasswordHash());
    copy.setStatus(accountToCopy.getStatus());
    copy.setAlternateIds(accountToCopy.getAlternateIds());
    copy.setEmailAddress(accountToCopy.getEmailAddress());
    copy.setProfile(SimpleUserProfile.from(accountToCopy.getProfile()));
    copy.setRoles(accountToCopy.getRoles());
    copy.setPermissions(accountToCopy.getPermissions());
    return copy;
  }

  //-------------------------------------------------------------------------
  /**
   * Creates a user.
   */
  protected SimpleUserAccount() {
  }

  /**
   * Creates a user.
   *
   * @param userName  the user name, not null
   */
  public SimpleUserAccount(final String userName) {
    setUserName(userName);
  }

  //-------------------------------------------------------------------------
  /**
   * Adds an alternate user identifier to the bundle representing this user.
   *
   * @param alternateId  the identifier to add, not null
   */
  public void addAlternateId(final ExternalId alternateId) {
    setAlternateIds(getAlternateIds().withExternalId(alternateId));
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code SimpleUserAccount}.
   * @return the meta-bean, not null
   */
  public static SimpleUserAccount.Meta meta() {
    return SimpleUserAccount.Meta.INSTANCE;
  }

  static {
    MetaBean.register(SimpleUserAccount.Meta.INSTANCE);
  }

  @Override
  public SimpleUserAccount.Meta metaBean() {
    return SimpleUserAccount.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the user name that uniquely identifies the user.
   * This is used with the password to authenticate.
   * @return the value of the property, not null
   */
  @Override
  public String getUserName() {
    return _userName;
  }

  /**
   * Sets the user name that uniquely identifies the user.
   * This is used with the password to authenticate.
   * @param userName  the new value of the property, not null
   */
  public void setUserName(String userName) {
    JodaBeanUtils.notNull(userName, "userName");
    this._userName = userName;
  }

  /**
   * Gets the the {@code userName} property.
   * This is used with the password to authenticate.
   * @return the property, not null
   */
  public final Property<String> userName() {
    return metaBean().userName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the hashed version of the user password.
   * May be null or empty, particularly if the user is disabled.
   * @return the value of the property
   */
  @Override
  public String getPasswordHash() {
    return _passwordHash;
  }

  /**
   * Sets the hashed version of the user password.
   * May be null or empty, particularly if the user is disabled.
   * @param passwordHash  the new value of the property
   */
  public void setPasswordHash(String passwordHash) {
    this._passwordHash = passwordHash;
  }

  /**
   * Gets the the {@code passwordHash} property.
   * May be null or empty, particularly if the user is disabled.
   * @return the property, not null
   */
  public final Property<String> passwordHash() {
    return metaBean().passwordHash().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the account status, determining if the user is allowed to login.
   * @return the value of the property
   */
  @Override
  public UserAccountStatus getStatus() {
    return _status;
  }

  /**
   * Sets the account status, determining if the user is allowed to login.
   * @param status  the new value of the property
   */
  public void setStatus(UserAccountStatus status) {
    this._status = status;
  }

  /**
   * Gets the the {@code status} property.
   * @return the property, not null
   */
  public final Property<UserAccountStatus> status() {
    return metaBean().status().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the bundle of alternate user identifiers.
   * <p>
   * This allows the user identifiers of external systems to be associated with the account
   * Some of these may be unique within the external system, others may be more descriptive.
   * @return the value of the property, not null
   */
  @Override
  public ExternalIdBundle getAlternateIds() {
    return _alternateIds;
  }

  /**
   * Sets the bundle of alternate user identifiers.
   * <p>
   * This allows the user identifiers of external systems to be associated with the account
   * Some of these may be unique within the external system, others may be more descriptive.
   * @param alternateIds  the new value of the property, not null
   */
  public void setAlternateIds(ExternalIdBundle alternateIds) {
    JodaBeanUtils.notNull(alternateIds, "alternateIds");
    this._alternateIds = alternateIds;
  }

  /**
   * Gets the the {@code alternateIds} property.
   * <p>
   * This allows the user identifiers of external systems to be associated with the account
   * Some of these may be unique within the external system, others may be more descriptive.
   * @return the property, not null
   */
  public final Property<ExternalIdBundle> alternateIds() {
    return metaBean().alternateIds().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the roles that the user belongs to.
   * Roles are used to manage groups of multiple users.
   * This is the combined set of all roles that the user has, expressed as strings.
   * @return the value of the property, not null
   */
  @Override
  public Set<String> getRoles() {
    return _roles;
  }

  /**
   * Sets the roles that the user belongs to.
   * Roles are used to manage groups of multiple users.
   * This is the combined set of all roles that the user has, expressed as strings.
   * @param roles  the new value of the property, not null
   */
  public void setRoles(Set<String> roles) {
    JodaBeanUtils.notNull(roles, "roles");
    this._roles.clear();
    this._roles.addAll(roles);
  }

  /**
   * Gets the the {@code roles} property.
   * Roles are used to manage groups of multiple users.
   * This is the combined set of all roles that the user has, expressed as strings.
   * @return the property, not null
   */
  public final Property<Set<String>> roles() {
    return metaBean().roles().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the permissions that the user has.
   * Permissions are used to define access control.
   * This is the combined set of all permissions that the user has, expressed as strings.
   * @return the value of the property, not null
   */
  @Override
  public Set<String> getPermissions() {
    return _permissions;
  }

  /**
   * Sets the permissions that the user has.
   * Permissions are used to define access control.
   * This is the combined set of all permissions that the user has, expressed as strings.
   * @param permissions  the new value of the property, not null
   */
  public void setPermissions(Set<String> permissions) {
    JodaBeanUtils.notNull(permissions, "permissions");
    this._permissions.clear();
    this._permissions.addAll(permissions);
  }

  /**
   * Gets the the {@code permissions} property.
   * Permissions are used to define access control.
   * This is the combined set of all permissions that the user has, expressed as strings.
   * @return the property, not null
   */
  public final Property<Set<String>> permissions() {
    return metaBean().permissions().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the primary email address associated with the account.
   * @return the value of the property
   */
  @Override
  public String getEmailAddress() {
    return _emailAddress;
  }

  /**
   * Sets the primary email address associated with the account.
   * @param emailAddress  the new value of the property
   */
  public void setEmailAddress(String emailAddress) {
    this._emailAddress = emailAddress;
  }

  /**
   * Gets the the {@code emailAddress} property.
   * @return the property, not null
   */
  public final Property<String> emailAddress() {
    return metaBean().emailAddress().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the user profile, containing user settings.
   * @return the value of the property, not null
   */
  @Override
  public SimpleUserProfile getProfile() {
    return _profile;
  }

  /**
   * Sets the user profile, containing user settings.
   * @param profile  the new value of the property, not null
   */
  public void setProfile(SimpleUserProfile profile) {
    JodaBeanUtils.notNull(profile, "profile");
    this._profile = profile;
  }

  /**
   * Gets the the {@code profile} property.
   * @return the property, not null
   */
  public final Property<SimpleUserProfile> profile() {
    return metaBean().profile().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SimpleUserAccount clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SimpleUserAccount other = (SimpleUserAccount) obj;
      return JodaBeanUtils.equal(getUserName(), other.getUserName()) &&
          JodaBeanUtils.equal(getPasswordHash(), other.getPasswordHash()) &&
          JodaBeanUtils.equal(getStatus(), other.getStatus()) &&
          JodaBeanUtils.equal(getAlternateIds(), other.getAlternateIds()) &&
          JodaBeanUtils.equal(getRoles(), other.getRoles()) &&
          JodaBeanUtils.equal(getPermissions(), other.getPermissions()) &&
          JodaBeanUtils.equal(getEmailAddress(), other.getEmailAddress()) &&
          JodaBeanUtils.equal(getProfile(), other.getProfile());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getUserName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPasswordHash());
    hash = hash * 31 + JodaBeanUtils.hashCode(getStatus());
    hash = hash * 31 + JodaBeanUtils.hashCode(getAlternateIds());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRoles());
    hash = hash * 31 + JodaBeanUtils.hashCode(getPermissions());
    hash = hash * 31 + JodaBeanUtils.hashCode(getEmailAddress());
    hash = hash * 31 + JodaBeanUtils.hashCode(getProfile());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(288);
    buf.append("SimpleUserAccount{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("userName").append('=').append(JodaBeanUtils.toString(getUserName())).append(',').append(' ');
    buf.append("passwordHash").append('=').append(JodaBeanUtils.toString(getPasswordHash())).append(',').append(' ');
    buf.append("status").append('=').append(JodaBeanUtils.toString(getStatus())).append(',').append(' ');
    buf.append("alternateIds").append('=').append(JodaBeanUtils.toString(getAlternateIds())).append(',').append(' ');
    buf.append("roles").append('=').append(JodaBeanUtils.toString(getRoles())).append(',').append(' ');
    buf.append("permissions").append('=').append(JodaBeanUtils.toString(getPermissions())).append(',').append(' ');
    buf.append("emailAddress").append('=').append(JodaBeanUtils.toString(getEmailAddress())).append(',').append(' ');
    buf.append("profile").append('=').append(JodaBeanUtils.toString(getProfile())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SimpleUserAccount}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code userName} property.
     */
    private final MetaProperty<String> _userName = DirectMetaProperty.ofReadWrite(
        this, "userName", SimpleUserAccount.class, String.class);
    /**
     * The meta-property for the {@code passwordHash} property.
     */
    private final MetaProperty<String> _passwordHash = DirectMetaProperty.ofReadWrite(
        this, "passwordHash", SimpleUserAccount.class, String.class);
    /**
     * The meta-property for the {@code status} property.
     */
    private final MetaProperty<UserAccountStatus> _status = DirectMetaProperty.ofReadWrite(
        this, "status", SimpleUserAccount.class, UserAccountStatus.class);
    /**
     * The meta-property for the {@code alternateIds} property.
     */
    private final MetaProperty<ExternalIdBundle> _alternateIds = DirectMetaProperty.ofReadWrite(
        this, "alternateIds", SimpleUserAccount.class, ExternalIdBundle.class);
    /**
     * The meta-property for the {@code roles} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<String>> _roles = DirectMetaProperty.ofReadWrite(
        this, "roles", SimpleUserAccount.class, (Class) Set.class);
    /**
     * The meta-property for the {@code permissions} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<String>> _permissions = DirectMetaProperty.ofReadWrite(
        this, "permissions", SimpleUserAccount.class, (Class) Set.class);
    /**
     * The meta-property for the {@code emailAddress} property.
     */
    private final MetaProperty<String> _emailAddress = DirectMetaProperty.ofReadWrite(
        this, "emailAddress", SimpleUserAccount.class, String.class);
    /**
     * The meta-property for the {@code profile} property.
     */
    private final MetaProperty<SimpleUserProfile> _profile = DirectMetaProperty.ofReadWrite(
        this, "profile", SimpleUserAccount.class, SimpleUserProfile.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "userName",
        "passwordHash",
        "status",
        "alternateIds",
        "roles",
        "permissions",
        "emailAddress",
        "profile");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -266666762:  // userName
          return _userName;
        case 566700617:  // passwordHash
          return _passwordHash;
        case -892481550:  // status
          return _status;
        case -1805823010:  // alternateIds
          return _alternateIds;
        case 108695229:  // roles
          return _roles;
        case 1133704324:  // permissions
          return _permissions;
        case -1070931784:  // emailAddress
          return _emailAddress;
        case -309425751:  // profile
          return _profile;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SimpleUserAccount> builder() {
      return new DirectBeanBuilder<>(new SimpleUserAccount());
    }

    @Override
    public Class<? extends SimpleUserAccount> beanType() {
      return SimpleUserAccount.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code userName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> userName() {
      return _userName;
    }

    /**
     * The meta-property for the {@code passwordHash} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> passwordHash() {
      return _passwordHash;
    }

    /**
     * The meta-property for the {@code status} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UserAccountStatus> status() {
      return _status;
    }

    /**
     * The meta-property for the {@code alternateIds} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalIdBundle> alternateIds() {
      return _alternateIds;
    }

    /**
     * The meta-property for the {@code roles} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<String>> roles() {
      return _roles;
    }

    /**
     * The meta-property for the {@code permissions} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<String>> permissions() {
      return _permissions;
    }

    /**
     * The meta-property for the {@code emailAddress} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> emailAddress() {
      return _emailAddress;
    }

    /**
     * The meta-property for the {@code profile} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SimpleUserProfile> profile() {
      return _profile;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -266666762:  // userName
          return ((SimpleUserAccount) bean).getUserName();
        case 566700617:  // passwordHash
          return ((SimpleUserAccount) bean).getPasswordHash();
        case -892481550:  // status
          return ((SimpleUserAccount) bean).getStatus();
        case -1805823010:  // alternateIds
          return ((SimpleUserAccount) bean).getAlternateIds();
        case 108695229:  // roles
          return ((SimpleUserAccount) bean).getRoles();
        case 1133704324:  // permissions
          return ((SimpleUserAccount) bean).getPermissions();
        case -1070931784:  // emailAddress
          return ((SimpleUserAccount) bean).getEmailAddress();
        case -309425751:  // profile
          return ((SimpleUserAccount) bean).getProfile();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -266666762:  // userName
          ((SimpleUserAccount) bean).setUserName((String) newValue);
          return;
        case 566700617:  // passwordHash
          ((SimpleUserAccount) bean).setPasswordHash((String) newValue);
          return;
        case -892481550:  // status
          ((SimpleUserAccount) bean).setStatus((UserAccountStatus) newValue);
          return;
        case -1805823010:  // alternateIds
          ((SimpleUserAccount) bean).setAlternateIds((ExternalIdBundle) newValue);
          return;
        case 108695229:  // roles
          ((SimpleUserAccount) bean).setRoles((Set<String>) newValue);
          return;
        case 1133704324:  // permissions
          ((SimpleUserAccount) bean).setPermissions((Set<String>) newValue);
          return;
        case -1070931784:  // emailAddress
          ((SimpleUserAccount) bean).setEmailAddress((String) newValue);
          return;
        case -309425751:  // profile
          ((SimpleUserAccount) bean).setProfile((SimpleUserProfile) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((SimpleUserAccount) bean)._userName, "userName");
      JodaBeanUtils.notNull(((SimpleUserAccount) bean)._alternateIds, "alternateIds");
      JodaBeanUtils.notNull(((SimpleUserAccount) bean)._roles, "roles");
      JodaBeanUtils.notNull(((SimpleUserAccount) bean)._permissions, "permissions");
      JodaBeanUtils.notNull(((SimpleUserAccount) bean)._profile, "profile");
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
