package com.github.lothar.security.acl.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class JpaSpecProvider<T> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider aclStrategyProvider;
  private JpaSpecFeature<T> jpaSpecFeature;

  public JpaSpecProvider(AclStrategyProvider aclStrategyProvider,
      JpaSpecFeature<T> jpaSpecFeature) {
    super();
    this.aclStrategyProvider = aclStrategyProvider;
    this.jpaSpecFeature = jpaSpecFeature;
  }

  public Specification<T> aclJpaSpecificationFor(Class<T> domainType) {
    AclStrategy strategy = aclStrategyProvider.strategyFor(domainType);
    Specification<T> aclJpaSpec = strategy.filterFor(jpaSpecFeature);

    // TODO implement default aclJpaSpec

    logger.debug("Using ACL JPA specification for '{}' using strategy '{}': {}",
        domainType.getSimpleName(), strategy, aclJpaSpec);
    return aclJpaSpec;
  }
}
