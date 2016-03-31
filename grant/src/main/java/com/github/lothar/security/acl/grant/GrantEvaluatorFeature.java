package com.github.lothar.security.acl.grant;

import com.github.lothar.security.acl.AclFeature;

public final class GrantEvaluatorFeature implements AclFeature<GrantEvaluator> {

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
