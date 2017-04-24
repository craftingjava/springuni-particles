package com.springuni.user.domain.model;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import com.springuni.contact.domain.model.ContactData;
import java.util.Locale;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model class for users.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "screenName")
public class User implements Entity<Long, User> {

  private Long id;
  private String screenName;

  private ContactData contactData;

  private String passwordHash;
  private String passwordSalt;

  private Set<String> authorities;
  private Timezone timezone;
  private Locale locale;

  private boolean confirmed;
  private boolean locked;
  private boolean deleted;

  private AuditData<User> auditData;

  @Override
  public boolean sameIdentityAs(User other) {
    return equals(other);
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
