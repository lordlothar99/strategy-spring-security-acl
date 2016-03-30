package com.github.lothar.security.acl.jpa.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class AllowAllSpecification<T> implements Specification<T> {

  private static AllowAllSpecification<Object> INSTANCE = new AllowAllSpecification<Object>();

  private AllowAllSpecification() {}

  @SuppressWarnings("unchecked")
  public static <T> Specification<T> get() {
    return (Specification<T>) INSTANCE;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    return cb.conjunction();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
