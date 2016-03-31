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
          : AclNumberKeyedRepository.class;
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
      // com.github.lothar.security.acl.elasticsearch.repository.AclNumberKeyedRepository.AclNumberKeyedRepository(ElasticsearchEntityInformation<T,
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
