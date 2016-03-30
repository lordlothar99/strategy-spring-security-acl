package com.github.lothar.security.acl.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class AclJpaSpecProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider aclStrategyProvider;
  private SpecificationFeature specificationFeature;

  public AclJpaSpecProvider(AclStrategyProvider aclStrategyProvider,
      SpecificationFeature specificationFeature) {
    super();
    this.aclStrategyProvider = aclStrategyProvider;
    this.specificationFeature = specificationFeature;
  }

  public <T> Specification<T> aclJpaSpecificationFor(Class<T> domainType) {
    AclStrategy strategy = aclStrategyProvider.strategyFor(domainType);
    Specification<T> aclJpaSpec = strategy.featureFor(specificationFeature);
    logger.debug("Using ACL JPA specification on {} : {}", domainType.getSimpleName(), strategy);
    return aclJpaSpec;
  }
}
