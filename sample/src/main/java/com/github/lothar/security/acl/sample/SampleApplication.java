/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.lothar.security.acl.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import com.github.lothar.security.acl.elasticsearch.config.ElasticSearchAclConfiguration;
import com.github.lothar.security.acl.elasticsearch.repository.AclElasticsearchRepositoryFactoryBean;
import com.github.lothar.security.acl.jpa.config.JpaSpecAclConfiguration;
import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Import({ JpaSpecAclConfiguration.class, ElasticSearchAclConfiguration.class })
@EnableJpaRepositories(value = "com.github.lothar.security.acl.sample.jpa",
    repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class)
@EnableElasticsearchRepositories(value = "com.github.lothar.security.acl.sample.elasticsearch",
    repositoryFactoryBeanClass = AclElasticsearchRepositoryFactoryBean.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }
}
