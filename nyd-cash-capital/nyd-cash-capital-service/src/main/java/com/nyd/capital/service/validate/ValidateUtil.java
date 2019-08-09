package com.nyd.capital.service.validate;


import com.nyd.capital.model.annotation.EncryptField;
import com.nyd.capital.model.annotation.RequireField;
import com.nyd.capital.service.wsm.WsmBase;
import com.nyd.capital.service.wsm.WsmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
@Component
public class ValidateUtil {
    @Autowired
    private WsmConfig wsmConfig;
    @Autowired
    private WsmBase wsmBase;
    public  void process(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        for(Field field:fields){
            RequireField requireField =  field.getAnnotation(RequireField.class);
            EncryptField encryptField = field.getAnnotation(EncryptField.class);
            if(requireField!=null){
                try {
                    if(field.get(obj)==null||field.get(obj).toString().trim().length()==0){
                        throw new NullPointerException(field.getName()+"为空");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if(encryptField!=null){
                try {
                    if(field.get(obj)==null||field.get(obj).toString().trim().length()==0){

                    }else {
                       String value =  field.get(obj).toString();
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
                        Method setMethod = pd.getWriteMethod();
                        setMethod.invoke(obj,wsmConfig.encrypt(value,wsmConfig.getCsjmsy(),wsmConfig.getCsjmsy()));
//                       field.set(field.getName(),);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
