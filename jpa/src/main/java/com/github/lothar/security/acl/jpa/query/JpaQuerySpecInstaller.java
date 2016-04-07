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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.ReflectionUtils;

import com.github.lothar.security.acl.jpa.JpaSpecProvider;

public class JpaQuerySpecInstaller {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Set<Integer> installedQueries = new HashSet<>();
  private JpaSpecProvider<Object> jpaSpecProvider;

  public JpaQuerySpecInstaller(JpaSpecProvider<Object> jpaSpecProvider) {
    this.jpaSpecProvider = jpaSpecProvider;
  }

  public void installAclSpec(Method method, RepositoryQuery query, Class<?> domainType,
      EntityManager em) {
    if (!(query instanceof PartTreeJpaQuery)) {
      logger.warn(
          "Unsupported query type for method '{}' > ACL Jpa Specification not installed: {}",
          method, query.getClass());
    } else if (isAlreadyInstalled(query)) {
      logger.debug("ACL Jpa Specification already installed for method '{}' and query {}", method,
          query);
    } else {
      doInstall(method, query, domainType, em);
    }
  }

  private void doInstall(Method method, RepositoryQuery query, Class<?> domainType,
      EntityManager em) {
    try {
      Object queryPreparer = getField(PartTreeJpaQuery.class, query, "query");
      CriteriaQuery<?> criteriaQuery =
          getField(queryPreparer.getClass(), queryPreparer, "cachedCriteriaQuery");
      Specification<Object> jpaSpec = jpaSpecProvider.jpaSpecFor(domainType);
      Predicate predicate =
          jpaSpec.toPredicate(root(criteriaQuery), criteriaQuery, em.getCriteriaBuilder());
      criteriaQuery.where(criteriaQuery.getRestriction(), predicate);
      installedQuery(query);
      logger.debug("ACL Jpa Specification installed for method '{}' and query {}: {}", method,
          query, jpaSpec);
    } catch (Exception e) {
      logger.warn(
          "Unable to install ACL Jpa Specification for method '" + method + "' and query: " + query,
          e);
    }
  }

  private void installedQuery(RepositoryQuery query) {
    installedQueries.add(query.hashCode());
  }

  private boolean isAlreadyInstalled(RepositoryQuery query) {
    return installedQueries.contains(query.hashCode());
  }

  @SuppressWarnings("unchecked")
  private Root<Object> root(CriteriaQuery<?> criteriaQuery) {
    return (Root<Object>) criteriaQuery.getRoots().iterator().next();
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Class<?> type, Object object, String fieldName) {
    Field field = ReflectionUtils.findField(type, fieldName);
    field.setAccessible(true);
    Object property = ReflectionUtils.getField(field, object);
    return (T) property;
  }

}
