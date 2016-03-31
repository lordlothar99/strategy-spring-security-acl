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
  // com.trackaflat.config.repository.AclJpaRepositoryFactoryBean.Factory.getTargetRepository(RepositoryInformation,
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
