package com.github.lothar.security.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.BeanNameAware;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy implements AclStrategy, BeanNameAware {

  private Map<AclFeature<?>, Object> filtersByFeature = new HashMap<>();
  private String name;

  public <Filter> void install(AclFeature<Filter> feature, Filter filter) {
    if (filter == null) {
      throw new IllegalArgumentException("Filter to register can't be null ; please use unregister("
          + AclFeature.class.getSimpleName() + ")");
    }
    filtersByFeature.put(feature, filter);
  }

  public <Filter> Filter uninstall(AclFeature<Filter> feature) {
    return (Filter) filtersByFeature.remove(feature);
  }

  public <Filter> Filter filterFor(AclFeature<Filter> feature) {
    return (Filter) filtersByFeature.get(feature);
  }

  public <Filter> boolean hasFilter(AclFeature<Filter> feature) {
    return filtersByFeature.containsKey(feature);
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
