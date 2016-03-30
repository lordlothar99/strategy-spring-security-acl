package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class AclFilterProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider aclStrategyProvider;
  private FilterBuilderFeature filterBuilderFeature;

  public AclFilterProvider(AclStrategyProvider aclStrategyProvider,
      FilterBuilderFeature filterBuilderFeature) {
    super();
    this.aclStrategyProvider = aclStrategyProvider;
    this.filterBuilderFeature = filterBuilderFeature;
  }

  public FilterBuilder filterFor(Class<?> domainType) {
    AclStrategy strategy = aclStrategyProvider.strategyFor(domainType);
    FilterBuilder filterBuilder = strategy.featureFor(filterBuilderFeature);
    logger.debug("Using ACL ElasticSearch filter builder on {} : {}", domainType.getSimpleName(),
        strategy);
    return filterBuilder;
  }

}
