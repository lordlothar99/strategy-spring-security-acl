package com.github.lothar.security.acl.compound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.lothar.security.acl.AclFeature;

@SuppressWarnings("unchecked")
public class AclComposersRegistry {

  private Map<AclFeature<?>, AclComposer<?>> composers = new HashMap<>();

  public <Filter> void register(AclFeature<Filter> feature, AclComposer<Filter> composer) {
    composers.put(feature, composer);
  }

  public <Filter> AclComposer<Filter> unregister(AclFeature<Filter> feature) {
    return (AclComposer<Filter>) composers.remove(feature);
  }

  public <Filter> AclComposer<Filter> composerFor(AclFeature<Filter> feature) {
    return (AclComposer<Filter>) composers.get(feature);
  }

  @Override
  public String toString() {
    return Objects.toString(composers);
  }
}
