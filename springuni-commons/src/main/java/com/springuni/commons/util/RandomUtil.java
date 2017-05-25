package com.springuni.commons.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lcsontos on 5/24/17.
 */
public class RandomUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);

  private static final ThreadLocal<Random> THREAD_LOCAL_RANDOM;

  static {
    THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(RandomUtil::createSecureRandom);
  }

  public static IntStream ints(long streamSize) {
    return THREAD_LOCAL_RANDOM.get().ints(streamSize);
  }

  public static void nextBytes(byte[] bytes) {
    THREAD_LOCAL_RANDOM.get().nextBytes(bytes);
  }

  public static int nextInt() {
    return THREAD_LOCAL_RANDOM.get().nextInt();
  }

  private static Random createSecureRandom() {
    try {
      return SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException nae) {
      LOGGER.warn("Couldn't create strong secure random generator; reason: {}.", nae.getMessage());
      return new SecureRandom();
    }
  }

}
