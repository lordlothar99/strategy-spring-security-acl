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
package com.github.lothar.security.acl.sample.grant;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.sample.SampleApplication;
import com.github.lothar.security.acl.sample.domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleApplication.class)
@Transactional
public class CustomerServiceTest {

  @Resource
  private CustomerService service;
  @Resource
  private SimpleAclStrategy customerStrategy;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Before
  public void init() {
    logger.info("Customer strategy : {}", customerStrategy);
  }

  @Test
  public void should_save_an_authorized_customer() {
    service.save(new Customer("Alice", "Smith"));
  }

  @Test
  public void should_not_save_an_unauthorized_customer() {
    service.save(new Customer("John", "Doe"));
  }

}
