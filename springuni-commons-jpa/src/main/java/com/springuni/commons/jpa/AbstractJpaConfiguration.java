/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.springuni.commons.jpa;

import static com.springuni.commons.util.Maps.entriesToMap;
import static com.springuni.commons.util.Maps.entry;
import static javax.persistence.SharedCacheMode.ENABLE_SELECTIVE;
import static org.hibernate.cfg.AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.LOG_SESSION_METRICS;
import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Support class for configuring a JPA entity manager and it's related infrastructure.
 */
@Configuration
@EnableTransactionManagement
public abstract class AbstractJpaConfiguration {

  private JpaTransactionManager transactionManager;

  /**
   * Primary data source.
   *
   * @return the primary data source
   */
  @Bean
  @Primary
  public DataSource dataSource() {
    DataSource dataSourceTarget = new HikariDataSource(getHikariConfig());
    return new LazyConnectionDataSourceProxy(dataSourceTarget);
  }

  /**
   * Creates the entity manager factory.
   *
   * @param dataSource the data source to use
   * @return entity manager factory
   */
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();

    entityManagerFactoryBean.setDataSource(dataSource);

    JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter();
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    Map<String, String> jpaPropertyMap = getJpaPropertyMap();
    entityManagerFactoryBean.setJpaPropertyMap(jpaPropertyMap);

    entityManagerFactoryBean.setPackagesToScan(getPackagesToScan());
    entityManagerFactoryBean.setMappingResources(getMappingResources());

    // https://hibernate.atlassian.net/browse/HHH-5303#comment-44439
    entityManagerFactoryBean.setSharedCacheMode(ENABLE_SELECTIVE);

    return entityManagerFactoryBean;
  }

  /**
   * Create the TX manager for the given EMF.
   *
   * @param dataSource data source
   * @param entityManagerFactory entity manager factory
   * @return TX manager
   */
  @Bean
  public PlatformTransactionManager transactionManager(
      DataSource dataSource, EntityManagerFactory entityManagerFactory) {

    transactionManager = new JpaTransactionManager(entityManagerFactory);
    transactionManager.setDataSource(dataSource);
    return transactionManager;
  }

  protected JpaVendorAdapter createJpaVendorAdapter() {
    AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(isShowSql());

    // TODO: detect database automatically
    adapter.setDatabase(Database.H2);
    return adapter;
  }

  protected void customizeJpaPropertyMap(Map<String, String> jpaPropertyMap) {
  }

  protected abstract Properties getDataSourceProperties();

  protected abstract String getDataSourceClassName();

  protected HikariConfig getHikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDataSourceProperties(getDataSourceProperties());
    hikariConfig.setDataSourceClassName(getDataSourceClassName());
    return hikariConfig;
  }

  protected abstract String[] getMappingResources();

  protected abstract String[] getPackagesToScan();

  protected Map<String, String> getJpaPropertyMap() {
    Map<String, String> jpaPropertyMap = Stream.of(
        entry(FORMAT_SQL, "true"),
        entry(HBM2DDL_AUTO, "validate"),
        entry(ENABLE_LAZY_LOAD_NO_TRANS, "true"),

        // TODO: implement replacement for org.hibernate.cfg.ImprovedNamingStrategy
        entry(PHYSICAL_NAMING_STRATEGY, PhysicalNamingStrategyStandardImpl.class.getName()),

        // https://hibernate.atlassian.net/browse/HHH-8793
        entry(GENERATE_STATISTICS, "true"),
        entry(LOG_SESSION_METRICS, "false")
    ).collect(entriesToMap());
    customizeJpaPropertyMap(jpaPropertyMap);
    return Collections.unmodifiableMap(jpaPropertyMap);
  }

  protected boolean isShowSql() {
    return false;
  }

}
