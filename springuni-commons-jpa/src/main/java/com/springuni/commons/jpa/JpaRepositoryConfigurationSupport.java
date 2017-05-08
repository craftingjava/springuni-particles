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

import java.util.Optional;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Support class for configuring a JPA entity manager and it's related infrastructure.
 */
public class JpaRepositoryConfigurationSupport
    extends AbstractJpaRepositoryConfiguration implements EnvironmentAware {

  private static final String JDBC_DATABASE_URL = "JDBC_DATABASE_URL";
  private static final String JDBC_DATABASE_USERNAME = "JDBC_DATABASE_USERNAME";
  private static final String JDBC_DATABASE_PASSWORD = "JDBC_DATABASE_PASSWORD";

  private Environment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  protected Optional<String> getJdbcUrl() {
    return Optional.ofNullable(environment.getProperty(JDBC_DATABASE_URL));
  }

  @Override
  protected Optional<String> getJdbcUsername() {
    return Optional.ofNullable(environment.getProperty(JDBC_DATABASE_USERNAME));
  }

  @Override
  protected Optional<String> getJdbcPassword() {
    return Optional.ofNullable(environment.getProperty(JDBC_DATABASE_PASSWORD));
  }

  @Override
  protected Optional<String[]> getMappingResources() {
    return Optional.empty();
  }

  @Override
  protected Optional<String[]> getPackagesToScan() {
    return Optional.empty();
  }

}
