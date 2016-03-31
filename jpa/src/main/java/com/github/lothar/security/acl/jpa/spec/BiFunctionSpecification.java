package com.github.lothar.security.acl.jpa.spec;

import java.util.function.BiFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class BiFunctionSpecification<T> implements Specification<T> {

  private BiFunction<Root<T>, CriteriaBuilder, Predicate> predicateFunction;

  public BiFunctionSpecification(
      BiFunction<Root<T>, CriteriaBuilder, Predicate> predicateFunction) {
    this.predicateFunction = predicateFunction;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    return predicateFunction.apply(root, cb);
  }
}
