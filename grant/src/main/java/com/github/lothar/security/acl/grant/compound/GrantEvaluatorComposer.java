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
package com.github.lothar.security.acl.grant.compound;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.github.lothar.security.acl.compound.AclComposer;
import com.github.lothar.security.acl.grant.GrantEvaluator;

public class GrantEvaluatorComposer implements AclComposer<GrantEvaluator> {

  @Override
  public GrantEvaluator and(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, CompositionOperator.AND);
  }

  @Override
  public GrantEvaluator or(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, CompositionOperator.OR);
  }

  public static class CompoundGrantEvaluator implements GrantEvaluator {

    private final GrantEvaluator lhs;
    private final GrantEvaluator rhs;
    private final CompositionOperator compositionOperator;

    private CompoundGrantEvaluator(GrantEvaluator lhs, GrantEvaluator rhs,
        CompositionOperator compositionOperator) {

      Assert.notNull(compositionOperator, "compositionOperator must not be null!");

      this.lhs = lhs;
      this.rhs = rhs;
      this.compositionOperator = compositionOperator;
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Serializable targetId, String targetType) {
      return compositionOperator.apply( //
          lhs.isGranted(permission, authentication, targetId, targetType), //
          rhs.isGranted(permission, authentication, targetId, targetType) //
      );
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Object domainObject) {
      return compositionOperator.apply( //
          lhs.isGranted(permission, authentication, domainObject), //
          rhs.isGranted(permission, authentication, domainObject) //
      );
    }

    @Override
    public String toString() {
      return compositionOperator.toString(lhs, rhs);
    }
  }

  private static enum CompositionOperator {

    AND {
      @Override
      boolean apply(boolean lhs, boolean rhs) {
        return lhs && rhs;
      }

      @Override
      String toString(GrantEvaluator lhs, GrantEvaluator rhs) {
        return "(" + lhs + " AND " + rhs + ")";
      }
    },

    OR {
      @Override
      boolean apply(boolean lhs, boolean rhs) {
        return lhs || rhs;
      }

      @Override
      String toString(GrantEvaluator lhs, GrantEvaluator rhs) {
        return "(" + lhs + " OR " + rhs + ")";
      }
    };

    abstract boolean apply(boolean lhs, boolean rhs);

    abstract String toString(GrantEvaluator lhs, GrantEvaluator rhs);
  }
}
