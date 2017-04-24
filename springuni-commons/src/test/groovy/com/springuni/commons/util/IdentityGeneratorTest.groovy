package com.springuni.commons.util

import static com.springuni.commons.util.IdentityGenerator.EPOCH
import static com.springuni.commons.util.IdentityGenerator.doGenerate
import static com.springuni.commons.util.IdentityGenerator.extractInstant
import static com.springuni.commons.util.IdentityGenerator.extractShardId
import static com.springuni.commons.util.IdentityGenerator.generate
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertTrue

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by lcsontos on 2/20/17.
 */
class IdentityGeneratorTest {

  static final Logger LOGGER = LoggerFactory.getLogger(IdentityGeneratorTest.class)

  static final long TIME_DIFF = 10000
  static final Instant INSTANT = EPOCH.plusMillis(TIME_DIFF)
  static final byte SHARD_ID = 126

  @Test
  void testCollision() throws Exception {
    ExecutorService executorService = Executors.newFixedThreadPool(100)

    final int NUM_IDS = 1_000_000
    final ConcurrentMap<Long, AtomicInteger> collisionMap = new ConcurrentHashMap<>(NUM_IDS)

    for (int i = 0; i < NUM_IDS; i++) {
      executorService.submit(new Runnable() {
        @Override
        void run() {
          long id = generate()

          AtomicInteger collisionCount = collisionMap.get(id)
          if (collisionCount == null) {
            collisionCount = new AtomicInteger(0)
            AtomicInteger oldCollisionCount = collisionMap.putIfAbsent(id, collisionCount)
            if (oldCollisionCount != null) {
              collisionCount.addAndGet(oldCollisionCount.get())
            }
          } else {
            int currentCollisionCount = collisionCount.incrementAndGet()
            LOGGER.warn("{} collision(s) on ID {}", currentCollisionCount, id)
          }
        }
      })
    }

    executorService.shutdown()
    executorService.awaitTermination(30, TimeUnit.SECONDS)

    double uniquePercent = (collisionMap.size() * 100.0 / NUM_IDS)
    LOGGER.info("{}% of all IDs are unique.", uniquePercent)

    assertTrue(uniquePercent > 99.9)
  }

  @Test
  void testDoGenerate_withSerial() {
    long id = doGenerate((byte) 0, 0L, 1)
    assertEquals(1L, id)
  }

  @Test
  void testDoGenerate_withShardId() {
    long id = doGenerate((byte) 1, 0L, 0)
    assertEquals(1L << 56, id)
  }

  @Test
  void testDoGenerate_withTime() {
    long id = doGenerate((byte) 0, 1L, 0)
    assertEquals(1L << 16, id)
  }

  @Test
  void testExtractShardId() {
    long id = doGenerate(SHARD_ID, TIME_DIFF, 10)
    byte shardId = extractShardId(id)
    assertEquals(SHARD_ID, shardId)
  }

  @Test
  void testExtractInstant() {
    long id = doGenerate(SHARD_ID, TIME_DIFF, 10)
    Instant instant = extractInstant(id)
    assertEquals(INSTANT.getEpochSecond(), instant.getEpochSecond())
  }

  @Test
  void testGenerate() {
    long id = generate()
    assertNotEquals(0, id)
    System.out.println(id)
  }

}
