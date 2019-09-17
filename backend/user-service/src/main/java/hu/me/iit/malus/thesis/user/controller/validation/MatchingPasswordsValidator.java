package hu.me.iit.malus.thesis.user.controller.validation;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom constraint validator, to check that the password matches the password confirmation field
 * in the registration request.
 * @author Javorek Dénes
 */
public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, Object> {
    @Override
    public void initialize(MatchingPasswords constraintAnnotation) {
        // Empty
    }

    /**
     * Simply checks the password and the passwordConfirm fields of the request with String.equals()
     * Throws IllegalStateException when used on an invalid class
     * @see RegistrationRequest
     * @param object
     * @param constraintValidatorContext
     * @return True if the password fields are identical, false otherwise
     */
    @Override
    public boolean isValid(final Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object instanceof RegistrationRequest) {
            final RegistrationRequest request = (RegistrationRequest) object;
            if (request.getPassword() != null && request.getPasswordConfirm() != null) {
                return request.getPassword().equals(request.getPasswordConfirm());
            }
            return false;
        }
        throw new IllegalStateException("Invalid target for " + this.getClass().getName());
    }
}
