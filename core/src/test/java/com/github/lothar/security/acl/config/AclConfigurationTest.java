package com.github.lothar.security.acl.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.AclStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AclConfiguration.class)
public class AclConfigurationTest {

  @Autowired
  private AclStrategy defaultStrategy;
  @Autowired
  private AclStrategy allowAllStrategy;
  @Autowired
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
