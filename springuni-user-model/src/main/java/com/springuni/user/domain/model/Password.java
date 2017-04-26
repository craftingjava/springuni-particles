package com.springuni.user.domain.model;

import com.springuni.commons.domain.ValueObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lcsontos on 4/24/17.
 */
@Data
public class Password implements ValueObject<Password> {

  private final String passwordHash;
  private final String passwordSalt;

  public Password(String passwordHash, String passwordSalt) {
    this.passwordHash = passwordHash;
    this.passwordSalt = passwordSalt;
  }

  @Override
  public boolean sameValueAs(Password other) {
    return equals(other);
  }

}
