package com.github.lothar.security.acl.compound;

import java.util.HashMap;
import java.util.Map;

import com.github.lothar.security.acl.AclFeatureType;

public class AclFeatureComposersRegistry {

  private Map<AclFeatureType, AclFeatureComposer<?>> behaviorTypes = new HashMap<>();

  public <Behavior> void register(AclFeatureType type, AclFeatureComposer<Behavior> composer) {
    behaviorTypes.put(type, composer);
  }

  public void unregister(AclFeatureType type) {
    behaviorTypes.remove(type);
  }

  @SuppressWarnings("unchecked")
  public <Behavior> AclFeatureComposer<Behavior> composerFor(AclFeatureType behaviorType) {
    return (AclFeatureComposer<Behavior>) behaviorTypes.get(behaviorType);
  }
}
