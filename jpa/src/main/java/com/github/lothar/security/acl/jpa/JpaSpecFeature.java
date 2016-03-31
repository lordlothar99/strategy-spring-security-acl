package com.github.lothar.security.acl.jpa;

import org.springframework.data.jpa.domain.Specification;

import com.github.lothar.security.acl.AclFeature;
import com.github.lothar.security.acl.bean.NamedBean;

public final class JpaSpecFeature<T> extends NamedBean implements AclFeature<Specification<T>> {
}
