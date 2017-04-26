package com.springuni.commons.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lcsontos on 4/25/17.
 */
public class Validator {

  private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
      "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)"
          + "+[\\w](?:[\\w-]*[\\w])?");

  private Validator() {
  }

  /**
   * Returns <code>true</code> if the string is a valid email address.
   *
   * @param  email the string to check
   * @return <code>true</code> if the string is a valid email address;
   *         <code>false</code> otherwise
   */
  public static boolean isEmail(String email) {
    if (Objects.isNull(email)) {
      return false;
    }
    Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(email);
    return matcher.matches();
  }

}
