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
package com.github.lothar.security.acl.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import org.springframework.aop.TargetSource;

public class AclPredicateTargetSource implements TargetSource {

    private Predicate original;
    private ThreadLocal<Predicate> delegate;
    private CriteriaBuilder criteriaBuilder;

    public AclPredicateTargetSource(CriteriaBuilder criteriaBuilder, Predicate original) {
        this.criteriaBuilder = criteriaBuilder;
        this.original = original;
        delegate = ThreadLocal.withInitial(() -> original);
    }

    public void installAcl(Predicate aclPredicate) {
        Predicate enhancedPredicate = criteriaBuilder.and(original, aclPredicate);
        delegate.set(enhancedPredicate);
    }

    public void uninstallAcl() {
        delegate.set(original);
    }

    @Override
    public Class<?> getTargetClass() {
        return delegate.get().getClass();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public Object getTarget() throws Exception {
        return delegate.get();
    }

    @Override
    public void releaseTarget(Object target) throws Exception {
    }

}
