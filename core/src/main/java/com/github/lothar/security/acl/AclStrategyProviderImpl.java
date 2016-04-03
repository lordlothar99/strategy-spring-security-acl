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

import static org.springframework.util.Assert.notNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class AclStrategyProviderImpl implements BeanFactoryAware, AclStrategyProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategy defaultStrategy;
  private BeanFactory beanFactory;

  public AclStrategyProviderImpl(AclStrategy defaultStrategy) {
    notNull(defaultStrategy, "Default ACL strategy can't be null");
    this.defaultStrategy = defaultStrategy;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public AclStrategy strategyFor(Class<?> entityClass) {

    AclStrategy strategy = null;

    Acl acl = entityClass.getAnnotation(Acl.class);
    if (acl != null) {
      String strategyBeanName = acl.value();
      logger.debug("{} annotation found on '{}', indicating strategy '{}'", Acl.class.getName(),
          entityClass.getSimpleName(), strategy);
      strategy = loadStrategyBean(strategyBeanName);

    } else {
      logger.debug("No {} annotation found on '{}' > fall back on default strategy",
          Acl.class.getName(), entityClass.getSimpleName());
      strategy = defaultStrategy;
    }

    logger.debug("Using acl strategy on '{}' : '{}'", entityClass.getSimpleName(), strategy);
    return strategy;
  }

  private AclStrategy loadStrategyBean(String strategyBeanName) {
    try {
      AclStrategy strategy = beanFactory.getBean(strategyBeanName, AclStrategy.class);
      return strategy;
    } catch (NoSuchBeanDefinitionException e) {
      throw new IllegalArgumentException("Unable to find " + AclStrategy.class.getName()
          + " bean with name '" + strategyBeanName + "'", e);
    }
  }

}
