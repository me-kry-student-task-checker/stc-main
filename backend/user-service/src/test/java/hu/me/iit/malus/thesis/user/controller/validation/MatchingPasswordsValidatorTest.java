package hu.me.iit.malus.thesis.user.controller.validation;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MatchingPasswordsValidatorTest {

    @Test(expected = Test.None.class)
    public void initializeDoesNotThrowException() {
        // GIVEN
        MatchingPasswords matchingPasswords = mock(MatchingPasswords.class);
        MatchingPasswordsValidator matchingPasswordsValidator = new MatchingPasswordsValidator();

        // WHEN
        matchingPasswordsValidator.initialize(matchingPasswords);

        // THEN
    }

    @Test(expected = IllegalStateException.class)
    public void whenRequestObjectIsNotRegistrationRequest_isValidThrowsException() {
        // GIVEN
        int registrationRequest = 5;
        MatchingPasswordsValidator validator = new MatchingPasswordsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // WHEN
        validator.isValid(registrationRequest, context);

        // THEN
    }

    @Test
    public void whenPasswordsAreNull_isValidReturnsFalse() {
        // GIVEN
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword(null);
        registrationRequest.setPasswordConfirm(null);
        MatchingPasswordsValidator validator = new MatchingPasswordsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // WHEN
        boolean isValid = validator.isValid(registrationRequest, context);

        // THEN
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void whenPasswordsAreNotEqual_isValidReturnsFalse() {
        // GIVEN
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("apple");
        registrationRequest.setPasswordConfirm("pear");
        MatchingPasswordsValidator validator = new MatchingPasswordsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // WHEN
        boolean isValid = validator.isValid(registrationRequest, context);

        // THEN
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void whenPasswordsAreEqual_isValidReturnsTrue() {
        // GIVEN
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setPassword("apple");
        registrationRequest.setPasswordConfirm("apple");
        MatchingPasswordsValidator validator = new MatchingPasswordsValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // WHEN
        boolean isValid = validator.isValid(registrationRequest, context);

        // THEN
        Assertions.assertThat(isValid).isTrue();
    }
}