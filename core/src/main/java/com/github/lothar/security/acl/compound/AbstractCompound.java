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
