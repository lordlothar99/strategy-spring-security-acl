package com.github.lothar.security.acl.elasticsearch.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclFeatureComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.compound.ElasticSearchFeatureComposer;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;

@Configuration
@AutoConfigureAfter(AclConfiguration.class)
@EnableElasticsearchRepositories(
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
public class ElasticSearchFeatureConfiguration {

  private ElasticSearchFeature elasticSearchFeature = new ElasticSearchFeature();

  @Bean
  public ElasticSearchFeature elasticSearchFeature() {
    return elasticSearchFeature;
  }

  @Bean
  @ConditionalOnMissingBean(ElasticSearchFeatureComposer.class)
  public ElasticSearchFeatureComposer elasticSearchFeatureComposer(
      AclFeatureComposersRegistry registry) {
    ElasticSearchFeatureComposer composer = new ElasticSearchFeatureComposer();
    registry.register(elasticSearchFeature, composer);
    return composer;
  }

  @Bean
  public AclFilterProvider aclFilterProvider(AclStrategyProvider aclStrategyProvider) {
    return new AclFilterProvider(aclStrategyProvider, elasticSearchFeature);
  }
}
