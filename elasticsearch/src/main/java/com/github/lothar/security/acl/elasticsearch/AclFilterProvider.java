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
package com.github.lothar.security.acl.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class AclFilterProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider strategyProvider;
  private ElasticSearchFeature elasticSearchFeature;
  private QueryBuilder defaultQueryBuilder;

  public AclFilterProvider(AclStrategyProvider strategyProvider,
      ElasticSearchFeature elasticSearchFeature, QueryBuilder defaultQueryBuilder) {
    super();
    this.strategyProvider = strategyProvider;
    this.elasticSearchFeature = elasticSearchFeature;
    this.defaultQueryBuilder = defaultQueryBuilder;
  }

  public QueryBuilder filterFor(Class<?> domainType) {
    QueryBuilder filterBuilder = defaultQueryBuilder;

    AclStrategy strategy = strategyProvider.strategyFor(domainType);
    if (strategy == null) {
      logger.debug("No strategy found for '{}' in strategy provider", domainType.getSimpleName());

    } else {
      QueryBuilder filter = strategy.handlerFor(elasticSearchFeature);
      if (filter == null) {
        logger.debug(
            "No ACL ElasticSearch found in strategy {} > fall back on default ACL ElasticSearch specification",
            strategy);
      } else {
        filterBuilder = filter;
      }
    }

    logger.debug("Using ACL ElasticSearch filter builder for {} using strategy {}: {}",
        domainType.getSimpleName(), strategy, filterBuilder);
    return filterBuilder;
  }

}
