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

import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.lothar.security.acl.named.NamedBean;

@SuppressWarnings("unchecked")
public class SimpleAclStrategy extends NamedBean implements AclStrategy {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Map<AclFeature<?>, Object> handlersByFeature = new HashMap<>();

  public <Handler> void install(AclFeature<Handler> feature, Handler handler) {
    notNull(feature, "Feature can't be null");
    notNull(handler, "Can't register a null handler ; please use unregister("
        + AclFeature.class.getSimpleName() + ")");
    handlersByFeature.put(feature, handler);
    if (logger.isDebugEnabled()) {
      logger.debug("Installed {} handler in {} : {}", feature, name(), handler);
    }
  }

  public <Handler> Handler uninstall(AclFeature<Handler> feature) {
    Handler filter = (Handler) handlersByFeature.remove(feature);
    if (logger.isDebugEnabled()) {
      logger.debug("Uninstalled {} handler from {}", feature, name());
    }
    return filter;
  }

  public <Handler> Handler handlerFor(AclFeature<Handler> feature) {
    return (Handler) handlersByFeature.get(feature);
  }

  public <Handler> boolean hasHandler(AclFeature<Handler> feature) {
    return handlersByFeature.containsKey(feature);
  }

  @Override
  public String toString() {
    return name() + ":" + Objects.toString(handlersByFeature);
  }
}
