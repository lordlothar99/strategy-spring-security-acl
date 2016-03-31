package com.github.lothar.security.acl.elasticsearch.compound;

import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.compound.AclComposer;

public class FilterBuilderComposer implements AclComposer<FilterBuilder> {

  @Override
  public FilterBuilder and(FilterBuilder lhs, FilterBuilder rhs) {
    return andFilter(lhs, rhs);
  }

  @Override
  public FilterBuilder or(FilterBuilder lhs, FilterBuilder rhs) {
    return orFilter(lhs, rhs);
  }

}
