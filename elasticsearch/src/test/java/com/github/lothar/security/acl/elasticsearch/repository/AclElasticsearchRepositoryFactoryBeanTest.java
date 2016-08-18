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
package com.github.lothar.security.acl.elasticsearch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.elasticsearch.AclFilterProvider;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchTestConfiguration;
import com.github.lothar.security.acl.elasticsearch.domain.AllowedToAllObject;
import com.github.lothar.security.acl.elasticsearch.domain.DeniedToAllObject;
import com.github.lothar.security.acl.elasticsearch.domain.NoAclObject;
import com.github.lothar.security.acl.elasticsearch.domain.NoStrategyObject;
import com.github.lothar.security.acl.elasticsearch.domain.UnknownStrategyObject;
import com.github.lothar.security.acl.elasticsearch.domain.WithoutHandlerObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticSearchTestConfiguration.class)
public class AclElasticsearchRepositoryFactoryBeanTest {

  @Resource
  private AclFilterProvider filterProvider;
  @Resource
  private QueryBuilder allowAllFilter;
  @Resource
  private QueryBuilder denyAllFilter;

  @Test
  public void should_provider_return_allowAll_filter() {
    assertThat(filterProvider.filterFor(AllowedToAllObject.class)).isSameAs(allowAllFilter);
  }

  @Test
  public void should_provider_return_denyAll_spec() {
    assertThat(filterProvider.filterFor(DeniedToAllObject.class)).isSameAs(denyAllFilter);
  }

  @Test
  public void should_provider_return_allowAll_filter_for_noAcl() {
    assertThat(filterProvider.filterFor(NoAclObject.class)).isSameAs(allowAllFilter);
  }

  @Test
  public void should_provider_return_allowAll_filter_for_noStrategy() {
    assertThat(filterProvider.filterFor(NoStrategyObject.class)).isSameAs(allowAllFilter);
  }

  @Test
  public void should_provider_return_allowAll_filter_for_unknownStrategy() {
    assertThat(filterProvider.filterFor(UnknownStrategyObject.class)).isSameAs(allowAllFilter);
  }

  @Test
  public void should_provider_return_allowAll_filter_for_withoutHandlerStrategy() {
    assertThat(filterProvider.filterFor(WithoutHandlerObject.class)).isSameAs(allowAllFilter);
  }

}
