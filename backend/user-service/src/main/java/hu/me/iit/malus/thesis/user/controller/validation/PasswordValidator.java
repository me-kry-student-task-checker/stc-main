package hu.me.iit.malus.thesis.user.controller.validation;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Custom constraint validator, to check that the strength of the password passes a minimum level.
 * @author Javorek Dénes
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Empty
    }

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext context) {
        final org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
                // More possible rules can be found in org.passay
                new LengthRule(8, 30),
                new SpecialCharacterRule(1),
                new WhitespaceRule()));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result))).addConstraintViolation();
        return false;
    }
}
