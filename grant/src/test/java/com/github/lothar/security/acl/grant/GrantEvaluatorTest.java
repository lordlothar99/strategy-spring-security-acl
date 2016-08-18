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
package com.github.lothar.security.acl.grant;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.grant.domain.AllowedToAllObject;
import com.github.lothar.security.acl.grant.domain.DeniedToAllObject;
import com.github.lothar.security.acl.grant.domain.NoAclObject;
import com.github.lothar.security.acl.grant.domain.NoStrategyObject;
import com.github.lothar.security.acl.grant.domain.UnknownStrategyObject;
import com.github.lothar.security.acl.grant.domain.WithoutHandlerObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantEvaluatorTestConfiguration.class)
public class GrantEvaluatorTest {

  @Resource
  private PermissionEvaluator permissionEvaluator;
  private Authentication authentication;

  @Before
  public void init() {
    authentication = new TestingAuthenticationToken("fake principal", "fake credentials");
  }

  // allow all

  @Test
  public void should_grant_when_allowAllStrategy_loaded() {
    AllowedToAllObject domainObject = new AllowedToAllObject();
    assertStrong(domainObject).isTrue();
    assertWeak(domainObject).isTrue();
  }

  // deny all

  @Test
  public void should_not_grant_when_denyAllStrategy_loaded() {
    DeniedToAllObject domainObject = new DeniedToAllObject();
    assertStrong(domainObject).isFalse();
    assertWeak(domainObject).isFalse();
  }

  // no acl

  @Test
  public void should_grant_when_no_acl() {
    NoAclObject domainObject = new NoAclObject();
    assertStrong(domainObject).isTrue();
    assertWeak(domainObject).isTrue();
  }

  // no strategy

  @Test
  public void should_grant_when_no_strategy() {
    NoStrategyObject domainObject = new NoStrategyObject();
    assertStrong(domainObject).isTrue();
    assertWeak(domainObject).isTrue();
  }

  // unknown strategy

  @Test
  public void should_grant_when_unknown_strategy_using_strong_signature() {
    UnknownStrategyObject domainObject = new UnknownStrategyObject();
    assertStrong(domainObject).isTrue();
  }

  @Test
  public void should_grant_when_unknown_strategy_using_weak_signature() {
    UnknownStrategyObject domainObject = new UnknownStrategyObject();
    assertWeak(domainObject).isTrue();
  }

  // without handler

  @Test
  public void should_grant_when_without_grantEvaluator_in_strategy() {
    WithoutHandlerObject domainObject = new WithoutHandlerObject();
    assertStrong(domainObject).isTrue();
    assertWeak(domainObject).isTrue();
  }

  private AbstractBooleanAssert<?> assertWeak(Object domainObject) {
    return assertThat(permissionEvaluator.hasPermission(authentication, "fake id",
        domainObject.getClass().getName(), "fake permission"));
  }

  private AbstractBooleanAssert<?> assertStrong(Object domainObject) {
    return assertThat(
        permissionEvaluator.hasPermission(authentication, domainObject, "fake permission"));
  }
}
