package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.user.model.UserInfo;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class UserInfoValidator  extends ValidatorHandler<UserInfo> implements Validator<UserInfo> {
    @Override
    public boolean validate(ValidatorContext context, UserInfo userInfo) {
        if(userInfo.getRealName()==null){
            context.addError(ValidationError.create("真实姓名"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(userInfo.getIdNumber()==null){
            context.addError(ValidationError.create("身份证"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return super.validate(context, userInfo);
    }
}
