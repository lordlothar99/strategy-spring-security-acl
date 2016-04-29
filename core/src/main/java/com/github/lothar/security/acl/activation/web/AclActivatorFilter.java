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
package com.github.lothar.security.acl.activation.web;

import static com.github.lothar.security.acl.activation.AclStatus.ENABLED;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.activation.AclStatus;

public class AclActivatorFilter implements Filter {

  private AclStatus statusDuringTest;
  private AclSecurityActivator aclSecurityActivator;

  public AclActivatorFilter(AclSecurityActivator aclSecurityActivator) {
    this(ENABLED, aclSecurityActivator);
  }

  public AclActivatorFilter(AclStatus statusDuringTest, AclSecurityActivator aclSecurityActivator) {
    super();
    this.statusDuringTest = statusDuringTest;
    this.aclSecurityActivator = aclSecurityActivator;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    AclStatus status = aclSecurityActivator.getStatus();
    aclSecurityActivator.setStatus(statusDuringTest);
    try {
      chain.doFilter(request, response);
    } finally {
      aclSecurityActivator.setStatus(status);
    }
  }

  @Override
  public void destroy() {}
}
