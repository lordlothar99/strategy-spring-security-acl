package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class AclFilterProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider aclStrategyProvider;
  private ElasticSearchFeature elasticSearchFeature;

  public AclFilterProvider(AclStrategyProvider aclStrategyProvider,
      ElasticSearchFeature elasticSearchFeature) {
    super();
    this.aclStrategyProvider = aclStrategyProvider;
    this.elasticSearchFeature = elasticSearchFeature;
  }

  public FilterBuilder filterFor(Class<?> domainType) {
    AclStrategy strategy = aclStrategyProvider.strategyFor(domainType);
    FilterBuilder filterBuilder = strategy.filterFor(elasticSearchFeature);

    // TODO implement default filterBuilder

    logger.debug("Using ACL ElasticSearch filter builder for {} using strategy {}: {}",
        domainType.getSimpleName(), strategy, filterBuilder);
    return filterBuilder;
  }

}
