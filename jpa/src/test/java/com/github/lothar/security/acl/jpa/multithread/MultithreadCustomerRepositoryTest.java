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

import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.lothar.security.acl.jpa.domain.Customer;
import com.github.lothar.security.acl.jpa.repository.CustomerRepository;

/**
 * Test ACL JPA {@link Specification} in multithread environment 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MultithreadTestConfiguration.class)
@Transactional
@TestExecutionListeners({ TestDataPreparer.class,
        DirtiesContextBeforeModesTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
public class MultithreadCustomerRepositoryTest {

    @Resource
    private CustomerRepository repository;
    private Customer aliceSmith;
    private Customer bobSmith;
    private Customer johnDoe;
    private ExecutorService executor;
    private Collection<Callable<String>> tasks;
    private static final int TIMES = 100;

    @Before
    public void init() {
        aliceSmith = repository.findByFirstName("Alice");
        bobSmith = repository.findByFirstName("Bob");
        johnDoe = repository.findByFirstName("John");

        executor = newFixedThreadPool(8);
        tasks = new ArrayList<>();
    }

    // count

    @Test
    public void should_count_authorized_customers() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.count()).isEqualTo(2);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.count()).isEqualTo(1);
        }}, "Doe");
        waitForAssertions();
    }

    // exist

    @Test
    public void should_test_customer_existance() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.exists(johnDoe.getId())).isFalse();
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.exists(johnDoe.getId())).isTrue();
        }}, "Doe");
        waitForAssertions();
    }

    // findAll

    @Test
    public void should_find_authorized_customers() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findAll()).containsOnly(aliceSmith, bobSmith);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findAll()).containsOnly(johnDoe);
        }}, "Doe");
        waitForAssertions();
    }

    @Test
    public void should_find_authorized_customers_using_specific_ids() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findAll(customerIds())).containsOnly(aliceSmith, bobSmith);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findAll(customerIds())).containsOnly(johnDoe);
        }}, "Doe");
        waitForAssertions();
    }

    // findByLastName

    @Test
    public void should_find_with_method_query__lastName() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByLastName("Doe")).isEmpty();
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByLastName("Doe")).containsOnly(johnDoe);
        }}, "Doe");
        waitForAssertions();
    }

    // findByFirstName

    @Test
    public void should_find_with_method_query__firstName() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByFirstName("John")).isNull();
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByFirstName("John")).isEqualTo(johnDoe);
        }}, "Doe");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByFirstName("Alice")).isEqualTo(aliceSmith);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findByFirstName("Alice")).isNull();
        }}, "Doe");
        waitForAssertions();
    }

    // findOne

    @Test
    public void should_findOne_with_method_query() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findOne(johnDoe.getId())).isNull();
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findOne(johnDoe.getId())).isEqualTo(johnDoe);
        }}, "Doe");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findOne(aliceSmith.getId())).isEqualTo(aliceSmith);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.findOne(aliceSmith.getId())).isNull();
        }}, "Doe");
        waitForAssertions();
    }

    // getOne

    @Test
    public void should_getOne_retrieve_customers() throws InterruptedException {
        prepareTaskAs(new Runnable() { @Override public void run() {
            Throwable throwable = null;
            try { repository.getOne(johnDoe.getId());
            } catch (Throwable t) { throwable = t; }
            assertThat(throwable).isInstanceOf(JpaObjectRetrievalFailureException.class);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.getOne(johnDoe.getId())).isEqualTo(johnDoe);
        }}, "Doe");
        prepareTaskAs(new Runnable() { @Override public void run() {
            assertThat(repository.getOne(aliceSmith.getId())).isEqualTo(aliceSmith);
        }}, "Smith");
        prepareTaskAs(new Runnable() { @Override public void run() {
            Throwable throwable = null;
            try { repository.getOne(aliceSmith.getId());
            } catch (Throwable t) { throwable = t; }
            assertThat(throwable).isInstanceOf(JpaObjectRetrievalFailureException.class);
        }}, "Doe");
        waitForAssertions();
    }

    // utils

    private Iterable<String> customerIds() {
        return asList(aliceSmith.getId(), bobSmith.getId(), johnDoe.getId());
    }

    private void prepareTaskAs(final Runnable runnable, final String lastName) {
        for (int i = 0; i < TIMES; i++) {
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Session.login(lastName);
                    try {
                        runnable.run();
                    } catch (ComparisonFailure failure) {
                        throw new ComparisonFailure(lastName + ": " + failure.getMessage(),
                                failure.getExpected(), failure.getActual());
                    } finally {
                        Session.logout();
                    }
                    return lastName;
                }
            });
        }
    }

    private void waitForAssertions() throws InterruptedException {
        List<Future<String>> futures = executor.invokeAll(tasks);
        tasks.clear();

        boolean fail = false;
        String message = "";
        for (Future<String> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                message += e.getMessage() + "\n";
                fail = true;
            }
        }
        if (fail) {
            fail(message);
        }
    }

}
