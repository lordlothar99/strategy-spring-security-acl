package com.github.lothar.security.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

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
    AclStrategy aclStrategy = null;

    Acl acl = entityClass.getAnnotation(Acl.class);
    if (acl != null) {
      String strategyBean = acl.value();
      aclStrategy = (AclStrategy) beanFactory.getBean(strategyBean);
      logger.debug("{} annotation found on {} -> using strategy {}", Acl.class.getName(),
          entityClass.getSimpleName(), aclStrategy);
    }

    if (aclStrategy == null) {
      aclStrategy = defaultStrategy;
      logger.debug("Using default acl strategy {} on {}", aclStrategy, entityClass.getSimpleName());
    }
    return aclStrategy;
  }

}
