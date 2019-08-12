package com.nyd.settlement.service.utils;


import com.nyd.settlement.model.annotation.RequireField;

import java.lang.reflect.Field;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public class ValidateUtil {

    public static void process(Object obj){
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
