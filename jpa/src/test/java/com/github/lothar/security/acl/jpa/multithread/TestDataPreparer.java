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
package com.github.lothar.security.acl.jpa.multithread;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.github.lothar.security.acl.jpa.domain.Customer;
import com.github.lothar.security.acl.jpa.repository.CustomerRepository;

@Component
public class TestDataPreparer extends AbstractTestExecutionListener {

    private Customer aliceSmith;
    private Customer bobSmith;
    private Customer johnDoe;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        CustomerRepository repository = repository(testContext);
        aliceSmith = repository.saveAndFlush(new Customer("Alice", "Smith"));
        bobSmith = repository.saveAndFlush(new Customer("Bob", "Smith"));
        johnDoe = repository.saveAndFlush(new Customer("John", "Doe"));
    }
    
    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        CustomerRepository repository = repository(testContext);
        repository.delete(aliceSmith);
        repository.delete(bobSmith);
        repository.delete(johnDoe);
    }

    private CustomerRepository repository(TestContext testContext) {
      ApplicationContext context = testContext.getApplicationContext();
      CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
      return customerRepository;
    }
}
