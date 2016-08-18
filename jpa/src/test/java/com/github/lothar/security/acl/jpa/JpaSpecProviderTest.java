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
package com.github.lothar.security.acl.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.jpa.domain.NoStrategyObject;
import com.github.lothar.security.acl.jpa.domain.UnknownStrategyObject;
import com.github.lothar.security.acl.jpa.domain.WithoutHandlerObject;
import com.github.lothar.security.acl.jpa.repository.AllowedToAllRepository;
import com.github.lothar.security.acl.jpa.repository.DeniedToAllRepository;
import com.github.lothar.security.acl.jpa.repository.NoAclRepository;
import com.github.lothar.security.acl.jpa.repository.NoStrategyRepository;
import com.github.lothar.security.acl.jpa.repository.UnknownStrategyRepository;
import com.github.lothar.security.acl.jpa.repository.WithoutHandlerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSpecTestConfiguration.class)
public class JpaSpecProviderTest {

  @Resource
  private JpaSpecProvider<Object> jpaSpecProvider;
  @Resource
  private Specification<?> defaultAclSpecification;
  @Resource
  private AllowedToAllRepository allowedToAllRepository;
  @Resource
  private DeniedToAllRepository deniedToAllRepository;
  @Resource
  private NoAclRepository noAclRepository;
  @Resource
  private NoStrategyRepository noStrategyRepository;
  @Resource
  private UnknownStrategyRepository unknownStrategyRepository;
  @Resource
  private WithoutHandlerRepository withoutHandlerRepository;

  @Test
  public void should_all_acl_repositories_be_loaded() {
    assertThat(allowedToAllRepository).isNotNull();
    assertThat(deniedToAllRepository).isNotNull();
    assertThat(noAclRepository).isNotNull();
    assertThat(noStrategyRepository).isNotNull();
    assertThat(unknownStrategyRepository).isNotNull();
    assertThat(withoutHandlerRepository).isNotNull();
  }

  @Test
  public void should_use_default_handler_when_none_defined() {
    assertThat(jpaSpecProvider.jpaSpecFor(WithoutHandlerObject.class)).isSameAs(defaultAclSpecification);
  }

  @Test
  public void should_use_default_handler_when_unknown_defined() {
    assertThat(jpaSpecProvider.jpaSpecFor(UnknownStrategyObject.class)).isSameAs(defaultAclSpecification);
  }

  @Test
  public void should_use_default_handler_when_no_strategy_defined() {
    assertThat(jpaSpecProvider.jpaSpecFor(NoStrategyObject.class)).isSameAs(defaultAclSpecification);
  }
}
