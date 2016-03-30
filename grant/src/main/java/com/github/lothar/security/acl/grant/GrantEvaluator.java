package com.github.lothar.security.acl.grant;

import java.io.Serializable;

import org.springframework.security.core.Authentication;

public interface GrantEvaluator {

  boolean isGranted(Object permission, Authentication authentication, Object domainObject);

  boolean isGranted(Object permission, Authentication authentication, Serializable targetId,
      String targetType);

}
