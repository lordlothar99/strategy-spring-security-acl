package com.github.lothar.security.acl.sample.grant;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import com.github.lothar.security.acl.grant.TypedGrantEvaluator;

public abstract class AbstractGrantEvaluator<T, ID extends Serializable>
    extends TypedGrantEvaluator<T, ID, Authentication, Permission> {

  @Override
  protected Permission mapPermission(Object permission) {
    notNull(permission, "Permission must be not null");
    return Permission.valueOf(String.valueOf(permission));
  }

}
