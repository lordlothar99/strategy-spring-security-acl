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

import static org.springframework.util.ReflectionUtils.findMethod;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;

import com.github.lothar.security.acl.Acl;
import com.github.lothar.security.acl.jpa.JpaSpecProvider;
import com.github.lothar.security.acl.jpa.annotation.NoAcl;
import com.github.lothar.security.acl.jpa.query.AclJpaQuery;

public class AclJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends JpaRepositoryFactoryBean<T, S, ID> {

  @Resource
  private JpaSpecProvider<Object> jpaSpecProvider;

  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new Factory(entityManager);
  }

  private class Factory extends JpaRepositoryFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private EntityManager em;

    public Factory(EntityManager entityManager) {
      super(entityManager);
      em = entityManager;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return !hasAclStrategyAnnotation(metadata.getDomainType()) //
          ? super.getRepositoryBaseClass(metadata) //
          : AclJpaRepository.class;
    }

    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(Key key,
        EvaluationContextProvider evaluationContextProvider) {
      return new AclQueryLookupStrategy(key, evaluationContextProvider);
    }

    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information,
        EntityManager entityManager) {
      Class<?> domainType = information.getDomainType();
      if (!hasAclStrategyAnnotation(domainType)) {
        return super.getTargetRepository(information, entityManager);
      }

      JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(domainType);

      // invokes
      // com.github.lothar.security.acl.jpa.repository.AclJpaRepository.AclJpaRepository(JpaEntityInformation<T,
      // ?>, EntityManager, JpaSpecProvider<T>)
      SimpleJpaRepository<?, ?> repository = getTargetRepositoryViaReflection(information,
          entityInformation, entityManager, jpaSpecProvider);
      logger.debug("Created {}", repository);

      return repository;
    }

    private boolean hasAclStrategyAnnotation(Class<?> domainType) {
      return domainType.getAnnotation(Acl.class) != null;
    }

    private class AclQueryLookupStrategy implements QueryLookupStrategy {
      private Key key;
      private EvaluationContextProvider evaluationContextProvider;

      public AclQueryLookupStrategy(Key key, EvaluationContextProvider evaluationContextProvider) {
        this.key = key;
        this.evaluationContextProvider = evaluationContextProvider;
      }

      /**
       * Compatibility for projects using Spring Data Jpa < 1.10.0
       */
      @SuppressWarnings("unused")
      public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata,
          NamedQueries namedQueries) {
        QueryLookupStrategy queryLookupStrategy =
            Factory.super.getQueryLookupStrategy(key, evaluationContextProvider);

        Method resolveQuery = findMethod(QueryLookupStrategy.class, "resolveQuery",
                Method.class, RepositoryMetadata.class, NamedQueries.class);
        try {
            RepositoryQuery query = (RepositoryQuery) resolveQuery.invoke(queryLookupStrategy, method, 
                    metadata, namedQueries);
            return wrapQuery(method, metadata, query);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
      }

      /**
       * @since Spring data JPA 1.10.0
       */
      public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata,
            ProjectionFactory factory, NamedQueries namedQueries) {
        QueryLookupStrategy queryLookupStrategy =
            Factory.super.getQueryLookupStrategy(key, evaluationContextProvider);

        RepositoryQuery query = queryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        return wrapQuery(method, metadata, query);
      }

      private RepositoryQuery wrapQuery(Method method, RepositoryMetadata metadata,
            RepositoryQuery query) {
        if (method.getDeclaredAnnotation(NoAcl.class) != null) {
          // no acl applied here
          return query;
        }
        if (query instanceof PartTreeJpaQuery) {
          query = new AclJpaQuery(method, query, metadata.getDomainType(), em, jpaSpecProvider);
        } else {
          logger.warn(
              "Unsupported query type for method '{}' > ACL Jpa Specification not installed: {}",
              method, query.getClass());
        }
        return query;
      }
    }
  }

}
