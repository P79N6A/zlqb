package com.nyd.capital.ws;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public class ValidateUtils {
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

            public static <T> List<String> validate(T t) {
                 Validator validator = factory.getValidator();
                 Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

                 List<String> messageList = new ArrayList<>();
                 for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                         messageList.add(constraintViolation.getMessage());
                     }
                return messageList;
            }
}
