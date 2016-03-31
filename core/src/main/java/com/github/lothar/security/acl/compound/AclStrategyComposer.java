package com.github.lothar.security.acl.compound;

import org.springframework.util.Assert;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.AclStrategy;

public class AclStrategyComposer {

  private AclComposersRegistry composersRegistry;

  public AclStrategyComposer(AclComposersRegistry composersRegistry) {
    super();
    this.composersRegistry = composersRegistry;
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
      AclComposer<Filter> composer = composersRegistry.composerFor(feature);
      return compositionOperator.apply(composer, lhs.filterFor(feature), rhs.filterFor(feature));
    }
  }

  private static enum CompositionOperator {

    AND {
      @Override
      <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs) {
        return composer.and(lhs, rhs);
      }
    },

    OR {
      @Override
      <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs) {
        return composer.or(lhs, rhs);
      }
    };

    abstract <Filter> Filter apply(AclComposer<Filter> composer, Filter lhs, Filter rhs);
  }
}
