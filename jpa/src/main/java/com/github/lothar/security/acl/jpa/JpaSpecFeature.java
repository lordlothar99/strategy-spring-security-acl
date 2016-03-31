package com.github.lothar.security.acl.jpa;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclFeatureType;

public final class JpaSpecFeature<T> implements AclFeatureType<Specification<T>> {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
