package com.github.lothar.security.acl;

@FunctionalInterface
public interface AclStrategy {

  <Filter> Filter filterFor(AclFeature<Filter> feature);
}
