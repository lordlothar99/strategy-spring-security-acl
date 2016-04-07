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
package com.github.lothar.security.acl.jpa.query;

import static org.mockito.Mockito.verifyZeroInteractions;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.jpa.JpaSpecTestConfiguration;
import com.github.lothar.security.acl.jpa.domain.Customer;
import com.github.lothar.security.acl.jpa.repository.CustomerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JpaSpecTestConfiguration.class)
public class JpaQuerySpecInstallerTest {

  @Resource
  private Specification<Customer> smithFamilySpec;
  @Resource
  private CustomerRepository repository;
  @Resource
  private SimpleAclStrategy customerStrategy;
  @Resource
  private JpaSpecFeature<Customer> jpaSpecFeature;

  @Test
  public void should_not_create_predicate_when_query_method_invoked() {
    Specification<Customer> customerSpec = customerStrategy.handlerFor(jpaSpecFeature);
    Specification<Customer> spy = Mockito.spy(customerSpec);
    customerStrategy.install(jpaSpecFeature, spy);
    try {
      repository.countByLastName("Smith");
      repository.findByFirstName("John");
      repository.findByLastName("Smith");
      verifyZeroInteractions(spy);
    } finally {
      customerStrategy.install(jpaSpecFeature, customerSpec);
    }
  }

}
