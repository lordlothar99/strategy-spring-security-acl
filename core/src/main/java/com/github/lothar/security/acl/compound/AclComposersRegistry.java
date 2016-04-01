/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
