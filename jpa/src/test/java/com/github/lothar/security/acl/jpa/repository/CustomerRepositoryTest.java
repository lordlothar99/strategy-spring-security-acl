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
package com.github.lothar.security.acl.jpa.repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.jpa.JpaSpecTestConfiguration;
import com.github.lothar.security.acl.jpa.domain.Customer;
import com.github.lothar.security.acl.jpa.spec.CustomerSpecification;

/**
 * Test {@link CustomerRepository} with {@link CustomerSpecification} installed.
 * Only Smith family should be visible.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSpecTestConfiguration.class)
@Transactional
public class CustomerRepositoryTest {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private CustomerRepository repository;
  @Resource
  private SimpleAclStrategy customerStrategy;
  @Resource
  private JpaSpecFeature<Customer> jpaSpecFeature;
  @Resource
  private CustomerSpecification customerSpec;
  private Customer aliceSmith;
  private Customer bobSmith;
  private Customer johnDoe;

  @Before
  public void init() {
    aliceSmith = repository.save(new Customer("Alice", "Smith"));
    bobSmith = repository.save(new Customer("Bob", "Smith"));
    johnDoe = repository.save(new Customer("John", "Doe"));
    logger.info("Customer strategy : {}", customerStrategy);
  }

  @Test
  public void should_customer_spec_be_registered_in_customer_strategy() {
    Specification<Customer> customerSpec = customerStrategy.handlerFor(jpaSpecFeature);
    assertThat(customerSpec) //
        .as("Customer ACL JPA specification not registered") //
        .isNotNull();
  }

  // count

  @Test
  public void should_count_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  public void should_count_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerSpec(() -> assertThat(repository.count()).isEqualTo(3));
  }

  @Test
  public void should_not_count_members_of_Doe_family_with_method_query() {
    assertThat(repository.countByLastName("Doe")).isEqualTo(0);
  }

  @Test
  public void should_count_members_of_Smith_family_with_method_query() {
    assertThat(repository.countByLastName("Smith")).isEqualTo(2);
  }

  // acl disabled on specific query

  @Test
  public void should_count_all_customers_when_NoAcl_annotation_is_present() {
    assertThat(repository.countByLastNameContains("Doe")).isEqualTo(1);
    assertThat(repository.countByLastNameContains("Smith")).isEqualTo(2);
  }

  // exist

  @Test
  public void should_not_say_exist_members_of_Doe_family_with_method_query() {
    assertThat(repository.exists(johnDoe.getId())).isFalse();
  }

  @Test
  public void should_say_exist_members_of_Smith_family_with_method_query() {
    assertThat(repository.exists(aliceSmith.getId())).isTrue();
  }

  // findAll

  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.findAll()).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_find_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerSpec(() -> assertThat(repository.findAll()).containsOnly(aliceSmith, bobSmith, johnDoe));
  }

  @Test
  public void should_find_authorized_customers_using_specific_ids_only_when_strategy_applied() {
    assertThat(repository.findAll(customerIds())).containsOnly(aliceSmith, bobSmith);
  }

  @Test
  public void should_find_all_customers_using_specific_ids_only_when_strategy_not_applied() {
    doWithoutCustomerSpec(() -> assertThat(repository.findAll(customerIds())).containsOnly(aliceSmith, bobSmith, johnDoe));
  }

  // findByLastName

  @Test
  public void should_not_find_members_of_Doe_family_with_method_query() {
    assertThat(repository.findByLastName("Doe")).isEmpty();
  }

  @Test
  public void should_find_members_of_Smith_family_with_method_query() {
    assertThat(repository.findByLastName("Smith")).containsOnly(aliceSmith, bobSmith);
  }

  // findByFirstName

  @Test
  public void should_not_find_members_of_Doe_family_by_first_name_with_method_query() {
    assertThat(repository.findByFirstName("John")).isNull();
  }

  @Test
  public void should_find_members_of_Smith_family_by_first_name_with_method_query() {
    assertThat(repository.findByFirstName("Bob")).isSameAs(bobSmith);
  }

  // findOne

  @Test
  public void should_not_findOne_member_of_Doe_family_with_method_query() {
    assertThat(repository.findOne(johnDoe.getId())).isNull();
  }

  @Test
  public void should_findOne_member_of_Smith_family_with_method_query() {
    assertThat(repository.findOne(aliceSmith.getId())).isSameAs(aliceSmith);
  }

  // getOne

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void should_not_getOne_member_of_Doe_family_with_method_query() {
    assertThat(repository.getOne(johnDoe.getId())).isNull();
  }

  @Test
  public void should_getOne_member_of_Smith_family_with_method_query() {
    assertThat(repository.getOne(aliceSmith.getId())).isSameAs(aliceSmith);
  }

  @Test
  public void should_create_predicate_when_query_method_invoked() {
    Specification<Customer> customerSpec = customerStrategy.handlerFor(jpaSpecFeature);
    Specification<Customer> spy = Mockito.spy(customerSpec);
    customerStrategy.install(jpaSpecFeature, spy);
    try {
      repository.countByLastName("Smith");
      repository.findByFirstName("John");
      repository.findByLastName("Smith");
      verify(spy, times(3)).toPredicate(any(), any(), any());
    } finally {
      customerStrategy.install(jpaSpecFeature, customerSpec);
    }
  }

  @Test
  public void should_handle_dynamic_specifications() {
    assertThat(repository.findByLastName("Smith")).hasSize(2);
    assertThat(repository.findByLastName("Doe")).hasSize(0);
    customerSpec.setLastName("Doe");
    try {
      assertThat(repository.findByLastName("Smith")).hasSize(0);
      assertThat(repository.findByLastName("Doe")).hasSize(1);
    } finally {
      customerSpec.setLastName("Smith");
    }
  }

  // findBy ... with Sort

  @Test
  @Ignore
  public void should_find_members_of_Smith_family_with_sortable_query_method() {
    assertThat(repository.findByFirstNameContains("o", new Sort("id"))).containsOnly(bobSmith);
  }

  @Test
  @Ignore
  public void should_find_members_of_Smith_family_with_pageable_query_method() {
    assertThat(repository.findByFirstNameContains("o", new PageRequest(0, 10))).containsOnly(bobSmith);
  }

  // utils

  private void doWithoutCustomerSpec(Runnable runnable) {
    Specification<Customer> customerSpec = customerStrategy.uninstall(jpaSpecFeature);
    try {
      runnable.run();
    } finally {
      customerStrategy.install(jpaSpecFeature, customerSpec);
    }
  }

  private Iterable<String> customerIds() {
    return asList(aliceSmith.getId(), bobSmith.getId(), johnDoe.getId());
  }

}
