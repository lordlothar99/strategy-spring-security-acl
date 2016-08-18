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
package com.github.lothar.security.acl.sample.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.sample.SampleApplication;
import com.github.lothar.security.acl.sample.domain.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApplication.class)
public class CustomerSearchRepositoryTest {

  @Resource
  private CustomerSearchRepository searchRepository;

  @Before
  public void init() {
    searchRepository.deleteAll();
    searchRepository.save(new Customer("Alice", "Smith"));
    searchRepository.save(new Customer("Bob", "Smith"));
    searchRepository.save(new Customer("John", "Doe"));
  }

  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(searchRepository.count()).isEqualTo(2);
  }

  @Ignore("Not yet implemented #12")
  @Test
  public void should_not_find_members_of_Doe_family_when_strategy_applied() {
    assertThat(searchRepository.findByLastName("Doe")).isEmpty();
  }

  @Test
  public void should_search_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(searchRepository.search(matchQuery("name", "Doe"))).isEmpty();
  }
}
