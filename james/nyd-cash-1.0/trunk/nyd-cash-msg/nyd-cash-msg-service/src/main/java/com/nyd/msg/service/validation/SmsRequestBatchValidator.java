package com.nyd.msg.service.validation;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.model.SmsRequestBatch;
import com.nyd.msg.service.code.ResultCode;

/**
 * Yuxiang Cong
 **/
public class SmsRequestBatchValidator extends ValidatorHandler<SmsRequestBatch> implements Validator<SmsRequestBatch> {
    @Override
    public boolean validate(ValidatorContext context, SmsRequestBatch batch) {
        if(batch.getSmsType()==null){
            context.addError(ValidationError.create("短信类型"+ ResultCode.MSG_SMS_PARAM_INCOMPLETE.getMessage()).setErrorCode(ResultCode.MSG_SMS_PARAM_INCOMPLETE.getCodeInt()));
            return false;
        }else if(batch.getCellPhones()==null){
            context.addError(ValidationError.create("手机号"+ResultCode.MSG_SMS_PARAM_INCOMPLETE.getMessage()).setErrorCode(ResultCode.MSG_SMS_PARAM_INCOMPLETE.getCodeInt()));
            return false;
        }else if(batch.getCellPhones().size()<=0||batch.getCellPhones().size()>90){
            context.addError(ValidationError.create(ResultCode.MSG_SMS_BATCH_OVER_RANGE.getMessage()).setErrorCode(ResultCode.MSG_SMS_BATCH_OVER_RANGE.getCodeInt()));
            return false;
        }
        return true;
    }
}
