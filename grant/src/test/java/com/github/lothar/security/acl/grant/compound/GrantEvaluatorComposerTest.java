package com.github.lothar.security.acl.grant.compound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import com.github.lothar.security.acl.grant.GrantEvaluator;

public class GrantEvaluatorComposerTest {

    private GrantEvaluatorComposer composer = new GrantEvaluatorComposer();
    @Mock
    private GrantEvaluator lhs;
    @Mock
    private GrantEvaluator rhs;

    @Before
    public void init() {
        initMocks(this);
    }

    // OR operator - true

    @Test
    public void should_rhs_not_be_evaluated_when_OR_operator_and_lhs_is_true___strong_ref() {
        when(lhs.isGranted(any(), any(), any())).thenReturn(true);

        GrantEvaluator grantEvaluator = composer.or(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, "fake");

        verify(lhs).isGranted(any(), any(), any());
        verifyZeroInteractions(rhs);
        assertThat(granted).isTrue();
    }

    @Test
    public void should_rhs_not_be_evaluated_when_OR_operator_and_lhs_is_true___weak_ref() {
        when(lhs.isGranted(any(), any(), any(), anyString())).thenReturn(true);

        GrantEvaluator grantEvaluator = composer.or(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, 1L, "my_type");

        verify(lhs).isGranted(any(), any(), any(), anyString());
        verifyZeroInteractions(rhs);
        assertThat(granted).isTrue();
    }

    // OR operator - false,true

    @Test
    public void should_rhs_be_evaluated_when_OR_operator_and_lhs_is_false___strong_ref() {
        when(lhs.isGranted(any(), any(), any())).thenReturn(false);
        when(rhs.isGranted(any(), any(), any())).thenReturn(true);

        GrantEvaluator grantEvaluator = composer.or(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, "fake");

        verify(lhs).isGranted(any(), any(), any());
        verify(rhs).isGranted(any(), any(), any());
        assertThat(granted).isTrue();
    }

    @Test
    public void should_rhs_be_evaluated_when_OR_operator_and_lhs_is_false___weak_ref() {
        when(lhs.isGranted(any(), any(), any(), anyString())).thenReturn(false);
        when(rhs.isGranted(any(), any(), any(), anyString())).thenReturn(true);

        GrantEvaluator grantEvaluator = composer.or(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, 1L, "my_type");

        verify(lhs).isGranted(any(), any(), any(), anyString());
        verify(rhs).isGranted(any(), any(), any(), anyString());
        assertThat(granted).isTrue();
    }

    // AND operator - false

    @Test
    public void should_rhs_not_be_evaluated_when_AND_operator_and_lhs_is_false___strong_ref() {
        when(lhs.isGranted(any(), any(), any())).thenReturn(false);

        GrantEvaluator grantEvaluator = composer.and(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, "fake");

        verify(lhs).isGranted(any(), any(), any());
        verifyZeroInteractions(rhs);
        assertThat(granted).isFalse();
    }

    @Test
    public void should_rhs_not_be_evaluated_when_AND_operator_and_lhs_is_false___weak_ref() {
        when(lhs.isGranted(any(), any(), any(), anyString())).thenReturn(false);

        GrantEvaluator grantEvaluator = composer.and(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, 1L, "my_type");

        verify(lhs).isGranted(any(), any(), any(), anyString());
        verifyZeroInteractions(rhs);
        assertThat(granted).isFalse();
    }

    // AND operator - true,false

    @Test
    public void should_rhs_be_evaluated_when_AND_operator_and_lhs_is_true___strong_ref() {
        when(lhs.isGranted(any(), any(), any())).thenReturn(true);
        when(rhs.isGranted(any(), any(), any())).thenReturn(false);

        GrantEvaluator grantEvaluator = composer.and(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, "fake");

        verify(lhs).isGranted(any(), any(), any());
        verify(rhs).isGranted(any(), any(), any());
        assertThat(granted).isFalse();
    }

    @Test
    public void should_rhs_be_evaluated_when_AND_operator_and_lhs_is_true___weak_ref() {
        when(lhs.isGranted(any(), any(), any(), anyString())).thenReturn(true);
        when(rhs.isGranted(any(), any(), any(), anyString())).thenReturn(false);

        GrantEvaluator grantEvaluator = composer.and(lhs, rhs);
        boolean granted = grantEvaluator.isGranted(null, null, 1L, "my_type");

        verify(lhs).isGranted(any(), any(), any(), anyString());
        verify(rhs).isGranted(any(), any(), any(), anyString());
        assertThat(granted).isFalse();
    }

    private static String anyString() {
        return Mockito.any(String.class);
    }

    private static Authentication any() {
        return Mockito.any();
    }
}
