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
package com.github.lothar.security.acl.activation;

import static com.github.lothar.security.acl.activation.AclStatus.DISABLED;
import static com.github.lothar.security.acl.activation.AclStatus.ENABLED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AclSecurityActivator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AclStatus status;

  public AclSecurityActivator() {
    this(true);
  }

  public AclSecurityActivator(boolean enabled) {
    super();
    this.status = enabled ? ENABLED : DISABLED;
  }

  public AclSecurityActivator(AclStatus status) {
    super();
    this.status = status;
  }

  public void enable() {
    setStatus(ENABLED);
  }

  public void disable() {
    setStatus(DISABLED);
  }

  public void setStatus(AclStatus status) {
    this.status = status;
    logger.debug("ACL {}", status);
  }

  public AclStatus getStatus() {
    return status;
  }

  public boolean isDisabled() {
    return DISABLED.equals(status);
  }

  public boolean isEnabled() {
    return ENABLED.equals(status);
  }

}
