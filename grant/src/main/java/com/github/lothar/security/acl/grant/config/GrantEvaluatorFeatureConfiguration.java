package com.github.lothar.security.acl.grant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclFeatureComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeature;
import com.github.lothar.security.acl.grant.PermissionEvaluatorImpl;
import com.github.lothar.security.acl.grant.compound.GrantEvaluatorFeatureComposer;

@Configuration
@Import(AclConfiguration.class)
@AutoConfigureAfter(AclConfiguration.class)
public class GrantEvaluatorFeatureConfiguration {

  private GrantEvaluatorFeature grantEvaluatorFeature = new GrantEvaluatorFeature();
  private Logger logger = LoggerFactory.getLogger(GrantEvaluatorFeatureConfiguration.class);

  @Bean
  public GrantEvaluatorFeature grantEvaluatorFeature() {
    logger.info("Installed feature : {}", grantEvaluatorFeature.getClass().getName());
    return grantEvaluatorFeature;
  }

  @Bean
  @ConditionalOnMissingBean(GrantEvaluatorFeatureComposer.class)
  public GrantEvaluatorFeatureComposer grantEvaluatorFeatureComposer(
      AclFeatureComposersRegistry registry) {
    GrantEvaluatorFeatureComposer composer = new GrantEvaluatorFeatureComposer();
    registry.register(grantEvaluatorFeature, composer);
    return composer;
  }

  @Bean
  public PermissionEvaluatorImpl aclPermissionEvaluator(AclStrategyProvider strategyProvider) {
    return new PermissionEvaluatorImpl(strategyProvider, grantEvaluatorFeature);
  }
}
