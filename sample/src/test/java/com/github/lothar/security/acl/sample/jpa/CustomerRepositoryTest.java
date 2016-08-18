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
package com.github.lothar.security.acl.sample.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.sample.SampleApplication;
import com.github.lothar.security.acl.sample.domain.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApplication.class)
@Transactional
public class CustomerRepositoryTest {

  @Resource
  private CustomerRepository repository;
  @Resource
  private SimpleAclStrategy customerStrategy;
  @Resource
  private JpaSpecFeature<Customer> jpaSpecFeature;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Before
  public void init() {
    repository.save(new Customer("Alice", "Smith"));
    repository.save(new Customer("Bob", "Smith"));
    repository.save(new Customer("John", "Doe"));
    logger.info("Customer strategy : {}", customerStrategy);
  }

  @Test
  public void should_customer_spec_be_registered_in_customer_strategy() {
    Specification<Customer> customerSpec = customerStrategy.handlerFor(jpaSpecFeature);
    assertThat(customerSpec) //
        .as("Customer ACL JPA specification not registered") //
        .isNotNull();
  }

  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(repository.count()).isEqualTo(2);
  }

  @Test
  public void should_find_all_customers_only_when_strategy_not_applied() {
    doWithoutCustomerSpec(new Runnable() {
      @Override
      public void run() {
        assertThat(repository.count()).isEqualTo(3);
      }
    });
  }

  private void doWithoutCustomerSpec(Runnable runnable) {
    Specification<Customer> customerSpec = customerStrategy.uninstall(jpaSpecFeature);
    try {
      runnable.run();
    } finally {
      customerStrategy.install(jpaSpecFeature, customerSpec);
    }
  }

  @Test
  public void should_not_find_members_of_Doe_family_when_strategy_applied() {
    assertThat(repository.findByLastName("Doe")).isEmpty();
  }

}
