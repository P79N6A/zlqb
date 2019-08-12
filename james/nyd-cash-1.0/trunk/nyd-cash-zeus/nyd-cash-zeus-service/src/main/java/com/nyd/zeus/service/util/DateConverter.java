package com.nyd.zeus.service.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public class DateConverter implements Converter{
    @Override
    public Object convert(Class type, Object value) {
        if(value == null) {
            return null;
        }

        if(value instanceof Date) {
            return value;
        }

        if(value instanceof Long) {
            Long longValue = (Long) value;
            return new Date(longValue.longValue());
        }

        try {
            if(value.toString().length()==10){
                value = value+" 12:12:12";
            }
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }
}
