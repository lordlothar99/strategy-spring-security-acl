package com.github.lothar.security.acl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclFeatureComposersRegistry;
import com.github.lothar.security.acl.compound.AclStrategyComposer;

@Configuration
public class AclConfiguration {

  @Bean
  @ConditionalOnMissingBean(AclStrategyComposer.class)
  public AclStrategyComposer strategyComposer(AclFeatureComposersRegistry aclFeatureComposersRegistry) {
    return new AclStrategyComposer(aclFeatureComposersRegistry);
  }

  @Bean
  @ConditionalOnMissingBean(AclFeatureComposersRegistry.class)
  public AclFeatureComposersRegistry aclFeatureComposersRegistry() {
    return new AclFeatureComposersRegistry();
  }

  @Bean
  @ConditionalOnMissingBean(AclStrategyProvider.class)
  public AclStrategyProvider aclStrategyProvider() {
    return new AclStrategyProvider();
  }
}
