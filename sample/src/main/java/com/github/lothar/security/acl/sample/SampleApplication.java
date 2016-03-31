package com.github.lothar.security.acl.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.lothar.security.acl.elasticsearch.config.ElasticSearchFeatureConfiguration;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;
import com.github.lothar.security.acl.jpa.config.JpaSpecFeatureConfiguration;
import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Import({ JpaSpecFeatureConfiguration.class, ElasticSearchFeatureConfiguration.class })
@EnableJpaRepositories(value = "com.github.lothar.security.acl.sample.jpa",
    repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class)
@EnableElasticsearchRepositories(value = "com.github.lothar.security.acl.sample.elasticsearch",
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }
}
