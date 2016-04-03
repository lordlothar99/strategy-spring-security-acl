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

import org.springframework.util.Assert;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.AclStrategy;

public class AclStrategyComposer {

  private AclStrategyComposerProvider composerProvider;

  public AclStrategyComposer(AclStrategyComposerProvider composerProvider) {
    super();
    this.composerProvider = composerProvider;
  }

  public CompoundAclStrategy and(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, CompositionOperator.AND);
  }

  public CompoundAclStrategy or(AclStrategy lhs, AclStrategy rhs) {
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
    public <Filter> Filter filterFor(AclFeature<Filter> feature) {
      AclComposer<Filter> composer = composerProvider.composerFor(feature);
      return compositionOperator.apply(composer, lhs.filterFor(feature), rhs.filterFor(feature));
    }

    @Override
    public String toString() {
      return compositionOperator.toString(lhs, rhs);
    }
  }

  private static enum CompositionOperator {

    AND {
      @Override
      <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs) {
        return composer.and(lhs, rhs);
      }

      @Override
      <Filter> String toString(AclStrategy lhs, AclStrategy rhs) {
        return "(" + lhs + " AND " + rhs + ")";
      }
    },

    OR {
      @Override
      <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs) {
        return composer.or(lhs, rhs);
      }

      @Override
      <Filter> String toString(AclStrategy lhs, AclStrategy rhs) {
        return "(" + lhs + " OR " + rhs + ")";
      }
    };

    abstract <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs);

    abstract <Filter> String toString(AclStrategy lhs, AclStrategy rhs);
  }
}
