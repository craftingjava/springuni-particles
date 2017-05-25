/**
 * Copyright (c) 2017-present Laszlo Csontos All rights reserved.
 *
 * This file is part of springuni-particles.
 *
 * springuni-particles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * springuni-particles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with springuni-particles.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.springuni.commons.util;

import static com.springuni.commons.util.RandomUtil.nextInt;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.Instant;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database agnostic unique ID generator inspired by
 * <a href="https://engineering.instagram.com/sharding-ids-at-instagram-1cf5a71e5a5c">
 * Sharding & IDs at Instagram</a>
 *
 * <p>Going to the most to the least significant bits
 * <ul>
 * <li>the first bit (sign) is always zero
 * <li>the next 7 bits represent the shard ID
 * <li>the next 40 bits represent the elapsed milliseconds from a custom Epoch (2017-02-20)
 * <li>the next 16 bits represent a serial number XOR-ed with a per-thread random number
 * </ul>
 *
 * <p>With this technique 65536 unique IDs can be generated per shard per millisecond.
 */
public final class IdentityGenerator {

  /**
   * Default shard ID.
   */
  public static final byte DEFAULT_SHARD_ID = 0;

  /**
   * Custom Epoch (2017-02-20).
   */
  public static final Instant EPOCH = LocalDateTime.of(2017, 2, 20, 0, 0, 0, 0).toInstant(UTC);

  private static final Logger LOGGER = LoggerFactory.getLogger(IdentityGenerator.class);

  private static final ThreadLocal<Serial> THREAD_LOCAL_SERIAL;

  static {
    THREAD_LOCAL_SERIAL = ThreadLocal.withInitial(() -> new Serial(nextInt(), nextInt()));
  }

  private IdentityGenerator() {
  }

  /**
   * Extracts the timestamp parts as an {@link Instant} from the given ID.
   *
   * @param id ID
   * @return an {@link Instant}
   */
  public static Instant extractInstant(long id) {
    long time = (id & 0xffffffffff0000L) >>> 16;
    return EPOCH.plusMillis(time);
  }

  /**
   * Extracts the shard ID from the given ID.
   *
   * @param id ID
   * @return shard ID
   */
  public static byte extractShardId(long id) {
    return (byte) ((id >>> 56) & 0x7fL);
  }

  /**
   * Generates a new unique ID for the default shard.
   *
   * @return a new unique ID
   */
  public static long generate() {
    return generate(DEFAULT_SHARD_ID);
  }

  /**
   * Generates a new unique ID for the given shard.
   *
   * @return a new unique ID
   */
  public static long generate(byte shardId) {
    long time = MILLIS.between(EPOCH, Instant.now());
    int serial = THREAD_LOCAL_SERIAL.get().increment();
    return doGenerate(shardId, time, serial);
  }

  static long doGenerate(byte shardId, long time, int serial) {
    return (shardId & 0x7fL) << 56 | (time & 0xffffffffffL) << 16 | (serial & 0xffff);
  }

  static class Serial {

    final int mask;
    int value;

    Serial(int value, int mask) {
      this.value = value;
      this.mask = mask;
    }

    int increment() {
      return (value++ ^ mask);
    }

  }

}
