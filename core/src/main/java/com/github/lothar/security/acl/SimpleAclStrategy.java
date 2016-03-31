package com.github.lothar.security.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.BeanNameAware;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy implements AclStrategy, BeanNameAware {

  private Map<AclFeature<?>, Object> filtersByFeature = new HashMap<>();
  private String name;

  public <Filter> void register(AclFeature<Filter> feature, Filter filter) {
    filtersByFeature.put(feature, filter);
  }

  public <Filter> Filter unregister(AclFeature<Filter> feature) {
    return (Filter) filtersByFeature.remove(feature);
  }

  public <Filter> Filter filterFor(AclFeature<Filter> feature) {
    return (Filter) filtersByFeature.get(feature);
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
    return Objects.toString(name, getClass().getName()) + ":" + Objects.toString(filtersByFeature);
  }
}
