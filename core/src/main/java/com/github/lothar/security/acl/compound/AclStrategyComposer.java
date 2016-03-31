package com.github.lothar.security.acl.compound;

import org.springframework.util.Assert;

import com.github.lothar.security.acl.AclFeatureType;
import com.github.lothar.security.acl.AclStrategy;

public class AclStrategyComposer {

  private AclFeatureComposersRegistry composersRegistry;

  public AclStrategyComposer(AclFeatureComposersRegistry composersRegistry) {
    super();
    this.composersRegistry = composersRegistry;
  }

  public CompoundAclStrategy and(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, CompositionType.AND);
  }

  public CompoundAclStrategy or(AclStrategy lhs, AclStrategy rhs) {
    return new CompoundAclStrategy(lhs, rhs, CompositionType.OR);
  }

  private class CompoundAclStrategy implements AclStrategy {

    private final AclStrategy lhs;
    private final AclStrategy rhs;
    private final CompositionType compositionType;

    private CompoundAclStrategy(AclStrategy lhs, AclStrategy rhs,
        CompositionType compositionType) {

      Assert.notNull(compositionType, "CompositionType must not be null!");

      this.lhs = lhs;
      this.rhs = rhs;
      this.compositionType = compositionType;
    }

    @Override
    public <Feature> Feature featureFor(AclFeatureType<Feature> featureType) {
      AclFeatureComposer<Feature> featureComposer = composersRegistry.composerFor(featureType);
      return featureComposer.compose(//
          compositionType, //
          lhs.featureFor(featureType), //
          rhs.featureFor(featureType)//
      );
    }
  }
}
