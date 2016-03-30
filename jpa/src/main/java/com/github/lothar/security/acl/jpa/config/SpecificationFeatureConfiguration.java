package com.github.lothar.security.acl.jpa.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclFeatureComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.jpa.AclJpaSpecProvider;
import com.github.lothar.security.acl.jpa.SpecificationFeature;
import com.github.lothar.security.acl.jpa.compound.SpecificationFeatureComposer;
import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;

@Configuration
@AutoConfigureAfter(AclConfiguration.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class)
public class SpecificationFeatureConfiguration {

  private SpecificationFeature specificationFeature = new SpecificationFeature();

  @Bean
  public SpecificationFeature specificationFeature() {
    return specificationFeature;
  }

  @Bean
  @ConditionalOnMissingBean(SpecificationFeatureComposer.class)
  public SpecificationFeatureComposer specificationFeatureComposer(
      AclFeatureComposersRegistry registry) {
    SpecificationFeatureComposer composer = new SpecificationFeatureComposer();
    registry.register(specificationFeature, composer);
    return composer;
  }

  @Bean
  public AclJpaSpecProvider aclJpaSpecProvider(AclStrategyProvider aclStrategyProvider) {
    return new AclJpaSpecProvider(aclStrategyProvider, specificationFeature);
  }
}
