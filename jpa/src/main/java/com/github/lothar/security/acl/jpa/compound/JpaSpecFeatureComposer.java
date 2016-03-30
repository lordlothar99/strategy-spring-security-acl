package com.github.lothar.security.acl.jpa.compound;

import static com.github.lothar.security.acl.compound.CompositionType.AND;
import static com.github.lothar.security.acl.compound.CompositionType.OR;
import static org.springframework.data.jpa.domain.Specifications.where;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.compound.AclFeatureComposer;
import com.github.lothar.security.acl.compound.CompositionType;

public class JpaSpecFeatureComposer implements AclFeatureComposer<Specification<Object>> {

  @Override
  public Specification<Object> compose(CompositionType compositionType, Specification<Object> lhs,
      Specification<Object> rhs) {

    if (AND.equals(compositionType)) {
      return where(lhs).and(rhs);

    } else if (OR.equals(compositionType)) {
      return where(lhs).or(rhs);

    } else {
      throw new IllegalArgumentException("Illegal composition : " + compositionType);
    }
  }

}
