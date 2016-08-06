package com.github.lothar.security.acl.jdk8;

public interface Supplier<T> {
  /**
   * Gets a result.
   *
   * @return a result
   */
  T get();
}
