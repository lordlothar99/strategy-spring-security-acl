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
package com.github.lothar.security.acl.activation.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.activation.test.AclTestExecutionListener;
import com.github.lothar.security.acl.config.AclConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AclConfiguration.class)
@TestExecutionListeners({AclTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class})
public class AclActivatorFilterTest {

  @Resource
  private AclSecurityActivator aclSecurityActivator;

  @Test
  public void should_acl_security_be_enabled_when_filter_is_activated()
      throws IOException, ServletException {
    AclActivatorFilter filter = new AclActivatorFilter(aclSecurityActivator);
    FilterChain chainAssert = new FilterChain() {
      @Override
      public void doFilter(ServletRequest request, ServletResponse response)
          throws IOException, ServletException {
        assertThat(aclSecurityActivator.isEnabled()).isTrue();
      }
    };

    assertThat(aclSecurityActivator.isEnabled()).isFalse();
    filter.doFilter(null, null, chainAssert);
    assertThat(aclSecurityActivator.isEnabled()).isFalse();
  }
}
