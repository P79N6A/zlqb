package com.nyd.capital.service.validate;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.nyd.capital.api.enums.ErrorCode;
import com.nyd.order.model.OrderInfo;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public class OrderInfoValidator  extends ValidatorHandler<OrderInfo> implements Validator<OrderInfo> {
    @Override
    public boolean validate(ValidatorContext context, OrderInfo orderInfo) {
        if(orderInfo.getBankName()==null||orderInfo.getBankName().trim().length()==0){
            context.addError(ValidationError.create("银行名称"+ ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(orderInfo.getBankAccount()==null||orderInfo.getBankAccount().trim().length()==0){
            context.addError(ValidationError.create("银行卡号"+ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(orderInfo.getLoanAmount()==null){
            context.addError(ValidationError.create("贷款金额"+ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }else if(orderInfo.getBorrowTime()==null){
            context.addError(ValidationError.create("借款时间"+ErrorCode.PARAM_NULL.getMsg()).setErrorCode(ErrorCode.PARAM_NULL.getCodeInt()));
            return false;
        }
        return true;
    }
}
