package com.github.lothar.security.acl.activation;

public enum AclStatus {

  ENABLED(true), DISABLED(false);
  private boolean value;

  private AclStatus(boolean value) {
    this.value = value;
  }

  boolean value() {
    return value;
  }
}
