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
package com.github.lothar.security.acl.grant.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.grant.AclPermissionEvaluator;
import com.github.lothar.security.acl.grant.GrantEvaluator;
import com.github.lothar.security.acl.grant.GrantEvaluatorFeature;
import com.github.lothar.security.acl.grant.GrantEvaluatorTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrantEvaluatorTestConfiguration.class)
public class GrantEvaluatorAclConfigurationTest {

  @Resource
  private PermissionEvaluator permissionEvaluator;
  @Resource
  private GrantEvaluatorFeature grantEvaluatorFeature;
  @Resource
  private GrantEvaluator allowAllGrantEvaluator;
  @Resource
  private GrantEvaluator defaultGrantEvaluator;
  @Resource
  private GrantEvaluator denyAllGrantEvaluator;

  @Test
  public void should_grantEvaluatorFeature_be_loaded() {
    assertThat(grantEvaluatorFeature).isNotNull();
  }

  @Test
  public void should_permissionEvaluator_be_loaded() {
    assertThat(permissionEvaluator).isInstanceOf(AclPermissionEvaluator.class);
  }

  @Test
  public void should_default_and_allowAll_be_the_same() {
    assertThat(defaultGrantEvaluator).isSameAs(allowAllGrantEvaluator);
  }

  @Test
  public void should_denyAll_and_allowAll_be_different() {
    assertThat(denyAllGrantEvaluator).isNotSameAs(allowAllGrantEvaluator);
  }
}
