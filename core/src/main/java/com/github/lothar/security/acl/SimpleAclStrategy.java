package com.github.lothar.security.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.BeanNameAware;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy implements AclStrategy, BeanNameAware {

  private Map<AclFeatureType<?>, Object> features = new HashMap<>();
  private String name;

  public <Feature> void register(AclFeatureType<Feature> featureType, Feature feature) {
    features.put(featureType, feature);
  }

  public <Feature> Feature unregister(AclFeatureType<Feature> featureType) {
    return (Feature) features.remove(featureType);
  }

  public <Feature> Feature featureFor(AclFeatureType<Feature> featureType) {
    return (Feature) features.get(featureType);
  }

  @Override
  public void setBeanName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return Objects.toString(name, getClass().getName()) + ":" + Objects.toString(features);
  }
}
