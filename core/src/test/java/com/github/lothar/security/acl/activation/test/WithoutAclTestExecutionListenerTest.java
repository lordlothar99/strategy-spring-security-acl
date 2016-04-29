package com.github.lothar.security.acl.activation.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.lothar.security.acl.activation.AclSecurityActivator;
import com.github.lothar.security.acl.config.AclConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AclConfiguration.class)
public class WithoutAclTestExecutionListenerTest {

  @Resource
  private AclSecurityActivator aclSecurityActivator;

  @Test
  public void should_acl_security_be_enabled_when_listener_is_not_installed() {
    assertThat(aclSecurityActivator.isEnabled()).isTrue();
  }
}
