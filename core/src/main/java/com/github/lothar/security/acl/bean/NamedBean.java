package com.github.lothar.security.acl.bean;

import java.util.Objects;

import org.springframework.beans.factory.BeanNameAware;

public abstract class NamedBean implements BeanNameAware {

  private String name;

  @Override
  public void setBeanName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String name() {
    return Objects.toString(name, getClass().getSimpleName());
  }

  @Override
  public String toString() {
    return name();
  }
}
