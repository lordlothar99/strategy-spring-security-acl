package com.github.lothar.security.acl.compound;

import static org.springframework.util.Assert.notNull;

public abstract class AbstractCompound<T, O extends Operator<T>> {

  protected final T lhs;
  protected final T rhs;
  protected final O operator;

  public AbstractCompound(T lhs, T rhs, O operator) {
    super();
    notNull(operator, "Operator must not be null");
    this.lhs = lhs;
    this.rhs = rhs;
    this.operator = operator;
  }

  @Override
  public String toString() {
    return operator.toString(lhs, rhs);
  }

}
