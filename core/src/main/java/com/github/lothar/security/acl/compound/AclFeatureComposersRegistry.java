package com.github.lothar.security.acl.compound;

import java.util.HashMap;
import java.util.Map;

import com.github.lothar.security.acl.AclFeatureType;

@SuppressWarnings("unchecked")
public class AclFeatureComposersRegistry {

  private Map<AclFeatureType<?>, AclFeatureComposer<?>> featureComposers = new HashMap<>();

  public <Feature> void register(AclFeatureType<Feature> type,
      AclFeatureComposer<Feature> composer) {
    featureComposers.put(type, composer);
  }

  public <Feature> AclFeatureComposer<Feature> unregister(AclFeatureType<Feature> type) {
    return (AclFeatureComposer<Feature>) featureComposers.remove(type);
  }

  public <Feature> AclFeatureComposer<Feature> composerFor(AclFeatureType<Feature> featureType) {
    return (AclFeatureComposer<Feature>) featureComposers.get(featureType);
  }
}
