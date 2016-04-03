package com.github.lothar.security.acl.jpa.config;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.jpa.JpaSpecFeature;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(JpaSpecTestConfiguration.class)
public class JpaSpecTest {

  @Resource
  private JpaSpecFeature<?> jpaSpecFeature;

  @Test
  public void should_grantEvaluatorFeature_be_loaded() {
    assertThat(jpaSpecFeature).isNotNull();
  }

  @Test
  public void should_aclJpaRepository_be_loaded() {

  }
}
