package com.github.lothar.security.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class AclStrategyProvider implements BeanFactoryAware {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategy defaultStrategy;
  private BeanFactory beanFactory;

  public void setDefaultStrategy(AclStrategy defaultStrategy) {
    this.defaultStrategy = defaultStrategy;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public AclStrategy strategyFor(Class<?> entityClass) {
    AclStrategy strategy = loadStrategy(entityClass);

    if (strategy == null) {
      strategy = defaultStrategy;
      logger.debug("Using default acl strategy on '{}' : '{}'", entityClass.getSimpleName(),
          strategy);
    }
    return strategy;
  }

  private AclStrategy loadStrategy(Class<?> entityClass) {
    AclStrategy strategy = null;

    Acl acl = entityClass.getAnnotation(Acl.class);
    if (acl != null) {
      String strategyBeanName = acl.value();
      try {
        strategy = beanFactory.getBean(strategyBeanName, AclStrategy.class);
        logger.debug("{} annotation found on '{}' -> using strategy '{}'", Acl.class.getName(),
            entityClass.getSimpleName(), strategy);
      } catch (NoSuchBeanDefinitionException e) {
        throw new IllegalArgumentException("Unable to find " + AclStrategy.class.getSimpleName()
            + " bean with name '" + strategyBeanName + "'", e);
      }
    }
    return strategy;
  }

}
