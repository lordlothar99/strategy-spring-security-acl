package com.github.lothar.security.acl.compound;

public interface AclFeatureComposer<Feature> {

  Feature compose(CompositionType compositionType, Feature lhs, Feature rhs);

}
