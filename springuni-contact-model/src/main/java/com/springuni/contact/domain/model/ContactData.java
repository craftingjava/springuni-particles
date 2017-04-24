package com.springuni.contact.domain.model;

import com.springuni.commons.domain.ValueObject;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

/**
 * Created by lcsontos on 4/24/17.
 */
@Data
public class ContactData implements ValueObject<ContactData> {

  private String email;

  private String firstName;
  private String lastName;

  private Set<AddressData> addresses;

  private Gender gender;

  private LocalDate birthday;

  @Override
  public boolean sameValueAs(ContactData other) {
    return equals(other);
  }

}
