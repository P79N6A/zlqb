package com.nyd.msg.service.validation;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.code.ResultCode;

/**
 * smsvo 验证
 */
public class SmsRequestValidator extends ValidatorHandler<SmsRequest> implements Validator<SmsRequest> {
    @Override
    public boolean validate(ValidatorContext context, SmsRequest smsVo) {
        if(smsVo.getSmsType()==null){
            context.addError(ValidationError.create("短信类型"+ ResultCode.MSG_SMS_PARAM_INCOMPLETE.getMessage()).setErrorCode(ResultCode.MSG_SMS_PARAM_INCOMPLETE.getCodeInt()));
            return false;
        }else if(smsVo.getCellphone()==null){
            context.addError(ValidationError.create("手机号"+ResultCode.MSG_SMS_PARAM_INCOMPLETE.getMessage()).setErrorCode(ResultCode.MSG_SMS_PARAM_INCOMPLETE.getCodeInt()));
            return false;
        }
        return true;
    }
}
