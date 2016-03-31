package com.github.lothar.security.acl.compound;

public interface AclComposer<Filter> {

  Filter and(Filter lhs, Filter rhs);
  Filter or(Filter lhs, Filter rhs);

}
