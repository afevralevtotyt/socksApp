package me.fevralev.socksapp.validator;

import me.fevralev.socksapp.model.Color;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColorValidator implements ConstraintValidator <ColorValid, String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
            return Color.colorOf(value)!=null;
    }
}
