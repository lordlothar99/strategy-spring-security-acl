package com.github.lothar.security.acl.grant;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class PermissionEvaluatorImpl implements PermissionEvaluator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider strategyProvider;
  private GrantEvaluatorFeature grantEvaluatorFeature;

  public PermissionEvaluatorImpl(AclStrategyProvider strategyProvider,
      GrantEvaluatorFeature grantEvaluatorFeature) {
    super();
    this.strategyProvider = strategyProvider;
    this.grantEvaluatorFeature = grantEvaluatorFeature;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {

    if (targetDomainObject == null) {
      throw new IllegalArgumentException("No domain object specified for permission " + permission);
    }
    AclStrategy strategy = strategyProvider.strategyFor(targetDomainObject.getClass());
    GrantEvaluator grantEvaluator = strategy.featureFor(grantEvaluatorFeature);

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetDomainObject);
    String granted = grant ? "granted" : "not granted";
    if (logger.isDebugEnabled()) {
      logger.debug("Permission {} {} for {} on object {} using strategy {}", permission, granted,
          authentication, targetDomainObject, strategy);
    }
    return grant;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {

    Class<?> entityClass = asClass(targetType);
    AclStrategy strategy = strategyProvider.strategyFor(entityClass);
    GrantEvaluator grantEvaluator = strategy.featureFor(grantEvaluatorFeature);

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetId, targetType);
    String granted = grant ? "granted" : "not granted";
    if (logger.isDebugEnabled()) {
      logger.debug("Permission {} {} for {} on object {}#{} using strategy {}", permission, granted,
          authentication, targetType, targetId, strategy);
    }
    return grant;
  }

  private Class<?> asClass(String className) {
    try {
      Class<?> entityClass = Class.forName(className);
      return entityClass;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "Unable to find class '" + className + "'. Perhaps it's mispelled ?");
    }
  }

}
