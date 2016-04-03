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
package com.github.lothar.security.acl.grant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.grant.AclPermissionEvaluator;
import com.github.lothar.security.acl.grant.GrantEvaluator;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeature;
import com.github.lothar.security.acl.grant.compound.GrantEvaluatorComposer;
import com.github.lothar.security.acl.grant.evaluators.AllowAllGrantEvaluator;
import com.github.lothar.security.acl.grant.evaluators.DenyAllGrantEvaluator;

@Configuration
@Import(AclConfiguration.class)
public class GrantEvaluatorAclConfiguration {

  private GrantEvaluatorFeature grantEvaluatorFeature = new GrantEvaluatorFeature();
  private Logger logger = LoggerFactory.getLogger(GrantEvaluatorAclConfiguration.class);

  public GrantEvaluatorAclConfiguration() {
    logger.info("Configured feature : {}", grantEvaluatorFeature);
  }

  @Bean
  public GrantEvaluatorFeature grantEvaluatorFeature() {
    return grantEvaluatorFeature;
  }

  @Bean
  @ConditionalOnMissingBean(GrantEvaluatorComposer.class)
  public GrantEvaluatorComposer grantEvaluatorComposer(AclComposersRegistry registry) {
    GrantEvaluatorComposer composer = new GrantEvaluatorComposer();
    registry.register(grantEvaluatorFeature, composer);
    return composer;
  }

  @Bean
  public AclPermissionEvaluator aclPermissionEvaluator(AclStrategyProvider strategyProvider,
      GrantEvaluator defaultGrantEvaluator) {
    return new AclPermissionEvaluator(strategyProvider, grantEvaluatorFeature,
        defaultGrantEvaluator);
  }

  @Bean(name = {"allowAllGrantEvaluator", "defaultGrantEvaluator"})
  public GrantEvaluator allowAllGrantEvaluator(SimpleAclStrategy allowAllStrategy) {
    GrantEvaluator allowAllGrantEvaluator = new AllowAllGrantEvaluator();
    allowAllStrategy.install(grantEvaluatorFeature, allowAllGrantEvaluator);
    return allowAllGrantEvaluator;
  }

  @Bean
  public GrantEvaluator denyAllGrantEvaluator(SimpleAclStrategy denyAllStrategy) {
    GrantEvaluator denyAllGrantEvaluator = new DenyAllGrantEvaluator();
    denyAllStrategy.install(grantEvaluatorFeature, denyAllGrantEvaluator);
    return denyAllGrantEvaluator;
  }

}
