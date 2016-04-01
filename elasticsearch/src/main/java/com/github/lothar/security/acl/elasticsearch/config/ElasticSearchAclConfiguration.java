package com.github.lothar.security.acl.elasticsearch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.compound.FilterBuilderComposer;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;

@Configuration
@Import(AclConfiguration.class)
@AutoConfigureAfter(AclConfiguration.class)
@EnableElasticsearchRepositories(
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
public class ElasticSearchAclConfiguration {

  private ElasticSearchFeature elasticSearchFeature = new ElasticSearchFeature();
  private Logger logger = LoggerFactory.getLogger(ElasticSearchAclConfiguration.class);

  @Bean
  public ElasticSearchFeature elasticSearchFeature() {
    logger.info("Configured feature : {}", elasticSearchFeature);
    return elasticSearchFeature;
  }

  @Bean
  @ConditionalOnMissingBean(FilterBuilderComposer.class)
  public FilterBuilderComposer filterBuilderComposer(
      AclComposersRegistry registry) {
    FilterBuilderComposer composer = new FilterBuilderComposer();
    registry.register(elasticSearchFeature, composer);
    return composer;
  }

  @Bean
  public AclFilterProvider aclFilterProvider(AclStrategyProvider strategyProvider) {
    return new AclFilterProvider(strategyProvider, elasticSearchFeature);
  }
}
