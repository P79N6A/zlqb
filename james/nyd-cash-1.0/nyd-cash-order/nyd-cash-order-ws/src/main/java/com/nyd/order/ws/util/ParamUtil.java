package com.nyd.order.ws.util;

import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.dto.BorrowDto;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/22
 */
@Component
public class ParamUtil {
    public static String checkBorrow(BorrowDto borrowDto){
        String msg = null;
        if(borrowDto.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(borrowDto.getProductCode() == null){
            return msg = "金融产品编号不能为空";
        }
        return msg;
    }

    public static String checkBorrowConfirm(BorrowConfirmDto borrowConfirmDto){
        String msg = null;
        if(borrowConfirmDto.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(borrowConfirmDto.getProductCode() == null){
            return msg = "金融产品编号不能为空";
        }else if(borrowConfirmDto.getLoanAmount() == null){
            return msg = "借款金额不能为空";
        }else if(borrowConfirmDto.getBorrowTime() == null){
            return msg = "借款周期不能为空";
        }else if(borrowConfirmDto.getInterest() == null){
            return msg = "利息不能为空";
        }else if(borrowConfirmDto.getSyntheticalFee() == null){
            return msg = "综合费用不能为空";
        }else if(borrowConfirmDto.getAnnualizedRate() == null){
            return msg = "年化利率不能为空";
        }
        return msg;
    }

}
