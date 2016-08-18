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

import static com.github.lothar.security.acl.jpa.spec.AclJpaSpecifications.idEqualTo;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public long count() {
    return super.count((Specification<T>) null);
  }

  @Override
  public boolean exists(ID id) {
    return findOne(id) != null;
  }

  @Override
  public T findOne(ID id) {
    return super.findOne(idEqualTo(id));
  }

  @Override
  public T getOne(ID id) {
    T entity = this.findOne(id);
    if (entity == null) {
      throw new EntityNotFoundException("Unable to find " + getDomainClass().getName() + " with id " + id);
    }
    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected <S extends T> TypedQuery<Long> getCountQuery(Specification<S> spec,
      Class<S> domainClass) {
    return super.getCountQuery(((Specifications<S>)aclJpaSpec()).and(spec), domainClass);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected <S extends T> TypedQuery<S> getQuery(Specification<S> spec, Class<S> domainClass,
        Sort sort) {
    return super.getQuery(((Specifications<S>) aclJpaSpec()).and(spec), domainClass, sort);
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
