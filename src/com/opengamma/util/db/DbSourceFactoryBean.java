/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.util.db;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.SingletonFactoryBean;

/**
 * General purpose source of access to databases.
 * <p>
 * This class provides a simple-to-setup and simple-to-use way to access databases.
 * It can be configured for access via JDBC, Hibernate or both.
 * The main benefit is simpler configuration, especially if that configuration is in XML.
 */
public class DbSourceFactoryBean extends SingletonFactoryBean<DbSource> {

  /**
   * The name that this source is known by.
   */
  private String _name;
  /**
   * The underlying data source.
   */
  private DataSource _dataSource;
  /**
   * The database type, used to create a helper class.
   */
  private String _databaseDialectClass;
  /**
   * Factory bean to create Hibernate.
   * This can be used if more control is needed than the properties exposed on this factory bean.
   */
  private LocalSessionFactoryBean _hibernateFactoryBean;
  /**
   * The Hibernate mapping resource locations.
   */
  private String[] _mappingResources;
  /**
   * The Hibernate configuration to show the SQL.
   */
  private boolean _hibernateShowSql;
  /**
   * The transaction isolation level.
   * See {@link DefaultTransactionDefinition}.
   */
  private String _transactionIsolationLevelName;
  /**
   * The transaction isolation level.
   * See {@link DefaultTransactionDefinition}.
   */
  private String _transactionPropagationBehaviorName;
  /**
   * The transaction timeout in seconds.
   * See {@link DefaultTransactionDefinition}.
   */
  private int _transactionTimeoutSecs;
  /**
   * The transaction manager.
   * This can be left null, and an appropriate one will be created.
   */
  private PlatformTransactionManager _transactionManager;

  /**
   * Creates an instance.
   */
  public DbSourceFactoryBean() {
  }

  //-------------------------------------------------------------------------
  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  public DataSource getDataSource() {
    return _dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    _dataSource = dataSource;
  }

  public String getDialect() {
    return _databaseDialectClass;
  }

  public void setDialect(String databaseDialectClass) {
    _databaseDialectClass = databaseDialectClass;
  }

  public LocalSessionFactoryBean getHibernateFactoryBean() {
    return _hibernateFactoryBean;
  }

  public void setHibernateFactoryBean(LocalSessionFactoryBean hibernateFactoryBean) {
    _hibernateFactoryBean = hibernateFactoryBean;
  }

  public String[] getHibernateMappingResources() {
    return _mappingResources;
  }

  public void setHibernateMappingResources(String[] mappingResources) {
    _mappingResources = mappingResources;
  }

  public boolean isHibernateShowSql() {
    return _hibernateShowSql;
  }

  public void setHibernateShowSql(boolean hibernateShowSql) {
    _hibernateShowSql = hibernateShowSql;
  }

  public String getTransactionIsolationLevelName() {
    return _transactionIsolationLevelName;
  }

  public void setTransactionIsolationLevelName(String transactionIsolationLevelName) {
    _transactionIsolationLevelName = transactionIsolationLevelName;
  }

  public String getTransactionPropagationBehaviorName() {
    return _transactionPropagationBehaviorName;
  }

  public void setTransactionPropagationBehaviorName(String transactionPropagationBehaviorName) {
    _transactionPropagationBehaviorName = transactionPropagationBehaviorName;
  }

  public int getTransactionTimeout() {
    return _transactionTimeoutSecs;
  }

  public void setTransactionTimeout(int transactionTimeoutSecs) {
    _transactionTimeoutSecs = transactionTimeoutSecs;
  }

  public PlatformTransactionManager getTransactionManager() {
    return _transactionManager;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    _transactionManager = transactionManager;
  }

  //-------------------------------------------------------------------------
  @Override
  protected DbSource createObject() {
    ArgumentChecker.notNull(getName(), "name");
    ArgumentChecker.notNull(getDataSource(), "dataSource");
    ArgumentChecker.notNull(getDialect(), "dialect");
    DbHelper dialect = createDialect();
    SessionFactory hbFactory = createSessionFactory(dialect);
    DefaultTransactionDefinition transDefn = createTransactionDefinition();
    PlatformTransactionManager transMgr = createTransactionManager(hbFactory);
    return new DbSource(getName(), getDataSource(), dialect, hbFactory, transDefn, transMgr);
  }

  /**
   * Creates the database dialect.
   * @return the dialect, not null
   */
  protected DbHelper createDialect() {
    DbHelper dialect = null;
    String dialectStr = getDialect();
    if (dialectStr.contains(".") == false) {
      dialectStr = "org.opengamma.util." + dialectStr;
    }
    try {
      dialect = (DbHelper) getClass().getClassLoader().loadClass(dialectStr).newInstance();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return dialect;
  }

  /**
   * Creates the Hibernate session factory.
   * @param dialect  the dialect instance, not null
   * @return the session factory, may be null
   */
  protected SessionFactory createSessionFactory(DbHelper dialect) {
    LocalSessionFactoryBean factory = getHibernateFactoryBean();
    if (factory == null) {
      factory = new LocalSessionFactoryBean();
      factory.setMappingResources(getHibernateMappingResources());
      factory.setDataSource(getDataSource());
      Properties props = new Properties();
      props.setProperty("hibernate.dialect", dialect.getHibernateDialect().getClass().getName());
      props.setProperty("hibernate.show_sql", String.valueOf(isHibernateShowSql()));
      factory.setHibernateProperties(props);
    }
    try {
      factory.afterPropertiesSet();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return (SessionFactory) factory.getObject();
  }

  /**
   * Creates the transaction definition.
   * @return the transaction definition, not null
   */
  protected DefaultTransactionDefinition createTransactionDefinition() {
    DefaultTransactionDefinition transDefn = new DefaultTransactionDefinition();
    transDefn.setName(getName());
    if (getTransactionIsolationLevelName() != null) {
      transDefn.setIsolationLevelName(getTransactionIsolationLevelName());
    }
    if (getTransactionPropagationBehaviorName() != null) {
      transDefn.setPropagationBehaviorName(getTransactionPropagationBehaviorName());
    }
    if (getTransactionTimeout() != 0) {
      transDefn.setTimeout(getTransactionTimeout());
    }
    return transDefn;
  }

  /**
   * Creates the transaction manager.
   * @param sessionFactory  the Hibernate session factory, may be null
   * @return the transaction manager, not null
   */
  protected PlatformTransactionManager createTransactionManager(SessionFactory sessionFactory) {
    PlatformTransactionManager transMgr = getTransactionManager();
    if (transMgr == null) {
      if (sessionFactory != null) {
        transMgr = new HibernateTransactionManager(sessionFactory);
      } else {
        transMgr = new DataSourceTransactionManager(getDataSource());
      }
    }
    return transMgr;
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
