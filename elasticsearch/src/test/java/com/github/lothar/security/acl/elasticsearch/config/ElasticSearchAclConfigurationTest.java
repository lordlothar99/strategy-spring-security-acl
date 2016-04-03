package com.github.lothar.security.acl.elasticsearch.config;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ElasticSearchTestConfiguration.class)
public class ElasticSearchAclConfigurationTest {

  @Resource
  private ElasticSearchFeature elasticSearchFeature;

  @Test
  public void should_elasticSearchFeature_be_loaded() {
    assertThat(elasticSearchFeature).isNotNull();
  }
}
