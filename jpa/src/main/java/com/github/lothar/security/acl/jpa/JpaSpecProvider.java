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
package com.github.lothar.security.acl.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class JpaSpecProvider<T> {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider strategyProvider;
  private JpaSpecFeature<T> jpaSpecFeature;

  public JpaSpecProvider(AclStrategyProvider strategyProvider,
      JpaSpecFeature<T> jpaSpecFeature) {
    super();
    this.strategyProvider = strategyProvider;
    this.jpaSpecFeature = jpaSpecFeature;
  }

  public Specification<T> jpaSpecFor(Class<? extends T> domainType) {
    AclStrategy strategy = strategyProvider.strategyFor(domainType);
    Specification<T> aclJpaSpec = strategy.handlerFor(jpaSpecFeature);

    // TODO implement default aclJpaSpec

    logger.debug("Using ACL JPA specification for '{}' using strategy '{}': {}",
        domainType.getSimpleName(), strategy, aclJpaSpec);
    return aclJpaSpec;
  }
}
