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

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Utilities for initializing maps easily.
 * http://minborgsjavapot.blogspot.hu/2014/12/java-8-initializing-maps-in-smartest-way.html
 */
public class Maps {

  private Maps() {
  }

  /**
   * Creates a {@code MapBuilder}.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @return a {@code MapBuilder}
   */
  public static <K, V> MapBuilder<K, V> builder() {
    return builder(HashMap::new);
  }

  /**
   * Creates a {@code MapBuilder} based on the given supplier.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @param mapSupplier map supplier
   * @return a {@code MapBuilder}
   */
  public static <K, V> MapBuilder<K, V> builder(Supplier<Map<K, V>> mapSupplier) {
    return new MapBuilder<>(mapSupplier.get());
  }

  /**
   * Creates a {@code ConcurrentMapBuilder}.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @return a {@code ConcurrentMapBuilder}
   */
  public static <K, V> ConcurrentMapBuilder<K, V> concurrentBuilder() {
    return concurrentBuilder(ConcurrentHashMap::new);
  }

  /**
   * Creates a {@code ConcurrentMapBuilder} based on the given supplier.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @param mapSupplier map supplier
   * @return a {@code ConcurrentMapBuilder}
   */
  public static <K, V> ConcurrentMapBuilder<K, V> concurrentBuilder(
      Supplier<ConcurrentMap<K, V>> mapSupplier) {

    return new ConcurrentMapBuilder<>(mapSupplier.get());
  }

  /**
   * Creates a {@code Map.Entry} for the given key, value pair.
   *
   * @param key Key
   * @param value Value
   * @param <K> Key type
   * @param <V> Value type
   * @return a {@code Map.Entry}
   */
  public static <K, V> Map.Entry<K, V> entry(K key, V value) {
    return new SimpleEntry<>(key, value);
  }

  /**
   * Converts entries to map.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @return a collector
   */
  public static <K, V> Collector<Entry<K, V>, ?, Map<K, V>> entriesToMap() {
    return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
  }

  /**
   * Converts entries to a cuncurrent map.
   *
   * @param <K> Key type
   * @param <V> Value type
   * @return a collector
   */
  public static <K, V> Collector<Map.Entry<K, V>, ?, ConcurrentMap<K, V>> entriesToConcurrentMap() {
    return Collectors.toConcurrentMap((e) -> e.getKey(), (e) -> e.getValue());
  }

  private static class BaseBuilder<M extends Map<K, V>, K, V> {

    protected final M map;

    public BaseBuilder(M map) {
      this.map = map;
    }

    public BaseBuilder<M, K, V> put(K key, V value) {
      map.put(key, value);
      return this;
    }

    public M build() {
      return map;
    }

  }

  public static class MapBuilder<K, V> extends BaseBuilder<Map<K, V>, K, V> {

    private boolean unmodifiable;

    public MapBuilder(Map<K, V> map) {
      super(map);
    }

    @Override
    public MapBuilder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }

    public MapBuilder<K, V> unmodifiable(boolean unmodifiable) {
      this.unmodifiable = unmodifiable;
      return this;
    }

    @Override
    public Map<K, V> build() {
      if (unmodifiable) {
        return Collections.unmodifiableMap(super.build());
      } else {
        return super.build();
      }
    }

  }

  public static class ConcurrentMapBuilder<K, V> extends BaseBuilder<ConcurrentMap<K, V>, K, V> {

    public ConcurrentMapBuilder(ConcurrentMap<K, V> map) {
      super(map);
    }

    @Override
    public ConcurrentMapBuilder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }

  }

}
