package com.github.lothar.security.acl.sample.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.sample.SampleApplication;
import com.github.lothar.security.acl.sample.domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleApplication.class)
public class CustomerSearchRepositoryTest {

  @Resource
  private CustomerSearchRepository searchRepository;

  @Before
  public void init() {
    searchRepository.deleteAll();
    searchRepository.save(new Customer("Alice", "Smith"));
    searchRepository.save(new Customer("Bob", "Smith"));
    searchRepository.save(new Customer("John", "Doe"));
  }

  @Ignore("Not yet implemented")
  @Test
  public void should_find_authorized_customers_only_when_strategy_applied() {
    assertThat(searchRepository.count()).isEqualTo(2);
  }

  @Ignore("Not yet implemented")
  @Test
  public void should_not_find_members_of_Doe_family_when_strategy_applied() {
    assertThat(searchRepository.findByLastName("Doe")).isEmpty();
  }

  @Test
  public void should_search_retrieve_authorized_customers_only_when_strategy_applied() {
    assertThat(searchRepository.search(termQuery("name", "Doe"))).isEmpty();
  }
}
