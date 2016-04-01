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
package com.github.lothar.security.acl.jpa.repository;

import static com.github.lothar.security.acl.jpa.spec.AclJpaSpecifications.idsIn;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.github.lothar.security.acl.jpa.JpaSpecProvider;

public class AclJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private JpaSpecProvider<T> jpaSpecProvider;

  public AclJpaRepository(Class<T> domainClass, EntityManager em,
      JpaSpecProvider<T> jpaSpecProvider) {
    super(domainClass, em);
    this.jpaSpecProvider = jpaSpecProvider;
  }

  // reflection invocation by
  // com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean.Factory.getTargetRepository(RepositoryInformation,
  // EntityManager)
  public AclJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager,
      JpaSpecProvider<T> jpaSpecProvider) {
    super(entityInformation, entityManager);
    this.jpaSpecProvider = jpaSpecProvider;
  }

  @Override
  public List<T> findAll() {
    return super.findAll(aclJpaSpec());
  }

  @Override
  public List<T> findAll(Iterable<ID> ids) {
    return this.findAll(idsIn(ids));
  }

  @Override
  public List<T> findAll(Sort sort) {
    return super.findAll(aclJpaSpec(), sort);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return super.findAll(aclJpaSpec(), pageable);
  }

  @Override
  public List<T> findAll(Specification<T> spec) {
    return super.findAll(aclJpaSpec().and(spec));
  }

  @Override
  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return super.findAll(aclJpaSpec().and(spec), pageable);
  }

  @Override
  public List<T> findAll(Specification<T> spec, Sort sort) {
    return super.findAll(aclJpaSpec().and(spec), sort);
  }

  @Override
  public long count() {
    return super.count(aclJpaSpec());
  }

  @Override
  public long count(Specification<T> spec) {
    return super.count(aclJpaSpec().and(spec));
  }

  private Specifications<T> aclJpaSpec() {
    Specification<T> spec = jpaSpecProvider.jpaSpecFor(getDomainClass());
    logger.debug("Using ACL JPA specification for objects '{}': {}",
        getDomainClass().getSimpleName(), spec);
    return where(spec);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "<" + getDomainClass().getSimpleName() + ">";
  }
}
