package com.github.lothar.security.acl.grant;

import static com.github.lothar.security.acl.compound.CompositionType.AND;
import static com.github.lothar.security.acl.compound.CompositionType.OR;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.github.lothar.security.acl.compound.AclFeatureComposer;
import com.github.lothar.security.acl.compound.CompositionType;

public class GrantEvaluatorFeatureComposer implements AclFeatureComposer<GrantEvaluator> {

  @Override
  public GrantEvaluator compose(CompositionType compositionType, GrantEvaluator lhs,
      GrantEvaluator rhs) {

    if (AND.equals(compositionType)) {
      return new ComposedGrantEvaluator(lhs, rhs, CompositionOperator.AND);

    } else if (OR.equals(compositionType)) {
      return new ComposedGrantEvaluator(lhs, rhs, CompositionOperator.OR);

    } else {
      throw new IllegalArgumentException("Illegal composition : " + compositionType);
    }
  }

  private static class ComposedGrantEvaluator implements GrantEvaluator {

    private final GrantEvaluator lhs;
    private final GrantEvaluator rhs;
    private final CompositionOperator compositionOperator;

    private ComposedGrantEvaluator(GrantEvaluator lhs, GrantEvaluator rhs,
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
