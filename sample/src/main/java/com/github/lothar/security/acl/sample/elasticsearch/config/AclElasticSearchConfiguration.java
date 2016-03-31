package com.github.lothar.security.acl.sample.elasticsearch.config;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;

@Configuration
public class AclElasticSearchConfiguration {

  private FilterBuilder smithFamilyOnly = FilterBuilders.termFilter("lastName", "Smith");

  @Bean
  public AclStrategy customerStrategy(ElasticSearchFeature elasticSearchFeature) {
    SimpleAclStrategy simpleAclStrategy = new SimpleAclStrategy();
    simpleAclStrategy.register(elasticSearchFeature, smithFamilyOnly);
    return simpleAclStrategy;
  }
}