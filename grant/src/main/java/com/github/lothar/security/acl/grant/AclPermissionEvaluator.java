/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.lothar.security.acl.grant;

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

  public AclPermissionEvaluator(AclStrategyProvider strategyProvider,
      GrantEvaluatorFeature grantEvaluatorFeature) {
    super();
    this.strategyProvider = strategyProvider;
    this.grantEvaluatorFeature = grantEvaluatorFeature;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {

    if (targetDomainObject == null) {
      throw new IllegalArgumentException("No domain object specified; permission=" + permission
          + "; authentication=" + authentication);
    }
    AclStrategy strategy = strategyProvider.strategyFor(targetDomainObject.getClass());
    GrantEvaluator grantEvaluator = strategy.filterFor(grantEvaluatorFeature);

    // TODO implement default grantEvaluator

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetDomainObject);
    String granted = grant ? "granted" : "not granted";
    logger.debug("Permission {} {} for {} on object {} using strategy {}", permission, granted,
        authentication, targetDomainObject, strategy);
    return grant;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {

    Class<?> entityClass = asClass(targetType);
    AclStrategy strategy = strategyProvider.strategyFor(entityClass);
    GrantEvaluator grantEvaluator = strategy.filterFor(grantEvaluatorFeature);

    // TODO implement default grantEvaluator

    boolean grant = grantEvaluator.isGranted(permission, authentication, targetId, targetType);
    String granted = grant ? "granted" : "not granted";
    logger.debug("Permission {} {} for {} on object {}#{} using strategy {}", permission, granted,
        authentication, targetType, targetId, strategy);
    return grant;
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
