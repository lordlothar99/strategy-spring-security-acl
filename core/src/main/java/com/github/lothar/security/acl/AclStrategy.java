package com.github.lothar.security.acl;

public interface AclStrategy {

  <Filter> Filter filterFor(AclFeature<Filter> feature);
}
