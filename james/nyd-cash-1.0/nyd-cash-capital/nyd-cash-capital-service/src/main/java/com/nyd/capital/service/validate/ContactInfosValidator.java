package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.user.model.ContactInfos;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class ContactInfosValidator  extends ValidatorHandler<ContactInfos> implements Validator<ContactInfos> {
    @Override
    public boolean validate(ValidatorContext context, ContactInfos contactInfos) {
        if(contactInfos.getDirectContactName()==null&&contactInfos.getMajorContactName()==null){
            context.addError(ValidationError.create("联系人"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(contactInfos.getMajorContactMobile()==null&&contactInfos.getDirectContactMobile()==null){
            context.addError(ValidationError.create("联系人电话"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(contactInfos.getDirectContactRelation()==null&&contactInfos.getMajorContactRelation()==null){
            context.addError(ValidationError.create("联系人关系"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return true;
    }
}
