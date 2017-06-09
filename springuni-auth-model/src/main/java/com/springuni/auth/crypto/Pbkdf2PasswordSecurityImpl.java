package com.springuni.auth.crypto;

import com.springuni.auth.domain.model.user.Password;
import com.springuni.commons.util.RandomUtil;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 based password hashing.
 * Based on https://gist.github.com/jtan189/3804290.
 */
public class Pbkdf2PasswordSecurityImpl implements PasswordSecurity {

  private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

  private static final int SALT_BYTES = 32;
  private static final int HASH_BYTES = 32;
  private static final int PBKDF2_ITERATIONS = 2000;

  private final SecretKeyFactory secretKeyFactory;

  public Pbkdf2PasswordSecurityImpl() {
    try {
      secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean check(Password password, String rawPassword) {
    // Decode the hash into its parameters
    byte[] salt = fromHex(password.getPasswordSalt());
    byte[] hash = fromHex(password.getPasswordHash());

    // Compute the hash of the provided password, using the same salt, iteration count, and length
    byte[] testHash = pbkdf2(rawPassword.toCharArray(), salt, PBKDF2_ITERATIONS, hash.length);

    // Compare the hashes in constant time. The password is correct if both hashes match.
    return slowEquals(hash, testHash);
  }

  @Override
  public Password ecrypt(String rawPassword) {
    byte[] salt = new byte[SALT_BYTES];
    RandomUtil.nextBytes(salt);

    byte[] hash = pbkdf2(rawPassword.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES);

    return new Password(toHex(hash), toHex(salt));
  }

  /**
   * Compares two byte arrays in length-constant time. This comparison method
   * is used so that password hashes cannot be extracted from an on-line
   * system using a timing attack and then attacked off-line.
   *
   * @param a the first byte array
   * @param b the second byte array
   * @return true if both byte arrays are the same, false if not
   */
  private boolean slowEquals(byte[] a, byte[] b) {
    int diff = a.length ^ b.length;
    for (int i = 0; i < a.length && i < b.length; i++) {
      diff |= a[i] ^ b[i];
    }
    return diff == 0;
  }

  /**
   * Computes the PBKDF2 hash of a password.
   *
   * @param password the password to hash.
   * @param salt the salt
   * @param iterations the iteration count (slowness factor)
   * @param bytes the length of the hash to compute in bytes
   * @return the PBDKF2 hash of the password
   */
  private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
    KeySpec keySpec = new PBEKeySpec(password, salt, iterations, bytes * 8);
    try {
      return secretKeyFactory.generateSecret(keySpec).getEncoded();
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Converts a string of hexadecimal characters into a byte array.
   *
   * @param hex the hex string
   * @return the hex string decoded into a byte array
   */
  private byte[] fromHex(String hex) {
    byte[] binary = new byte[hex.length() / 2];
    for (int i = 0; i < binary.length; i++) {
      binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return binary;
  }

  /**
   * Converts a byte array into a hexadecimal string.
   *
   * @param array the byte array to convert
   * @return a length*2 character string encoding the byte array
   */
  private String toHex(byte[] array) {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if (paddingLength > 0) {
      return String.format("%0" + paddingLength + "d", 0) + hex;
    } else {
      return hex;
    }
  }

}
