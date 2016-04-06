/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.github.lothar.security.acl.elasticsearch;

import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;

@SpringBootApplication
@EnableElasticsearchRepositories(value = "com.github.lothar.security.acl.elasticsearch.repository",
repositoryFactoryBeanClass = ElasticsearchRepositoryFactoryBean.class)
public class ElasticSearchTestConfiguration {

  private SimpleAclStrategy customerStrategy = new SimpleAclStrategy();

  @Bean
  public AclStrategy withoutHandlerStrategy() {
    return new SimpleAclStrategy();
  }

  @Bean
  public SimpleAclStrategy customerStrategy() {
    return customerStrategy;
  }

  @Bean
  public TermFilterBuilder smithFamilyFilter(ElasticSearchFeature elasticSearchFeature) {
    TermFilterBuilder smithFamilyFilter = termFilter("lastName", "Smith");
    customerStrategy.install(elasticSearchFeature, smithFamilyFilter);
    return smithFamilyFilter;
  }
}
