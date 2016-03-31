package com.github.lothar.security.acl.sample.config;

import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;

@Configuration
public class SampleAclConfiguration {

  @Resource
  private ElasticSearchFeature elasticSearchFeature;

  @Bean
  public AclStrategy customerStrategy() {
    SimpleAclStrategy customerStrategy = new SimpleAclStrategy();
    customerStrategy.register(elasticSearchFeature, termFilter("lastName", "Smith"));
    return customerStrategy;
  }
}
