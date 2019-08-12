package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.order.model.OrderDetailInfo;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class OrderDetailInfoValidator  extends ValidatorHandler<OrderDetailInfo> implements Validator<OrderDetailInfo> {
    @Override
    public boolean validate(ValidatorContext context, OrderDetailInfo orderDetailInfo) {
        if(orderDetailInfo.getRealName()==null||orderDetailInfo.getRealName().trim().length()<=1){
            context.addError(ValidationError.create("姓名"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(orderDetailInfo.getIdNumber()==null||orderDetailInfo.getIdNumber().trim().length()==0){
            context.addError(ValidationError.create("身份证号"+ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(orderDetailInfo.getMobile()==null||orderDetailInfo.getMobile().trim().length()==0){
            context.addError(ValidationError.create("手机号"+ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return true;
    }
}
