package com.springuni.auth.domain.model.user;

import com.springuni.commons.jpa.JpaRepositoryConfigurationSupport;
import java.util.Map;
import java.util.Optional;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;

/**
 * Created by lcsontos on 5/18/17.
 */
public class UserJpaRepositoryConfiguration extends JpaRepositoryConfigurationSupport {

  @Bean
  UserRepository userRepository() {
    return new UserJpaRepositoryImpl();
  }

  // TODO: remove when liquibase got integrated
  protected void customizeJpaPropertyMap(Map<String, String> jpaPropertyMap) {
    jpaPropertyMap.put(AvailableSettings.HBM2DDL_AUTO, "create");
  }

  @Override
  protected Optional<String[]> getMappingResources() {
    return Optional.of(new String[] { "META-INF/user-orm.xml" });
  }

  @Override
  protected boolean isShowSql() {
    return true;
  }

}
