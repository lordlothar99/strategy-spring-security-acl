package com.github.lothar.security.acl.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.github.lothar.security.acl.elasticsearch.config.ElasticSearchFeatureConfiguration;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Import(ElasticSearchFeatureConfiguration.class)
@EnableElasticsearchRepositories(value = "com.github.lothar.security.acl.sample.elasticsearch",
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }
}
