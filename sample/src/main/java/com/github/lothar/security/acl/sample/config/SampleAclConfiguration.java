package com.github.lothar.security.acl.sample.config;

import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.SimpleAclStrategy;
import com.github.lothar.security.acl.elasticsearch.ElasticSearchFeature;
import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.sample.domain.Customer;

@Configuration
public class SampleAclConfiguration {

  @Resource
  private ElasticSearchFeature elasticSearchFeature;
  @Resource
  private JpaSpecFeature<Customer> jpaSpecFeature;

  @Bean
  public AclStrategy customerStrategy() {
    SimpleAclStrategy customerStrategy = new SimpleAclStrategy();
    customerStrategy.install(elasticSearchFeature, smithFamilyFilter());
    customerStrategy.install(jpaSpecFeature, smithFamilySpec());
    return customerStrategy;
  }

  private TermFilterBuilder smithFamilyFilter() {
    return termFilter("lastName", "Smith");
  }

  private Specification<Customer> smithFamilySpec() {
    return new Specification<Customer>() {
      @Override
      public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("lastName"), "Smith");
      }
    };
  }
}
