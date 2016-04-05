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

import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lothar.security.acl.AclFeature;

@SuppressWarnings("unchecked")
public class AclComposersRegistry implements AclStrategyComposerProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private Map<AclFeature<?>, AclComposer<?>> composers = new HashMap<>();

  public <Handler> void register(AclFeature<Handler> feature, AclComposer<Handler> composer) {
    notNull(feature, "Feature can't be null");
    composers.put(feature, composer);
    logger.debug("Registered {} composer: {}", feature, composer);
  }

  public <Handler> AclComposer<Handler> unregister(AclFeature<Handler> feature) {
    return (AclComposer<Handler>) composers.remove(feature);
  }

  @Override
  public <Handler> AclComposer<Handler> composerFor(AclFeature<Handler> feature) {
    return (AclComposer<Handler>) composers.get(feature);
  }

  @Override
  public String toString() {
    return Objects.toString(composers);
  }
}
