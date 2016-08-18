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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import javax.annotation.Resource;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchTestConfiguration;
import com.github.lothar.security.acl.elasticsearch.domain.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchTestConfiguration.class)
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
  private Customer aliceSmith;
  private Customer bobSmith;
  private Customer johnDoe;
  private Customer aliceDoe;

  @Before
  public void init() {
    repository.deleteAll();
    aliceSmith = repository.save(new Customer("1", "Alice", "The Smith family"));
    bobSmith = repository.save(new Customer("2", "Bob", "The Smith family"));
    johnDoe = repository.save(new Customer("3", "John", "The Doe family"));
    aliceDoe = repository.save(new Customer("4", "Alice", "The Doe family"));
    logger.info("Customer strategy : {}", customerStrategy);
  }

  @Test
  public void should_customer_spec_be_registered_in_customer_strategy() {
    QueryBuilder customerFilter = customerStrategy.handlerFor(elasticSearchFeature);
    assertThat(customerFilter) //
        .as("Customer filter not registered") //
        .isNotNull();
  }

  // count

  @Test
  public void should_count_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  public void should_count_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.count()).isEqualTo(4);
      }
    });
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

  // exist

  @Test
  public void should_exists_consider_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.exists(aliceDoe.getId())).isFalse();
  }

  @Test
  public void should_exists_consider_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.exists(aliceDoe.getId())).isTrue();
      }
    });
  }

  // findAll

  @Test
  public void should_findAll_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll()).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_findAll_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.findAll()).containsOnly(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  @Test
  public void should_find_sorted_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll(new Sort("firstName"))).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_find_sorted_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.findAll(new Sort("firstName"))).containsExactly(aliceDoe, aliceSmith, bobSmith, johnDoe);
      }
    });
  }

  @Test
  public void should_findPageable_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll(new PageRequest(0, 4))).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_findPageable_retrieve__all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.findAll(new PageRequest(0, 4))).contains(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  @Test
  public void should_find_by_ids_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll(customerIds())).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_find_by_ids_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.findAll(customerIds())).contains(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  // findByLastName

  @Ignore("Fix me")
  @Test
  public void should_not_find_members_of_Doe_family_with_method_query() {
    assertThat(repository.findByLastName("Doe")).isEmpty();
  }

  @Test
  public void should_find_members_of_Smith_family_with_method_query() {
    assertThat(repository.findByLastName("Smith")).containsOnly(aliceSmith, bobSmith);
  }

  // findOne

  @Test
  public void should_findOne_consider_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findOne(aliceSmith.getId())).isEqualToComparingFieldByField(aliceSmith);
  }

  @Test
  public void should_findOne_consider_all_customers_when_strategy_not_applied() {
    assertThat(repository.findOne(johnDoe.getId())).isNull();
  }

  // search

  @Test
  public void should_searchQuery_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.search(matchAllQuery())).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_searchQuery_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchAllQuery())).containsOnly(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  @Test
  public void should_search_specific_customers_when_strategy_applied() {
    assertThat(repository.search(matchQuery("firstName", "Alice"))).containsOnly(aliceSmith);
  }

  @Test
  public void should_search_specific_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchQuery("firstName", "Alice"))).containsOnly(aliceSmith, aliceDoe);
      }
    });
  }

  @Test
  public void should_searchQueryPageable_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.search(matchAllQuery(), new PageRequest(0,  4))).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_searchQueryPageable_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchAllQuery(), new PageRequest(0,  4))).containsOnly(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  @Test
  public void should_searchPageable_specific_customers_when_strategy_applied() {
    assertThat(repository.search(matchQuery("firstName", "Alice"), new PageRequest(0,  4))).containsOnly(aliceSmith);
  }

  @Test
  public void should_searchPageable_specific_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(matchQuery("firstName", "Alice"), new PageRequest(0,  4))).containsOnly(aliceSmith, aliceDoe);
      }
    });
  }

  @Test
  public void should_search_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.search(new NativeSearchQuery(matchAllQuery()))).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_search_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.search(new NativeSearchQuery(matchAllQuery()))).containsOnly(aliceSmith, bobSmith, johnDoe, aliceDoe);
      }
    });
  }

  // searchSimilar

  @Ignore("fix me")
  @Test
  public void should_searchSimilar_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.searchSimilar(aliceSmith, new String[]{ "firstName" }, new PageRequest(0, 4))).containsOnly(aliceSmith);
  }

  @Ignore("doesn't work... I don't get it :(")
  @Test
  public void should_searchSimilar_retrieve_all_customers_when_strategy_not_applied() {
    doWithoutCustomerFilter(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.searchSimilar(aliceSmith, new String[]{ "firstName" }, new PageRequest(0, 4))).containsOnly(aliceSmith, aliceDoe);
      }
    });
  }

  private void doWithoutCustomerFilter(Runnable runnable) {
    QueryBuilder customerFilter = customerStrategy.uninstall(elasticSearchFeature);
    try {
      runnable.run();
    } finally {
      customerStrategy.install(elasticSearchFeature, customerFilter);
    }
  }

  private Iterable<String> customerIds() {
    return asList(aliceSmith.getId(), bobSmith.getId(), johnDoe.getId(), aliceDoe.getId());
  }

}
