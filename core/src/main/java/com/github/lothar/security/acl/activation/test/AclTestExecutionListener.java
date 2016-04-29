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
package com.github.lothar.security.acl.activation.test;

import static com.github.lothar.security.acl.activation.AclStatus.DISABLED;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;

import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.activation.AclStatus;

public class AclTestExecutionListener extends AbstractTestExecutionListener {

  private AclStatus statusDuringTest;
  private AclStatus initialStatus;

  public AclTestExecutionListener() {
    this(DISABLED);
  }

  public AclTestExecutionListener(AclStatus statusDuringTest) {
    super();
    Assert.notNull(statusDuringTest, "Status should not be null");
    this.statusDuringTest = statusDuringTest;
  }

  @Override
  public void beforeTestMethod(TestContext testContext) {
    AclSecurityActivator aclSecurityActivator = aclSecurityActivator(testContext);
    initialStatus = aclSecurityActivator.getStatus();
    aclSecurityActivator.setStatus(statusDuringTest);
  }

  @Override
  public void afterTestMethod(TestContext testContext) {
    AclSecurityActivator aclSecurityActivator = aclSecurityActivator(testContext);
    aclSecurityActivator.setStatus(initialStatus);
  }

  private AclSecurityActivator aclSecurityActivator(TestContext testContext) {
    ApplicationContext context = testContext.getApplicationContext();
    AclSecurityActivator aclSecurityActivator = context.getBean(AclSecurityActivator.class);
    return aclSecurityActivator;
  }
}
