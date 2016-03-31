package com.github.lothar.security.acl.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.sample.SampleApplication;
import com.github.lothar.security.acl.sample.domain.Customer;
import com.github.lothar.security.acl.sample.elasticsearch.CustomerSearchRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleApplication.class)
public class CustomerSearchRepositoryTest {

  @Resource
  private CustomerSearchRepository searchRepository;
  private long count;

  @Before
  public void init() {
    count = searchRepository.count();
    searchRepository.save(new Customer("Alice", "Smith"));
    searchRepository.save(new Customer("Bob", "Smith"));
    searchRepository.save(new Customer("John", "Doe"));
  }

  @Ignore("ElasticSearch module is not fully implemented yet")
  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(searchRepository.count()).isEqualTo(count + 2);
  }
}
