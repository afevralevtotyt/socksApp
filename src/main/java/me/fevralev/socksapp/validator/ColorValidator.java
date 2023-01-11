package me.fevralev.socksapp.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColorValidator implements ConstraintValidator <Color, String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
            return value.equals("белый")||value.equals("синий")||value.equals("зеленый")||value.equals("красный")
                    ||value.equals("желтый")||value.equals("черный");

    }
}
