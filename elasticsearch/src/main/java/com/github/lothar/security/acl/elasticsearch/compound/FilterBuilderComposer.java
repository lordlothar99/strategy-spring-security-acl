package com.github.lothar.security.acl.elasticsearch.compound;

import static com.github.lothar.security.acl.compound.CompositionType.AND;
import static com.github.lothar.security.acl.compound.CompositionType.OR;
import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.compound.AclComposer;
import com.github.lothar.security.acl.compound.CompositionType;

public class FilterBuilderComposer implements AclComposer<FilterBuilder> {

  @Override
  public FilterBuilder compose(CompositionType compositionType, FilterBuilder lhs,
      FilterBuilder rhs) {

    if (AND.equals(compositionType)) {
      return andFilter(lhs, rhs);

    } else if (OR.equals(compositionType)) {
      return orFilter(lhs, rhs);

    } else {
      throw new IllegalArgumentException("Illegal composition : " + compositionType);
    }
  }

}
