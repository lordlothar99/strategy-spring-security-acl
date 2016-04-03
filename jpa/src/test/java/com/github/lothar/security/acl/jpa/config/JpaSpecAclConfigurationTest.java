package com.github.lothar.security.acl.jpa.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.jpa.JpaSpecTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JpaSpecTestConfiguration.class)
public class JpaSpecAclConfigurationTest {

  @Resource
  private Specification<?> allowAllSpecification;
  @Resource
  private Specification<?> denyAllSpecification;
  @Resource
  private JpaSpecFeature<?> jpaSpecFeature;

  @Test
  public void should_jpaSpecFeature_be_loaded() {
    assertThat(jpaSpecFeature).isNotNull();
  }

  @Test
  public void should_allowAllSpec_be_loaded() {
    assertThat(allowAllSpecification).isNotNull();
  }

  @Test
  public void should_denyAllSpec_be_loaded() {
    assertThat(denyAllSpecification).isNotNull();
  }

}
