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
package com.github.lothar.security.acl.elasticsearch.repository;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.github.lothar.security.acl.Acl;
import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;

public class AclElasticsearchRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends ElasticsearchRepositoryFactoryBean<T, S, ID> {

  private ElasticsearchOperations operations;
  @Resource
  private AclFilterProvider filterProvider;

  public void setElasticsearchOperations(ElasticsearchOperations operations) {
    super.setElasticsearchOperations(operations);
    this.operations = operations;
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory() {
    return new Factory(operations);
  }

  private class Factory extends ElasticsearchRepositoryFactory {

    private ElasticsearchOperations elasticsearchOperations;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Factory(ElasticsearchOperations elasticsearchOperations) {
      super(elasticsearchOperations);
      this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return !hasAclStrategyAnnotation(metadata.getDomainType()) //
          ? super.getRepositoryBaseClass(metadata) //
          : AclElasticsearchRepository.class;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
      Class<?> domainType = metadata.getDomainType();
      ElasticsearchEntityInformation<?, Serializable> entityInformation =
          getEntityInformation(domainType);
      if (!hasAclStrategyAnnotation(domainType)) {
        return getTargetRepositoryViaReflection(metadata, entityInformation,
            elasticsearchOperations);
      }

      // invokes
      // com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepository.AclNumberKeyedRepository(ElasticsearchEntityInformation<T,
      // ID>, ElasticsearchOperations, AclFilterProvider)
      ElasticsearchRepository<?, ?> repository = getTargetRepositoryViaReflection(metadata,
          entityInformation, elasticsearchOperations, filterProvider);
      logger.debug("Created {}", repository);

      return repository;
    }

    private boolean hasAclStrategyAnnotation(Class<?> domainType) {
      return domainType.getAnnotation(Acl.class) != null;
    }
  }
}
