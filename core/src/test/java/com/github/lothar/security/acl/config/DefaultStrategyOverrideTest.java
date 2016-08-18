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

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.AclStrategy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DenyAllDefaultConfiguration.class)
public class DefaultStrategyOverrideTest {

  @Resource
  private AclStrategy defaultAclStrategy;
  @Resource
  private AclStrategy allowAllStrategy;
  @Resource
  private AclStrategy denyAllStrategy;

  @Test
  public void should_default_and_denyAll_be_the_same() {
    assertThat(defaultAclStrategy).isSameAs(denyAllStrategy);
  }

  @Test
  public void should_default_and_allowAll_be_different() {
    assertThat(defaultAclStrategy).isNotSameAs(allowAllStrategy);
  }
}
