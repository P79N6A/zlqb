package com.creativearts.nyd.pay.service.validator;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
@Component
public class ValidateUtil {

    public  void validate(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        for(Field field:fields){
            RequireField requireField =  field.getAnnotation(RequireField.class);
            if(requireField!=null){
                try {
                    if(field.get(obj)==null||field.get(obj).toString().trim().length()==0){
                        throw new NullPointerException(field.getName()+"为空");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
