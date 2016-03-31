package com.github.lothar.security.acl.grant.compound;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.github.lothar.security.acl.compound.AclComposer;
import com.github.lothar.security.acl.grant.GrantEvaluator;

public class GrantEvaluatorComposer implements AclComposer<GrantEvaluator> {

  @Override
  public GrantEvaluator and(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, CompositionOperator.AND);
  }

  @Override
  public GrantEvaluator or(GrantEvaluator lhs, GrantEvaluator rhs) {
    return new CompoundGrantEvaluator(lhs, rhs, CompositionOperator.OR);
  }

  private static class CompoundGrantEvaluator implements GrantEvaluator {

    private final GrantEvaluator lhs;
    private final GrantEvaluator rhs;
    private final CompositionOperator compositionOperator;

    private CompoundGrantEvaluator(GrantEvaluator lhs, GrantEvaluator rhs,
        CompositionOperator compositionOperator) {

      Assert.notNull(compositionOperator, "compositionOperator must not be null!");

      this.lhs = lhs;
      this.rhs = rhs;
      this.compositionOperator = compositionOperator;
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Serializable targetId, String targetType) {
      return compositionOperator.apply( //
          lhs.isGranted(permission, authentication, targetId, targetType), //
          rhs.isGranted(permission, authentication, targetId, targetType) //
      );
    }

    @Override
    public boolean isGranted(Object permission, Authentication authentication,
        Object domainObject) {
      return compositionOperator.apply( //
          lhs.isGranted(permission, authentication, domainObject), //
          rhs.isGranted(permission, authentication, domainObject) //
      );
    }
  }

  private static enum CompositionOperator {

    AND {
      @Override
      boolean apply(boolean lhs, boolean rhs) {
        return lhs && rhs;
      }
    },

    OR {
      @Override
      boolean apply(boolean lhs, boolean rhs) {
        return lhs || rhs;
      }
    };

    abstract boolean apply(boolean lhs, boolean rhs);
  }
}
