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
package com.github.lothar.security.acl.jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.jpa.JpaSpecProvider;
import com.github.lothar.security.acl.jpa.JpaSpecTestConfiguration;
import com.github.lothar.security.acl.jpa.domain.AllowedToAllObject;
import com.github.lothar.security.acl.jpa.domain.DeniedToAllObject;
import com.github.lothar.security.acl.jpa.domain.NoAclObject;
import com.github.lothar.security.acl.jpa.domain.NoStrategyObject;
import com.github.lothar.security.acl.jpa.domain.UnknownStrategyObject;
import com.github.lothar.security.acl.jpa.domain.WithoutHandlerObject;
import com.github.lothar.security.acl.jpa.spec.AllowAllSpecification;
import com.github.lothar.security.acl.jpa.spec.DenyAllSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSpecTestConfiguration.class)
public class AclJpaRepositoryFactoryBeanTest {

  @Resource
  private JpaSpecProvider<Object> jpaSpecProvider;
  @Resource
  private AllowAllSpecification<Object> allowAllSpec;
  @Resource
  private DenyAllSpecification<Object> denyAllSpec;

  @Test
  public void should_provider_return_allowAll_spec() {
    assertThat(jpaSpecProvider.jpaSpecFor(AllowedToAllObject.class)).isSameAs(allowAllSpec);
  }

  @Test
  public void should_provider_return_denyAll_spec() {
    assertThat(jpaSpecProvider.jpaSpecFor(DeniedToAllObject.class)).isSameAs(denyAllSpec);
  }

  @Test
  public void should_provider_return_allowAll_spec_for_noAcl() {
    assertThat(jpaSpecProvider.jpaSpecFor(NoAclObject.class)).isSameAs(allowAllSpec);
  }

  @Test
  public void should_provider_return_allowAll_spec_for_noStrategy() {
    assertThat(jpaSpecProvider.jpaSpecFor(NoStrategyObject.class)).isSameAs(allowAllSpec);
  }

  @Test
  public void should_provider_return_allowAll_spec_for_unknownStrategy() {
    assertThat(jpaSpecProvider.jpaSpecFor(UnknownStrategyObject.class)).isSameAs(allowAllSpec);
  }

  @Test
  public void should_provider_return_allowAll_spec_for_withoutHandlerStrategy() {
    assertThat(jpaSpecProvider.jpaSpecFor(WithoutHandlerObject.class)).isSameAs(allowAllSpec);
  }

}
