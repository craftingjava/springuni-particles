package com.springuni.commons.domain;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Created by lcsontos on 4/24/17.
 */
@Data
public class AuditData<U> implements ValueObject<AuditData<U>> {

  private LocalDateTime createdAt;
  private U createdBy;

  private LocalDateTime modifiedAt;
  private U modifiedBy;

  @Override
  public boolean sameValueAs(AuditData<U> other) {
    return equals(other);
  }

}
