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
package com.github.lothar.security.acl.config;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.AclStrategyProviderImpl;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.activation.AclActivationUtils;
import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.compound.AclStrategyComposer;
import com.github.lothar.security.acl.compound.AclStrategyComposerProvider;

@Configuration
@EnableConfigurationProperties(AclProperties.class)
public class AclConfiguration {

  private SimpleAclStrategy allowAllStrategy = new SimpleAclStrategy();
  @Resource
  private ApplicationContext applicationContext;
  private Logger logger = LoggerFactory.getLogger(AclConfiguration.class);

  @Bean
  public AclStrategyComposer strategyComposer(
      AclStrategyComposerProvider strategyComposerProvider) {
    return new AclStrategyComposer(strategyComposerProvider);
  }

  @Bean
  @ConditionalOnMissingBean(AclStrategyComposerProvider.class)
  public AclStrategyComposerProvider aclComposersRegistry() {
    return new AclComposersRegistry();
  }

  @Bean
  @ConditionalOnMissingBean(AclStrategyProvider.class)
  public AclStrategyProvider aclStrategyProvider(AclProperties properties,
      AclSecurityActivator aclSecurityActivator) {
    return new AclStrategyProviderImpl(properties, aclSecurityActivator, allowAllStrategy);
  }

  @Bean
  public BeanPostProcessor aclStrategyPostProcessor(AclStrategy defaultAclStrategy) {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessBeforeInitialization(Object bean, String beanName)
          throws BeansException {
        if (bean instanceof AclStrategyProvider) {
          AclStrategyProvider aclStrategyProvider = (AclStrategyProvider) bean;
          aclStrategyProvider.setDefaultStrategy(defaultAclStrategy);
          logger.debug("Set defaultAclStrategy : {}", defaultAclStrategy);
        }
        return bean;
      }

      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName)
          throws BeansException {
        return bean;
      }
    };
  }

  @Bean
  public SimpleAclStrategy allowAllStrategy() {
    return allowAllStrategy;
  }

  @Bean
  @ConditionalOnMissingBean(name = "defaultAclStrategy")
  public SimpleAclStrategy defaultAclStrategy() {
    return allowAllStrategy;
  }

  @Bean
  public SimpleAclStrategy denyAllStrategy() {
    return new SimpleAclStrategy();
  }

  @EventListener(ContextRefreshedEvent.class)
  public void logStrategies() {
    if (logger.isDebugEnabled()) {
      Map<String, AclStrategy> strategies = applicationContext.getBeansOfType(AclStrategy.class);
      strategies.entrySet().stream() //
          .forEach(e -> logger.debug("Strategy {}: {}", e.getKey(), e.getValue()));
    }
  }

  @Bean
  public AclSecurityActivator aclSecurityActivator(AclProperties properties) {
    return new AclSecurityActivator(!properties.isDisabled());
  }

  @Bean
  public AclActivationUtils aclActivationUtils(AclSecurityActivator aclSecurityActivator) {
    return new AclActivationUtils(aclSecurityActivator);
  }
}
