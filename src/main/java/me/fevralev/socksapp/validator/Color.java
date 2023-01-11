package me.fevralev.socksapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ColorValidator.class)
@Documented
public @interface Color {

    String message() default "Неверный цвет";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
