package hu.me.iit.malus.thesis.user.controller.validation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordValidatorTest {

    @Test(expected = Test.None.class)
    public void initializeDoesNotThrowException() {
        // GIVEN
        ValidPassword matchingPasswords = mock(ValidPassword.class);
        PasswordValidator passwordValidator = new PasswordValidator();

        // WHEN
        passwordValidator.initialize(matchingPasswords);

        // THEN
    }

    @Test
    public void whenPasswordCotainsWhitespace_isValidReturnsFalse() {
        // GIVEN
        String password = "The\tquick\tbrown\tfox\tjumps\tover\tthe\tlazy\tdog";
        PasswordValidator passwordValidator = new PasswordValidator();
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);

        // WHEN
        boolean isValid = passwordValidator.isValid(password, context);

        // THEN
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void whenPasswordIsStrongEnough_isValidReturnsTrue() {
        // GIVEN
        String password = "4ccur4t3T3st1ng1s3xh4ust1ng!";
        PasswordValidator passwordValidator = new PasswordValidator();
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);

        // WHEN
        boolean isValid = passwordValidator.isValid(password, context);

        // THEN
        Assertions.assertThat(isValid).isTrue();
    }
}