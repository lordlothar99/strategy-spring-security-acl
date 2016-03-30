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
import com.github.lothar.security.acl.elasticsearch.FilterBuilderFeature;
import com.github.lothar.security.acl.elasticsearch.compound.FilterBuilderFeatureComposer;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;

@Configuration
@AutoConfigureAfter(AclConfiguration.class)
@EnableElasticsearchRepositories(
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
public class FilterBuilderFeatureConfiguration {

  private FilterBuilderFeature filterBuilderFeature = new FilterBuilderFeature();

  @Bean
  public FilterBuilderFeature filterBuilderFeature() {
    return filterBuilderFeature;
  }

  @Bean
  @ConditionalOnMissingBean(FilterBuilderFeatureComposer.class)
  public FilterBuilderFeatureComposer filterBuilderFeatureComposer(
      AclFeatureComposersRegistry registry) {
    FilterBuilderFeatureComposer composer = new FilterBuilderFeatureComposer();
    registry.register(filterBuilderFeature, composer);
    return composer;
  }

  @Bean
  public AclFilterProvider aclFilterProvider(AclStrategyProvider aclStrategyProvider) {
    return new AclFilterProvider(aclStrategyProvider, filterBuilderFeature);
  }
}
