package hu.me.iit.malus.thesis.user.controller.validation;

import hu.me.iit.malus.thesis.user.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RoleValidatorTest {

    @Test(expected = Test.None.class)
    public void initializeDoesNotThrowException() {
        // GIVEN
        ValidRole validRole = mock(ValidRole.class);
        RoleValidator validator = new RoleValidator();

        // WHEN
        validator.initialize(validRole);

        // THEN
    }

    @Test
    public void whenInputIsNotString_isValidReturnsFalse() {
        // GIVEN
        int input = 5;
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        RoleValidator validator = new RoleValidator();

        // WHEN
        boolean isValid = validator.isValid(input, context);

        // THEN
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void whenInputIsNotARole_isValidReturnsFalse() {
        // GIVEN
        String role = "Investor Response Supervisor";
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        RoleValidator validator = new RoleValidator();

        // WHEN
        boolean isValid = validator.isValid(role, context);

        // THEN
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void whenInputIsARole_isValidReturnsTrue() {
        // GIVEN
        String role = UserRole.ADMIN.getRoleString();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        RoleValidator validator = new RoleValidator();

        // WHEN
        boolean isValid = validator.isValid(role, context);

        // THEN
        Assertions.assertThat(isValid).isTrue();
    }
}