/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.lothar.security.acl.elasticsearch.config;

import static org.elasticsearch.index.query.FilterBuilders.matchAllFilter;
import static org.elasticsearch.index.query.FilterBuilders.notFilter;

import javax.annotation.Resource;

import org.elasticsearch.index.query.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.FilterBuilderBean;
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
  @Resource
  private SimpleAclStrategy allowAllStrategy;
  @Resource
  private SimpleAclStrategy denyAllStrategy;

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

  @Bean
  public FilterBuilder allowAllFilter() {
    FilterBuilderBean allowAllFilter = new FilterBuilderBean(matchAllFilter());
    allowAllStrategy.install(elasticSearchFeature, allowAllFilter);
    return allowAllFilter;
  }

  @Bean
  public FilterBuilder denyAllFilter() {
    FilterBuilderBean denyAllFilter = new FilterBuilderBean(notFilter(matchAllFilter()));
    allowAllStrategy.install(elasticSearchFeature, denyAllFilter);
    return denyAllFilter;
  }
}
