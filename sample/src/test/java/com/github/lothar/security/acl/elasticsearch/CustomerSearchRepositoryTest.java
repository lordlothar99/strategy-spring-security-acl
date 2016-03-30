package com.github.lothar.security.acl.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.sample.SpringSecurityAclFeaturesApplication;
import com.github.lothar.security.acl.sample.domain.Customer;
import com.github.lothar.security.acl.sample.elasticsearch.CustomerSearchRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringSecurityAclFeaturesApplication.class)
public class CustomerSearchRepositoryTest {

  @Resource
  private CustomerSearchRepository searchRepository;

  @Test
  public void test() {
    long count = searchRepository.count();
    searchRepository.save(new Customer("Alice", "Smith"));
    searchRepository.save(new Customer("Bob", "Smith"));
    assertThat(searchRepository.count()).isEqualTo(count + 2);
  }

}
