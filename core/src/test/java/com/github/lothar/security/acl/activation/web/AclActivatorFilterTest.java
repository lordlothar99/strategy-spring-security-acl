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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.activation.test.AclTestExecutionListener;
import com.github.lothar.security.acl.config.AclConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AclConfiguration.class)
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
