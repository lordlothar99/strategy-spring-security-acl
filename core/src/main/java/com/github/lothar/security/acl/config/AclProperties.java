/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.github.lothar.security.acl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("strategy-security-acl")
public class AclProperties {

  private String overrideStrategy;
  private boolean disabled;

  public String getOverrideStrategy() {
    return overrideStrategy;
  }

  public void setOverrideStrategy(String overrideStrategy) {
    this.overrideStrategy = overrideStrategy;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

}
