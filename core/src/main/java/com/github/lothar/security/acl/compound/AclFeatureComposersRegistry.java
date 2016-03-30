package com.github.lothar.security.acl.compound;

import java.util.HashMap;
import java.util.Map;

import com.github.lothar.security.acl.AclFeatureType;

public class AclFeatureComposersRegistry {

  private Map<AclFeatureType, AclFeatureComposer<?>> featureComposers = new HashMap<>();

  public <Feature> void register(AclFeatureType type, AclFeatureComposer<Feature> composer) {
    featureComposers.put(type, composer);
  }

  public void unregister(AclFeatureType type) {
    featureComposers.remove(type);
  }

  @SuppressWarnings("unchecked")
  public <Feature> AclFeatureComposer<Feature> composerFor(AclFeatureType featureType) {
    return (AclFeatureComposer<Feature>) featureComposers.get(featureType);
  }
}
