package com.github.lothar.security.acl.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.github.lothar.security.acl.elasticsearch.config.ElasticSearchFeatureConfiguration;

@SpringBootApplication
@ComponentScan
@Import({/*AclConfiguration.class, */ElasticSearchFeatureConfiguration.class})
public class SpringSecurityAclFeaturesApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringSecurityAclFeaturesApplication.class, args);
  }
}
