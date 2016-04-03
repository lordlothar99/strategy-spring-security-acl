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

import static org.springframework.util.Assert.notNull;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.AclStrategy;

public class AclStrategyComposer implements AclComposer<AclStrategy> {

  private AclStrategyComposerProvider composerProvider;

  public AclStrategyComposer(AclStrategyComposerProvider composerProvider) {
    super();
    this.composerProvider = composerProvider;
  }

  public AclStrategy and(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, StrategyOperator.AND);
  }

  public AclStrategy or(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, StrategyOperator.OR);
  }

  private class CompoundAclStrategy extends AbstractCompound<AclStrategy, StrategyOperator>
      implements AclStrategy {

    private CompoundAclStrategy(AclStrategy lhs, AclStrategy rhs, StrategyOperator operator) {
      super(lhs, rhs, operator);
    }

    @Override
    public <Handler> Handler handlerFor(AclFeature<Handler> feature) {
      AclComposer<Handler> composer = composerProvider.composerFor(feature);
      notNull(composer, "No composer found for " + feature);
      return operator.apply(composer, lhs.handlerFor(feature), rhs.handlerFor(feature));
    }
  }

  private static enum StrategyOperator implements Operator<AclStrategy> {

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
