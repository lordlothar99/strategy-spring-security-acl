package com.github.lothar.security.acl.jpa.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.bean.NamedBean;

public class AllowAllSpecification<T> extends NamedBean implements Specification<T> {

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    return cb.conjunction();
  }
}
