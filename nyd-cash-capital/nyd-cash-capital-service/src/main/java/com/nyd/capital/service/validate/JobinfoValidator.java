package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.user.model.JobInfo;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class JobinfoValidator  extends ValidatorHandler<JobInfo> implements Validator<JobInfo> {
    @Override
    public boolean validate(ValidatorContext context, JobInfo jobInfo) {
        if(jobInfo.getTelephone()==null){
            context.addError(ValidationError.create("公司电话"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(jobInfo.getCompany()==null){
            context.addError(ValidationError.create("公司名称"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return true;
    }
}
