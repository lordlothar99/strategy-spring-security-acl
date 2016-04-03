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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.grant.domain.AllowedToAllDomainObject;
import com.github.lothar.security.acl.grant.domain.DeniedToAllDomainObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(GrantEvaluatorTestConfiguration.class)
public class GrantEvaluatorTest {

  @Resource
  private PermissionEvaluator permissionEvaluator;
  private Authentication authentication;

  @Before
  public void init() {
    authentication = new TestingAuthenticationToken("fake principal", "fake credentials");
  }

  @Test
  public void should_permissionEvaluator_be_loaded() {
    assertThat(permissionEvaluator).isInstanceOf(AclPermissionEvaluator.class);
  }

  @Test
  public void should_grant_when_allowAllStrategy_loaded() {
    Object domainObject = new AllowedToAllDomainObject();
    boolean grant =
        permissionEvaluator.hasPermission(authentication, domainObject, "fake permission");
    assertThat(grant).isTrue();
  }

  @Test
  public void should_grant_when_allowAllStrategy_loaded_using_weak_ref_signature() {
    boolean grant = permissionEvaluator.hasPermission(authentication, "fake id",
        AllowedToAllDomainObject.class.getName(), "fake permission");
    assertThat(grant).isTrue();
  }

  @Test
  public void should_not_grant_when_denyAllStrategy_loaded() {
    Object domainObject = new DeniedToAllDomainObject();
    boolean grant =
        permissionEvaluator.hasPermission(authentication, domainObject, "fake permission");
    assertThat(grant).isFalse();
  }

  @Test
  public void should_not_grant_when_denyAllStrategy_loaded_using_weak_ref_signature() {
    boolean grant = permissionEvaluator.hasPermission(authentication, "fake id",
        DeniedToAllDomainObject.class.getName(), "fake permission");
    assertThat(grant).isFalse();
  }
}
