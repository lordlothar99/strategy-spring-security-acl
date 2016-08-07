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
