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
package com.github.lothar.security.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.config.AclProperties;

public class AclStrategyProviderImpl implements BeanFactoryAware, AclStrategyProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategy defaultStrategy;
  private BeanFactory beanFactory;
  private AclProperties properties;
  private AclSecurityActivator activator;
  private AclStrategy allowAllStrategy;

  public AclStrategyProviderImpl(AclProperties properties, AclSecurityActivator activator,
      AclStrategy allowAllStrategy) {
    this.properties = properties;
    this.activator = activator;
    this.allowAllStrategy = allowAllStrategy;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public void setDefaultStrategy(AclStrategy strategy) {
    defaultStrategy = strategy;
  }

  @Override
  public AclStrategy strategyFor(Class<?> entityClass) {

    AclStrategy strategy = defaultStrategy;

    if (activator.isDisabled()) {
      logger.debug("ACL disabled");
      strategy = allowAllStrategy;
    } else {
      String strategyBeanName = strategyBeanName(entityClass);
      strategy = useIfPresent(loadStrategyBean(strategyBeanName));
    }
    logger.debug("Using acl strategy on '{}' : {}", entityClass.getSimpleName(), strategy);
    return strategy;
  }

  private String strategyBeanName(Class<?> entityClass) {
    String strategyBeanName = properties.getOverrideStrategy();
    if (strategyBeanName != null) {
      logger.debug("Using override strategy: {}", strategyBeanName);
      return strategyBeanName;
    }

    Acl acl = entityClass.getAnnotation(Acl.class);
    if (acl != null) {
      strategyBeanName = acl.value();
      logger.debug("{} annotation found on '{}', indicating strategy {}", Acl.class.getName(),
          entityClass.getSimpleName(), strategyBeanName);
    } else {
      logger.debug("No {} annotation found on '{}' > fall back on default strategy",
          Acl.class.getName(), entityClass.getSimpleName());
    }
    return strategyBeanName;
  }

  private AclStrategy loadStrategyBean(String strategyBeanName) {
    if (strategyBeanName != null) {
      try {
        return beanFactory.getBean(strategyBeanName, AclStrategy.class);
      } catch (NoSuchBeanDefinitionException e) {
        logger.warn("Unable to find {} bean with name '{}' > fall back on default strategy",
            AclStrategy.class.getName(), strategyBeanName);
      }
    }
    return null;
  }

  private AclStrategy useIfPresent(AclStrategy strategy) {
    return strategy != null ? strategy : defaultStrategy;
  }
}
