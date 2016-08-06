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

import org.springframework.security.core.Authentication;
import com.github.lothar.security.acl.named.NamedBean;

/**
 * @author Francois Lecomte
 *
 * @param <T> Object type
 * @param <ID> Object ID type
 * @param <A> {@link Authentication}
 * @param <P> Permission
 */
@SuppressWarnings("unchecked")
public abstract class TypedGrantEvaluator<T, ID extends Serializable, A, P> extends NamedBean
    implements GrantEvaluator {

  @Override
  public boolean isGranted(Object permission, Authentication authentication, Object domainObject) {
    P thePermission = mapPermission(permission);
    A theAuthentication = mapAuthentication(authentication);
    T theDomainObject = mapDomainObject(domainObject);
    return isGranted(thePermission, theAuthentication, theDomainObject);
  }

  public abstract boolean isGranted(P permission, A authentication, T domainObject);

  @Override
  public boolean isGranted(Object permission, Authentication authentication, Serializable targetId,
      String targetType) {
    P thePermission = mapPermission(permission);
    A theAuthentication = mapAuthentication(authentication);
    ID theTargetId = mapTargetId(targetId);
    Class<? extends T> theTargetType = mapTargetType(targetType);
    return isGranted(thePermission, theAuthentication, theTargetId, theTargetType);
  }

  public abstract boolean isGranted(P permission, A authentication, ID targetId,
      Class<? extends T> targetType);

  // ------------------------
  // Mappers ----------------
  // ------------------------

  protected P mapPermission(Object permission) {
    return (P) permission;
  }

  protected A mapAuthentication(Authentication authentication) {
    return (A) authentication;
  }

  protected T mapDomainObject(Object domainObject) {
    return (T) domainObject;
  }

  protected ID mapTargetId(Serializable targetId) {
    return (ID) targetId;
  }

  private Class<? extends T> mapTargetType(String targetType) {
    try {
      return (Class<? extends T>) Class.forName(targetType);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Unable to find target type '" + targetType + "'");
    }
  }
}
