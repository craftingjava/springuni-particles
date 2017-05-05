package com.springuni.auth.domain.model.user

import com.springuni.commons.jpa.AbstractJpaConfiguration
import org.hibernate.cfg.AvailableSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by lcsontos on 5/5/17.
 */
@Configuration
class UserJpaTestConfiguration extends AbstractJpaConfiguration {

  @Bean
  UserRepository userRepository() {
    return new UserJpaRepositoryImpl()
  }

  protected void customizeJpaPropertyMap(Map<String, String> jpaPropertyMap) {
    jpaPropertyMap.put(AvailableSettings.HBM2DDL_AUTO, "create")
  }

  @Override
  protected Properties getDataSourceProperties() {
    return [URL: "jdbc:h2:mem:test"]
  }

  @Override
  protected String getDataSourceClassName() {
    return "org.h2.jdbcx.JdbcDataSource";
  }

  @Override
  protected String[] getMappingResources() {
    return ["META-INF/user-orm.xml"] as String[]
  }

  @Override
  protected String[] getPackagesToScan() {
    return [] as String[]
  }

  protected boolean isShowSql() {
    return true
  }

}
