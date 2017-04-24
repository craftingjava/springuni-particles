package com.springuni.user.domain.model;

import com.springuni.commons.domain.AuditData;
import com.springuni.commons.domain.Entity;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lcsontos on 4/24/17.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ConfirmationToken implements Entity<Long, ConfirmationToken> {

  private Long id;
  private User owner;
  private String value;
  private ConfirmationTokenType tokenType;

  private boolean valid;
  private LocalDateTime expiredAt;

  private AuditData<User> auditData;

  @Override
  public Long getId() {
    return null;
  }

  @Override
  public void setId(Long id) {

  }

  @Override
  public boolean sameIdentityAs(ConfirmationToken other) {
    return equals(other);
  }

}
