package hu.me.iit.malus.thesis.user.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
@Documented
public @interface ValidRole {
    String message() default "Invalid role for user.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

