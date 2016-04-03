package com.github.lothar.security.acl.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.AclStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AclConfiguration.class)
public class AclConfigurationTest {

  @Resource
  private AclStrategy defaultStrategy;
  @Resource
  private AclStrategy allowAllStrategy;
  @Resource
  private AclStrategy denyAllStrategy;

  @Test
  public void should_default_and_allowAll_be_the_same() {
    assertThat(defaultStrategy).isSameAs(allowAllStrategy);
  }

  @Test
  public void should_denyAll_and_allowAll_be_different() {
    assertThat(denyAllStrategy).isNotSameAs(allowAllStrategy);
  }

}
