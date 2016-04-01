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
package com.github.lothar.security.acl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.lothar.security.acl.bean.NamedBean;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy extends NamedBean implements AclStrategy {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Map<AclFeature<?>, Object> filtersByFeature = new HashMap<>();

  public <Filter> void install(AclFeature<Filter> feature, Filter filter) {
    if (filter == null) {
      throw new IllegalArgumentException("Filter to register can't be null ; please use unregister("
          + AclFeature.class.getSimpleName() + ")");
    }
    filtersByFeature.put(feature, filter);
    logger.debug("Installed feature {} in {} : {}", feature, name(), filter);
  }

  public <Filter> Filter uninstall(AclFeature<Filter> feature) {
    Filter filter = (Filter) filtersByFeature.remove(feature);
    logger.debug("Uninstalled feature {} from {}", feature, name());
    return filter;
  }

  public <Filter> Filter filterFor(AclFeature<Filter> feature) {
    return (Filter) filtersByFeature.get(feature);
  }

  public <Filter> boolean hasFilter(AclFeature<Filter> feature) {
    return filtersByFeature.containsKey(feature);
  }

  @Override
  public String toString() {
    return name() + ":" + Objects.toString(filtersByFeature);
  }
}
