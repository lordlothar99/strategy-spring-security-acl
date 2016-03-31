package com.github.lothar.security.acl.compound;

public interface AclComposer<Filter> {

  Filter compose(CompositionType compositionType, Filter lhs, Filter rhs);

}
