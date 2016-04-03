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
package com.github.lothar.security.acl.compound;

import org.springframework.util.Assert;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.AclStrategy;

public class AclStrategyComposer implements AclComposer<AclStrategy> {

  private AclStrategyComposerProvider composerProvider;

  public AclStrategyComposer(AclStrategyComposerProvider composerProvider) {
    super();
    this.composerProvider = composerProvider;
  }

  public AclStrategy and(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, CompositionOperator.AND);
  }

  public AclStrategy or(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, CompositionOperator.OR);
  }

  private class CompoundAclStrategy implements AclStrategy {

    private final AclStrategy lhs;
    private final AclStrategy rhs;
    private final CompositionOperator compositionOperator;

    private CompoundAclStrategy(AclStrategy lhs, AclStrategy rhs,
        CompositionOperator compositionOperator) {

      Assert.notNull(compositionOperator, "CompositionType must not be null!");

      this.lhs = lhs;
      this.rhs = rhs;
      this.compositionOperator = compositionOperator;
    }

    @Override
    public <Handler> Handler handlerFor(AclFeature<Handler> feature) {
      AclComposer<Handler> composer = composerProvider.composerFor(feature);
      Assert.notNull(composer, "No composer found for " + feature);
      return compositionOperator.apply(composer, lhs.handlerFor(feature), rhs.handlerFor(feature));
    }

    @Override
    public String toString() {
      return compositionOperator.toString(lhs, rhs);
    }
  }

  private static enum CompositionOperator implements Operator<Object> {

    AND {
      @Override
      <Handler> Handler apply(AclComposer<Handler> composer, Handler lhs, Handler rhs) {
        return composer.and(lhs, rhs);
      }
    },

    OR {
      @Override
      <Handler> Handler apply(AclComposer<Handler> composer, Handler lhs, Handler rhs) {
        return composer.or(lhs, rhs);
      }
    };

    abstract <Handler> Handler apply(AclComposer<Handler> composer, Handler lhs, Handler rhs);
  }
}
