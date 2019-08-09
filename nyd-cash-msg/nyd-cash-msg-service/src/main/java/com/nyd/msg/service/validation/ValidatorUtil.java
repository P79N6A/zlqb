package com.nyd.msg.service.validation;


import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.msg.exception.ErrorInfo;
import com.nyd.msg.exception.ValidateException;
import com.nyd.msg.service.code.ResultCode;
import org.springframework.beans.BeanUtils;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toComplex;

//import com.alog.common.api.entity.ErrorInfo;

/**

* @describe: 校验工具类
**/
public class ValidatorUtil {

    public static void validateObject(Object obj, ValidatorHandler myValidator) throws ValidateException {
//        Preconditions.checkNotNull(obj, "validate object is null");
        if(obj==null){
            ValidateException validateException = new ValidateException();
            ErrorInfo info = new ErrorInfo();
            info.setErrorCode(ResultCode.MSG_OBJ_NULL.getCode());
            info.setErrorMsg(ResultCode.MSG_OBJ_NULL.getMessage());
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

    public static void main(String[] args) {

    }

}
