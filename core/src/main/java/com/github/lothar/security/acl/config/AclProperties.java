package com.github.lothar.security.acl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("strategy-security-acl")
public class AclProperties {

  private String overrideStrategy;

  public String getOverrideStrategy() {
    return overrideStrategy;
  }

  public void setOverrideStrategy(String overrideStrategy) {
    this.overrideStrategy = overrideStrategy;
  }

}
