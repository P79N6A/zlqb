package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import org.springframework.beans.BeanUtils;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class ValidatorUtils {
    public static void validateObject(Object obj, ValidatorHandler myValidator) throws ValidateException{
//        Preconditions.checkNotNull(obj, "validate object is null");
        if(obj==null){
            ValidateException validateException = new ValidateException();
            ErrorInfo info = new ErrorInfo();
            info.setErrorCode(ErrorCode.OBJ_NULL.getCode());
            info.setErrorMsg(ErrorCode.OBJ_NULL.getMsg());
            validateException.setErrorInfo(info);
            throw validateException;
        }
        ComplexResult result = FluentValidator.checkAll().on(obj, myValidator).doValidate().result(toComplex());
        if (!result.isSuccess()) {
            ValidateException validateException = new ValidateException();
            result.getErrors().forEach(validationError -> {
                ErrorInfo errorInfo = new ErrorInfo();
                BeanUtils.copyProperties(validationError, errorInfo);
                validateException.setErrorInfo(errorInfo);

            });
            throw validateException;

        }
    }

}
