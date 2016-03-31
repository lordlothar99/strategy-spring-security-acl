package com.github.lothar.security.acl;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy implements AclStrategy {

  private Map<AclFeatureType<?>, Object> features = new HashMap<>();

  public <Feature, F extends Feature> void register(AclFeatureType<Feature> featureType, F feature) {
    features.put(featureType, feature);
  }

  public <Feature> Feature unregister(AclFeatureType<Feature> featureType) {
    return (Feature) features.remove(featureType);
  }

  public <Feature> Feature featureFor(AclFeatureType<Feature> featureType) {
    return (Feature) features.get(featureType);
  }

}
