package com.github.lothar.security.acl.jpa.compound;

import static com.github.lothar.security.acl.compound.CompositionType.AND;
import static com.github.lothar.security.acl.compound.CompositionType.OR;
import static org.springframework.data.jpa.domain.Specifications.where;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.compound.AclComposer;
import com.github.lothar.security.acl.compound.CompositionType;

public class JpaSpecComposer<T> implements AclComposer<Specification<T>> {

  @Override
  public Specification<T> compose(CompositionType compositionType, Specification<T> lhs,
      Specification<T> rhs) {

    if (AND.equals(compositionType)) {
      return where(lhs).and(rhs);

    } else if (OR.equals(compositionType)) {
      return where(lhs).or(rhs);

    } else {
      throw new IllegalArgumentException("Illegal composition : " + compositionType);
    }
  }

}
