package hu.me.iit.malus.thesis.user.controller.validation;


import hu.me.iit.malus.thesis.user.model.UserRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom constraint validator, to check that the field can be converted to UserRole enum
 * @see hu.me.iit.malus.thesis.user.model.UserRole
 * @author Javorek Dénes
 */
public class RoleValidator implements ConstraintValidator<ValidRole, Object> {
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        // Empty
    }

    /**
     * Simply checks the password and the passwordConfirm fields of the request with String.equals()
     * @param object
     * @param constraintValidatorContext
     * @return True if the annotated field can be used as a USerRole, false otherwise
     */
    @Override
    public boolean isValid(final Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object instanceof String) {
            final String string = (String) object;

            try {
                UserRole.fromString(string);
                return true;
            } catch (IllegalArgumentException illegalArgE) {
                return false;
            }
        }
        return false;
    }
}
