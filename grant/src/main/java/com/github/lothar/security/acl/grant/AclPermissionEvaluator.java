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
package com.github.lothar.security.acl.grant;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;

public class AclPermissionEvaluator implements PermissionEvaluator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStrategyProvider strategyProvider;
  private GrantEvaluatorFeature grantEvaluatorFeature;
  private GrantEvaluator defaultGrantEvaluator;

  public AclPermissionEvaluator(AclStrategyProvider strategyProvider,
      GrantEvaluatorFeature grantEvaluatorFeature, GrantEvaluator defaultGrantEvaluator) {
    super();
    this.strategyProvider = strategyProvider;
    this.grantEvaluatorFeature = grantEvaluatorFeature;
    this.defaultGrantEvaluator = defaultGrantEvaluator;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {

    notNull(targetDomainObject, "No domain object specified; permission: '" + permission
        + "'; authentication: '" + authentication + "'");
    Class<? extends Object> entityClass = targetDomainObject.getClass();
    GrantEvaluator grantEvaluator = grantEvaluator(entityClass);

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetDomainObject);
    String granted = grant ? "granted" : "NOT granted";
    logger.trace("{} said permission '{}' is {} for {} on object {}", grantEvaluator, permission,
        granted, authentication, targetDomainObject);
    return grant;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {

    Class<?> entityClass = asClass(targetType);
    GrantEvaluator grantEvaluator = grantEvaluator(entityClass);

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetId, targetType);
    String granted = grant ? "granted" : "not granted";
    logger.trace("{} said permission '{}' {} for {} on object {}#{}", grantEvaluator, permission,
        granted, authentication, targetType, targetId);
    return grant;
  }

  private GrantEvaluator grantEvaluator(Class<?> entityClass) {
    GrantEvaluator grantEvaluator = defaultGrantEvaluator;

    AclStrategy strategy = strategyProvider.strategyFor(entityClass);
    if (strategy == null) {
      logger.debug("No strategy found for '{}' in strategy provider", entityClass.getSimpleName());
      return grantEvaluator;
    }

    grantEvaluator = strategy.handlerFor(grantEvaluatorFeature);
    if (grantEvaluator == null) {
      logger.debug("No grant evaluator found in strategy {} > fall back on default grant evaluator",
          strategy);
      grantEvaluator = defaultGrantEvaluator;
    }
    logger.debug("Using grant evaluator on '{}' : '{}'", entityClass.getSimpleName(), grantEvaluator);
    return grantEvaluator;
  }

  private static Class<?> asClass(String className) {
    try {
      Class<?> entityClass = Class.forName(className);
      return entityClass;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "Unable to find class '" + className + "'. Perhaps it's mispelled ?");
    }
  }

}
