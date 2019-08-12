package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.user.model.UserDetailInfo;

/**
 * Cong Yuxiang
 * 2017/12/4
 **/
public class UserDetailInfoValidator  extends ValidatorHandler<UserDetailInfo> implements Validator<UserDetailInfo> {


    @Override
    public boolean validate(ValidatorContext context, UserDetailInfo userDetailInfo) {
        if(userDetailInfo.getIdAddress()==null){
            context.addError(ValidationError.create("户籍地址"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(userDetailInfo.getLivingProvince()==null){
            context.addError(ValidationError.create("居住省"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(userDetailInfo.getLivingCity()==null){
            context.addError(ValidationError.create("居住市"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return true;
    }
}


