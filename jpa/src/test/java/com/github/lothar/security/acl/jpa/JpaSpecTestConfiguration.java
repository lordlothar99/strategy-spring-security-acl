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
package com.github.lothar.security.acl.jpa;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.jpa.domain.Customer;
import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;
import com.github.lothar.security.acl.jpa.spec.AllowAllSpecification;

@SpringBootApplication
@EnableJpaRepositories(value = "com.github.lothar.security.acl.jpa.repository",
    repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class)
public class JpaSpecTestConfiguration {

  @Resource
  private SimpleAclStrategy allowAllStrategy;
  @Resource
  private AllowAllSpecification<?> allowAllSpecification;
  @Resource
  private JpaSpecFeature<Customer> jpaSpecFeature;
  private SimpleAclStrategy customerStrategy = new SimpleAclStrategy();
  private Specification<Customer> smithFamilySpec = new Specification<Customer>() {
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query,
        CriteriaBuilder cb) {
      return cb.equal(root.get("lastName"), "Smith");
    }
  };

  @Bean
  public AclStrategy withoutHandlerStrategy() {
    return new SimpleAclStrategy();
  }

  @Bean
  public AclStrategy customerStrategy() {
    return customerStrategy;
  }

  @Bean
  public Specification<Customer> smithFamilySpec() {
    return smithFamilySpec;
  }

  @PostConstruct
  public void installStrategy() {
    customerStrategy.install(jpaSpecFeature, smithFamilySpec);
  }
}
