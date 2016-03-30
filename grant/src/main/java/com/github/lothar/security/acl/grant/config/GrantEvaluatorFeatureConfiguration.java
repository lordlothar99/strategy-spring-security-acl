package com.github.lothar.security.acl.grant.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclFeatureComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeature;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeatureComposer;
import com.github.lothar.security.acl.grant.PermissionEvaluatorImpl;

@Configuration
@AutoConfigureAfter(AclConfiguration.class)
public class GrantEvaluatorFeatureConfiguration {

  private GrantEvaluatorFeature grantEvaluatorFeature = new GrantEvaluatorFeature();

  @Bean
  public GrantEvaluatorFeature grantEvaluatorFeature() {
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
