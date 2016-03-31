package com.github.lothar.security.acl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.compound.AclStrategyComposer;

@Configuration
public class AclConfiguration {

  @Bean
  @ConditionalOnMissingBean(AclStrategyComposer.class)
  public AclStrategyComposer strategyComposer(AclComposersRegistry aclComposersRegistry) {
    return new AclStrategyComposer(aclComposersRegistry);
  }

  @Bean
  @ConditionalOnMissingBean(AclComposersRegistry.class)
  public AclComposersRegistry aclComposersRegistry() {
    return new AclComposersRegistry();
  }

  @Bean
  @ConditionalOnMissingBean(AclStrategyProvider.class)
  public AclStrategyProvider aclStrategyProvider() {
    return new AclStrategyProvider();
  }

  @Bean
  public AclStrategy allowAllStrategy() {
    return new SimpleAclStrategy();
  }

  @Bean
  public AclStrategy denyAllStrategy() {
    return new SimpleAclStrategy();
  }
}
