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
package com.github.lothar.security.acl.elasticsearch.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import javax.annotation.Resource;

import org.elasticsearch.index.query.FilterBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchTestConfiguration;
import com.github.lothar.security.acl.elasticsearch.domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ElasticSearchTestConfiguration.class)
public class CustomerRepositoryTest {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private CustomerRepository repository;
  @Resource
  private SimpleAclStrategy customerStrategy;
  @Resource
  private ElasticSearchFeature elasticSearchFeature;
  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Before
  public void init() {
    repository.deleteAll();
    repository.save(new Customer("1", "Alice", "The Smith family"));
    repository.save(new Customer("2", "Bob", "The Smith family"));
    repository.save(new Customer("3", "John", "The Doe family"));
    repository.save(new Customer("4", "Alice", "The Doe family"));
    logger.info("Customer strategy : {}", customerStrategy);
  }

  @Test
  public void should_customer_spec_be_registered_in_customer_strategy() {
    FilterBuilder customerFilter = customerStrategy.handlerFor(elasticSearchFeature);
    assertThat(customerFilter) //
        .as("Customer filter not registered") //
        .isNotNull();
  }

  // findOne

  @Test
  public void test_findOne() {
    assertThat(repository.findOne("1")).isNotNull();
  }

  @Ignore("Fix me")
  @Test
  public void test_findOne_blocked() {
    assertThat(repository.findOne("3")).isNull();
  }

  // count

//  @Ignore("Fix me")
  @Test
  public void should_count_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  public void should_count_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.count()).isEqualTo(4);
      }
    });
  }

  // findall

  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  public void should_find_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.findAll()).hasSize(4);
      }
    });
  }

  // search

  @Ignore("Fix me")
  @Test
  public void should_search_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.search(matchAllQuery())).hasSize(2);
  }

  @Test
  public void should_search_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchAllQuery())).hasSize(4);
      }
    });
  }

  // search match

  @Test
  public void should_search_specific_customers_when_strategy_applied() {
    assertThat(repository.search(matchQuery("firstName", "Alice"))).hasSize(1);
  }

  @Test
  public void should_search_specific_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchQuery("firstName", "Alice"))).hasSize(2);
      }
    });
  }

  @Ignore("Fix me")
  @Test
  public void should_not_find_members_of_Doe_family_with_method_query() {
    assertThat(repository.findByLastName("Doe")).isEmpty();
  }

  @Test
  public void should_find_members_of_Smith_family_with_method_query() {
    assertThat(repository.findByLastName("Smith")).hasSize(2);
  }

  @Ignore("Fix me")
  @Test
  public void should_not_count_members_of_Doe_family_with_method_query() {
    assertThat(repository.countByLastName("Doe")).isEqualTo(0);
  }

  @Test
  public void should_count_members_of_Smith_family_with_method_query() {
    assertThat(repository.countByLastName("Smith")).isEqualTo(2);
  }

  private void doWithoutCustomerFilter(Runnable runnable) {
    FilterBuilder customerFilter = customerStrategy.uninstall(elasticSearchFeature);
    try {
      runnable.run();
    } finally {
      customerStrategy.install(elasticSearchFeature, customerFilter);
    }
  }

}
