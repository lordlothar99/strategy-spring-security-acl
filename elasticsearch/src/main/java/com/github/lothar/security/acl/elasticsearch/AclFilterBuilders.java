package com.github.lothar.security.acl.elasticsearch;

import static org.elasticsearch.index.query.FilterBuilders.matchAllFilter;

import org.elasticsearch.index.query.FilterBuilder;

public class AclFilterBuilders {

  private AclFilterBuilders() {}

  public static FilterBuilder allowAll() {
    return matchAllFilter();
  }
}
