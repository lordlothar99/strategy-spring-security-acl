package com.github.lothar.security.acl.jpa.compound;

import static org.springframework.data.jpa.domain.Specifications.where;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.compound.AclComposer;

public class JpaSpecComposer<T> implements AclComposer<Specification<T>> {

  @Override
  public Specification<T> and(Specification<T> lhs, Specification<T> rhs) {
    return where(lhs).and(rhs);
  }

  @Override
  public Specification<T> or(Specification<T> lhs, Specification<T> rhs) {
    return where(lhs).or(rhs);
  }

}
