package com.github.lothar.security.acl;

public interface AclStrategy {

  <Feature> Feature featureFor(AclFeatureType<Feature> featureType);
}
