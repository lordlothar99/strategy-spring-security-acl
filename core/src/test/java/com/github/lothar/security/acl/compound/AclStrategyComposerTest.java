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
package com.github.lothar.security.acl.compound;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclTestConfiguration;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.StringTesterFeature;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AclTestConfiguration.class)
public class AclStrategyComposerTest {

  @Resource
  private AclStrategyComposer aclStrategyComposer;
  @Resource
  private StringTesterFeature stringTesterFeature;
  private SimpleAclStrategy containsA;
  private SimpleAclStrategy containsB;

  @Before
  public void init() {
    containsA = new SimpleAclStrategy();
    containsA.install(stringTesterFeature, s -> s.contains("A"));
    containsB = new SimpleAclStrategy();
    containsB.install(stringTesterFeature, s -> s.contains("B"));
  }

  @Test
  public void test_and_strategy() {
    AclStrategy aAndB = aclStrategyComposer.and(containsA, containsB);
    Function<String, Boolean> stringTester = aAndB.handlerFor(stringTesterFeature);
    assertThat(stringTester.apply("A")).isFalse();
    assertThat(stringTester.apply("B")).isFalse();
    assertThat(stringTester.apply("C")).isFalse();
    assertThat(stringTester.apply("AB")).isTrue();
    assertThat(stringTester.apply("ABC")).isTrue();
    assertThat(stringTester.apply("AC")).isFalse();
    assertThat(stringTester.apply("BC")).isFalse();
  }

  @Test
  public void test_or_strategy() {
    AclStrategy aOrB = aclStrategyComposer.or(containsA, containsB);
    Function<String, Boolean> stringTester = aOrB.handlerFor(stringTesterFeature);
    assertThat(stringTester.apply("A")).isTrue();
    assertThat(stringTester.apply("B")).isTrue();
    assertThat(stringTester.apply("C")).isFalse();
    assertThat(stringTester.apply("AB")).isTrue();
    assertThat(stringTester.apply("ABC")).isTrue();
    assertThat(stringTester.apply("AC")).isTrue();
    assertThat(stringTester.apply("BC")).isTrue();
  }

}
