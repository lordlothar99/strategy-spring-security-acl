package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.bean.NamedBean;

public final class ElasticSearchFeature extends NamedBean implements AclFeature<FilterBuilder> {
}
