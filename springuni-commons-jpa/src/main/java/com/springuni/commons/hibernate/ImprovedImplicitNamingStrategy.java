package com.springuni.commons.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

/**
 * Replacement of {@link org.hibernate.cfg.ImprovedNamingStrategy}.
 */
public class ImprovedImplicitNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

  @Override
  public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
    String name = source.getOwningPhysicalTableName() + "_"
        + source.getAssociationOwningAttributePath().getProperty();
    return toIdentifier(name, source.getBuildingContext());
  }

}
