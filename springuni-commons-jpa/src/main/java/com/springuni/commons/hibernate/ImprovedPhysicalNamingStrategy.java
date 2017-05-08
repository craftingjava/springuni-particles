package com.springuni.commons.hibernate;

import java.util.Locale;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Replacement of {@link org.hibernate.cfg.ImprovedNamingStrategy}.
 */
public class ImprovedPhysicalNamingStrategy implements PhysicalNamingStrategy {

  @Override
  public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return addUnderscores(name);
  }

  @Override
  public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return addUnderscores(name);
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return addUnderscores(name);
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return addUnderscores(name);
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return addUnderscores(name);
  }

  /**
   * Get an the identifier for the specified details.
   *
   * @param name the name of the identifier
   * @param quoted if the identifier is quoted
   * @return an identifier instance
   */
  protected Identifier getIdentifier(String name, boolean quoted) {
    return new Identifier(name.toLowerCase(Locale.ROOT), quoted);
  }

  private Identifier addUnderscores(Identifier name) {
    if (name == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
    for (int i = 1; i < builder.length() - 1; i++) {
      if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
        builder.insert(i++, '_');
      }
    }
    return getIdentifier(builder.toString(), name.isQuoted());
  }

  private boolean isUnderscoreRequired(char before, char current, char after) {
    return Character.isLowerCase(before) && Character.isUpperCase(current)
        && Character.isLowerCase(after);
  }

}
