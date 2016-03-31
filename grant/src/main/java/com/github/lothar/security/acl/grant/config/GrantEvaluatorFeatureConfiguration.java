package com.github.lothar.security.acl.grant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeature;
import com.github.lothar.security.acl.grant.PermissionEvaluatorImpl;
import com.github.lothar.security.acl.grant.compound.GrantEvaluatorComposer;

@Configuration
@Import(AclConfiguration.class)
@AutoConfigureAfter(AclConfiguration.class)
public class GrantEvaluatorFeatureConfiguration {

  private GrantEvaluatorFeature grantEvaluatorFeature = new GrantEvaluatorFeature();
  private Logger logger = LoggerFactory.getLogger(GrantEvaluatorFeatureConfiguration.class);

  @Bean
  public GrantEvaluatorFeature grantEvaluatorFeature() {
    logger.info("Configured feature : {}", grantEvaluatorFeature);
    return grantEvaluatorFeature;
  }

  @Bean
  @ConditionalOnMissingBean(GrantEvaluatorComposer.class)
  public GrantEvaluatorComposer grantEvaluatorComposer(
      AclComposersRegistry registry) {
    GrantEvaluatorComposer composer = new GrantEvaluatorComposer();
    registry.register(grantEvaluatorFeature, composer);
    return composer;
  }

  @Bean
  public PermissionEvaluatorImpl aclPermissionEvaluator(AclStrategyProvider strategyProvider) {
    return new PermissionEvaluatorImpl(strategyProvider, grantEvaluatorFeature);
  }
}
