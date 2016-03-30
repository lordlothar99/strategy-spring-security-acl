package com.github.lothar.security.acl.jpa.repository;

import java.io.Serializable;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.github.lothar.security.acl.Acl;
import com.github.lothar.security.acl.jpa.JpaSpecProvider;

public class AclJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends JpaRepositoryFactoryBean<T, S, ID> {

  @Resource
  private JpaSpecProvider jpaSpecProvider;

  protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
    return new AclJpaRepositoryFactory(entityManager);
  }

  private class AclJpaRepositoryFactory extends JpaRepositoryFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AclJpaRepositoryFactory(EntityManager entityManager) {
      super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return !hasAclStrategyAnnotation(metadata.getDomainType()) //
          ? super.getRepositoryBaseClass(metadata) //
          : AclJpaRepository.class;
    }

    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information,
        EntityManager entityManager) {
      Class<?> domainType = information.getDomainType();
      if (!hasAclStrategyAnnotation(domainType)) {
        return super.getTargetRepository(information, entityManager);
      }

      JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(domainType);

      Supplier<Specification<?>> aclJpaSpecSupplier =
          () -> jpaSpecProvider.aclJpaSpecificationFor(domainType);

      // invokes
      // com.trackaflat.repository.AclJpaRepository.AclJpaRepository(JpaEntityInformation<T, ?>,
      // EntityManager, Specification<T>)
      SimpleJpaRepository<?, ?> repository = getTargetRepositoryViaReflection(information,
          entityInformation, entityManager, aclJpaSpecSupplier);
      logger.debug("Created AclJpaRepository {}", repository);

      return repository;
    }

    private boolean hasAclStrategyAnnotation(Class<?> domainType) {
      return domainType.getAnnotation(Acl.class) != null;
    }
  }

}
