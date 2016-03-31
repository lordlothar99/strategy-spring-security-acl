package com.github.lothar.security.acl.jpa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.compound.AclComposersRegistry;
import com.github.lothar.security.acl.config.AclConfiguration;
import com.github.lothar.security.acl.jpa.JpaSpecProvider;
import com.github.lothar.security.acl.jpa.JpaSpecFeature;
import com.github.lothar.security.acl.jpa.compound.JpaSpecComposer;
import com.github.lothar.security.acl.jpa.repository.AclJpaRepositoryFactoryBean;

@Configuration
@Import(AclConfiguration.class)
@AutoConfigureAfter(AclConfiguration.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = AclJpaRepositoryFactoryBean.class)
public class JpaSpecFeatureConfiguration<T> {

  private JpaSpecFeature<T> jpaSpecFeature = new JpaSpecFeature<>();
  private Logger logger = LoggerFactory.getLogger(JpaSpecFeatureConfiguration.class);

  @Bean
  public JpaSpecFeature<T> jpaSpecFeature() {
    logger.info("Installed feature : {}", jpaSpecFeature);
    return jpaSpecFeature;
  }

  @Bean
  @ConditionalOnMissingBean(JpaSpecComposer.class)
  public JpaSpecComposer<T> jpaSpecComposer(AclComposersRegistry registry) {
    JpaSpecComposer<T> composer = new JpaSpecComposer<>();
    registry.register(jpaSpecFeature, composer);
    return composer;
  }

  @Bean
  public JpaSpecProvider<T> jpaSpecProvider(AclStrategyProvider strategyProvider) {
    return new JpaSpecProvider<>(strategyProvider, jpaSpecFeature);
  }
}
