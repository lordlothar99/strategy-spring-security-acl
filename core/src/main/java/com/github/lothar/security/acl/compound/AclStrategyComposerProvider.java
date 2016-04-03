package com.github.lothar.security.acl.compound;

import com.github.lothar.security.acl.AclFeature;

@FunctionalInterface
public interface AclStrategyComposerProvider {

  <Filter> AclComposer<Filter> composerFor(AclFeature<Filter> feature);

}
