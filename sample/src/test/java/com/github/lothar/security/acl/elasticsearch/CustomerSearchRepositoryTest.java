package com.github.lothar.security.acl.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.AclFeatureType;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.sample.SpringSecurityAclFeaturesApplication;
import com.github.lothar.security.acl.sample.domain.Customer;
import com.github.lothar.security.acl.sample.elasticsearch.CustomerSearchRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringSecurityAclFeaturesApplication.class)
public class CustomerSearchRepositoryTest {

  @Resource
  private CustomerSearchRepository searchRepository;

  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    long count = searchRepository.count();
    searchRepository.save(new Customer("Alice", "Smith"));
    searchRepository.save(new Customer("Bob", "Smith"));
    searchRepository.save(new Customer("John", "Doe"));
    assertThat(searchRepository.count()).isEqualTo(count + 3);
  }

  @Configuration
  public static class TestAclConfiguration {

    private FilterBuilder customerFilterBuilder = FilterBuilders.termFilter("lastName", "Smith");

    @Bean
    public AclStrategy customerStrategy(AclFeatureType elasticSearchFeature) {
      SimpleAclStrategy simpleAclStrategy = new SimpleAclStrategy();
      simpleAclStrategy.register(elasticSearchFeature, customerFilterBuilder);
      return simpleAclStrategy;
    }
  }
}
