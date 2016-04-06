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

import java.lang.reflect.Field;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.ReflectionUtils;

import com.github.lothar.security.acl.jpa.JpaSpecProvider;

public class AclJpaQuery implements RepositoryQuery {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private RepositoryQuery query;
  private JpaSpecProvider<Object> jpaSpecProvider;
  private Class<?> domainType;
  private EntityManager em;

  public AclJpaQuery(RepositoryQuery query, EntityManager em,
      JpaSpecProvider<Object> jpaSpecProvider, Class<?> domainType) {
    this.query = query;
    this.em = em;
    this.jpaSpecProvider = jpaSpecProvider;
    this.domainType = domainType;
  }

  @Override
  public Object execute(Object[] parameters) {
    if (query instanceof PartTreeJpaQuery) {
      installAclSpecification();
    } else {
      logger.warn("Unsupported query type, ACL Specification not installed: {}", query.getClass());
    }
    return query.execute(parameters);
  }

  @Override
  public QueryMethod getQueryMethod() {
    return query.getQueryMethod();
  }

  private void installAclSpecification() {
    try {
      Object queryPreparer = getField(PartTreeJpaQuery.class, query, "query");
      CriteriaQuery<?> criteriaQuery =
          getField(queryPreparer.getClass(), queryPreparer, "cachedCriteriaQuery");
      Specification<Object> jpaSpec = jpaSpecProvider.jpaSpecFor(domainType);
      CriteriaBuilder cb = em.getCriteriaBuilder();
      Predicate predicate = jpaSpec.toPredicate(root(criteriaQuery), criteriaQuery, cb);
      criteriaQuery.where(criteriaQuery.getRestriction(), predicate);
    } catch (Exception e) {
      logger.warn("Unable to install ACL Jpa Specification for query: " + query, e);
    }
  }

  @SuppressWarnings("unchecked")
  private Root<Object> root(CriteriaQuery<?> criteriaQuery) {
    return (Root<Object>) criteriaQuery.getRoots().iterator().next();
  }

  @SuppressWarnings("unchecked")
  private static <T> T getField(Class<?> type, Object object, String fieldName) {
    Field field = ReflectionUtils.findField(type, fieldName);
    field.setAccessible(true);
    Object property = ReflectionUtils.getField(field, object);
    return (T) property;
  }


}
