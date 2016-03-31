package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.AclFeatureType;

public final class ElasticSearchFeature implements AclFeatureType<FilterBuilder> {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
