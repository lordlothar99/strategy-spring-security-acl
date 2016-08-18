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

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.config.AclProperties;
import com.github.lothar.security.acl.domain.AllowedToAllObject;
import com.github.lothar.security.acl.domain.DeniedToAllObject;
import com.github.lothar.security.acl.domain.NoAclObject;
import com.github.lothar.security.acl.domain.NoStrategyObject;
import com.github.lothar.security.acl.domain.UnknownStrategyObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AclConfiguration.class)
public class AclStrategyProviderImplTest {

  @Resource
  private AclSecurityActivator activator;
  @Resource
  private AclStrategy defaultAclStrategy;
  @Resource
  private AclStrategy allowAllStrategy;
  @Resource
  private AclStrategy denyAllStrategy;
  @Resource
  private AclStrategyProvider strategyProvider;
  @Resource
  private AclProperties properties;

  @Test
  public void test_provider_for_allowAllStrategy() {
    assertThat(strategyFor(AllowedToAllObject.class)).isSameAs(allowAllStrategy);
  }

  @Test
  public void test_provider_for_denyAllStrategy() {
    assertThat(strategyFor(DeniedToAllObject.class)).isSameAs(denyAllStrategy);
  }

  @Test
  public void should_provider_return_defaultStrategy_when_no_strategy() {
    assertThat(strategyFor(NoStrategyObject.class)).isSameAs(defaultAclStrategy);
  }

  @Test
  public void should_provider_return_defaultStrategy_when_no_acl_annotation() {
    assertThat(strategyFor(NoAclObject.class)).isSameAs(defaultAclStrategy);
  }

  @Test
  public void should_provider_return_defaultStrategy_when_unknown_strategy() {
    assertThat(strategyFor(UnknownStrategyObject.class)).isSameAs(defaultAclStrategy);
  }

  @Test
  public void should_use_specified_strategy_when_override_set() {
    assertThat(properties.getOverrideStrategy()).isNull();
    properties.setOverrideStrategy("denyAllStrategy");
    try {
      assertThat(strategyFor(UnknownStrategyObject.class)).isSameAs(denyAllStrategy);
    } finally {
      properties.setOverrideStrategy(null);
    }
  }

  @Test
  public void should_use_allowAll_strategy_when_disabled() {
    activator.disable();
    try {
      assertThat(strategyFor(DeniedToAllObject.class)).isSameAs(allowAllStrategy);
    } finally {
      activator.enable();
    }
  }

  private AclStrategy strategyFor(Class<?> entityClass) {
    return strategyProvider.strategyFor(entityClass);
  }

}
