package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.AclFeature;

public final class ElasticSearchFeature implements AclFeature<FilterBuilder> {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
