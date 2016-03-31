package com.github.lothar.security.acl.jpa;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclFeature;

public final class JpaSpecFeature<T> implements AclFeature<Specification<T>> {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
