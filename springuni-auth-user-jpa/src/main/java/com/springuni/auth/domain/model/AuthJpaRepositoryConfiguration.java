package com.springuni.auth.domain.model;

import com.springuni.auth.domain.model.session.SessionJpaRepositoryImpl;
import com.springuni.auth.domain.model.session.SessionRepository;
import com.springuni.auth.domain.model.user.UserJpaRepositoryImpl;
import com.springuni.auth.domain.model.user.UserRepository;
import com.springuni.commons.jpa.JpaRepositoryConfigurationSupport;
import java.util.Map;
import java.util.Optional;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;

/**
 * Created by lcsontos on 5/18/17.
 */
public class AuthJpaRepositoryConfiguration extends JpaRepositoryConfigurationSupport {

  @Bean
  UserRepository userRepository() {
    return new UserJpaRepositoryImpl();
  }

  @Bean
  SessionRepository sessionRepository() {
    return new SessionJpaRepositoryImpl();
  }

  // TODO: remove when liquibase got integrated
  protected void customizeJpaPropertyMap(Map<String, String> jpaPropertyMap) {
    jpaPropertyMap.put(AvailableSettings.HBM2DDL_AUTO, "update");
  }

  @Override
  protected Optional<String[]> getMappingResources() {
    return Optional.of(new String[] { "META-INF/user-orm.xml", "META-INF/session-orm.xml" });
  }

  @Override
  protected boolean isShowSql() {
    return true;
  }

}
