/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.github.lothar.security.acl.grant.compound;

import java.io.Serializable;
import java.util.function.Supplier;

import org.springframework.security.core.Authentication;

import com.github.lothar.security.acl.compound.AbstractCompound;
import com.github.lothar.security.acl.compound.AclComposer;
import com.github.lothar.security.acl.compound.Operator;
import com.github.lothar.security.acl.grant.GrantEvaluator;

public class GrantEvaluatorComposer implements AclComposer<GrantEvaluator> {

  @Override
  public GrantEvaluator and(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, GrantEvaluatorOperator.AND);
  }

  @Override
  public GrantEvaluator or(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, GrantEvaluatorOperator.OR);
  }

  public static class CompoundGrantEvaluator
      extends AbstractCompound<GrantEvaluator, GrantEvaluatorOperator>implements GrantEvaluator {

    private CompoundGrantEvaluator(GrantEvaluator lhs, GrantEvaluator rhs,
        GrantEvaluatorOperator operator) {
      super(lhs, rhs, operator);
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Serializable targetId, String targetType) {
      return operator.apply( //
          () -> lhs.isGranted(permission, authentication, targetId, targetType), //
          () -> rhs.isGranted(permission, authentication, targetId, targetType) //
      );
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Object domainObject) {
      return operator.apply( //
          () -> lhs.isGranted(permission, authentication, domainObject), //
          () -> rhs.isGranted(permission, authentication, domainObject) //
      );
    }
  }

  private static enum GrantEvaluatorOperator implements Operator<GrantEvaluator> {

    AND {
      @Override
      boolean apply(Supplier<Boolean> lhs, Supplier<Boolean> rhs) {
        return lhs.get() && rhs.get();
      }
    },

    OR {
      @Override
      boolean apply(Supplier<Boolean> lhs, Supplier<Boolean> rhs) {
        return lhs.get() || rhs.get();
      }
    };

    abstract boolean apply(Supplier<Boolean> lhs, Supplier<Boolean> rhs);
  }
}
