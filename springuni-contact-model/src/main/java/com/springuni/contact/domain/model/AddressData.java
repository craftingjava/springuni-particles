package com.springuni.contact.domain.model;

import com.springuni.commons.domain.ValueObject;
import lombok.Data;

/**
 * Represents an address.
 */
@Data
public class AddressData implements ValueObject<AddressData> {

  private Country country;
  private State state;
  private String city;

  private String addressLine1;
  private String addressLine2;

  private String zipCode;

  private AddressType addressType;

  @Override
  public boolean sameValueAs(AddressData other) {
    return equals(other);
  }

}
