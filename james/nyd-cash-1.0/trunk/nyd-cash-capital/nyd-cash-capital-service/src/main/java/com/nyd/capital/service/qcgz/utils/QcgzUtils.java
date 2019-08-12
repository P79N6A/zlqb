package com.nyd.capital.service.qcgz.utils;

import com.nyd.capital.model.qcgz.LoanApplyRequest;
import com.nyd.capital.model.qcgz.LoanSuccessNotifyRequest;
import com.nyd.capital.model.qcgz.QueryLoanApplyResultRequest;
import com.nyd.capital.model.qcgz.SubmitAssetRequest;
import com.nyd.capital.model.qcgz.enums.BankNameResetEnum;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class QcgzUtils {

    /**
     * 将参数添加到map集合
     * @return map
     */

    public static Map<String,String> getMapWithoutSignSubmitAsset(SubmitAssetRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",request.getChannelCode());
        map.put("orderId",request.getOrderId());
        map.put("name",request.getName());

        if (StringUtils.isNotBlank(request.getInvestorList())){
            map.put("investorList",request.getInvestorList());
        }

        map.put("bidType",request.getBidType());
        map.put("sex",String.valueOf(request.getSex()));
        map.put("mobile",request.getMobile());
        map.put("idCardNumber",request.getIdCardNumber());
        map.put("bankName",getResetBankName(request.getBankName()));
        map.put("bankCardNo",request.getBankCardNo());

        if (StringUtils.isNotBlank(request.getLoanUse())){
            map.put("loanUse",request.getLoanUse());
        }

        if (request.getIncome() != null){
            map.put("income",String.valueOf(request.getIncome()));
        }


        if (StringUtils.isNotBlank(request.getAddress())){
            map.put("address",request.getAddress());
        }

        if (StringUtils.isNotBlank(request.getBirthPlace())){
            map.put("birthPlace",request.getBirthPlace());
        }

        if (String.valueOf(request.getMarriageState()) != null){
            map.put("marriageState",String.valueOf(request.getMarriageState()));
        }

        map.put("periods",String.valueOf(request.getPeriods()));
        map.put("periodsType",String.valueOf(request.getPeriodsType()));
        map.put("amount",String.valueOf(request.getAmount()));
        map.put("rates",String.valueOf(request.getRates()));

        return map;
    }


    public static Map<String,String> getMapWithoutSignSubmitLoanApply(LoanApplyRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",request.getChannelCode());
        map.put("assetId",request.getAssetId());
        map.put("bankName",request.getBankName());
        map.put("bankCardNo",request.getBankCardNo());
        return map;
    }


    public static Map<String,String> getMapWithoutSignQueryLoanApplyResult(QueryLoanApplyResultRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",request.getChannelCode());
        map.put("assetId",request.getAssetId());
        return map;
    }

    public static Map<String,String> getMapWithoutSignLoanSucceesNotify(LoanSuccessNotifyRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",request.getChannelCode());
        map.put("assetId",request.getAssetId());
        map.put("loanTime",request.getLoanTime());
        map.put("loanOrderStatus",String.valueOf(request.getLoanResult()));
        return map;
    }
    
    /**
     * 获取七彩格子对应银行名称
     * @param bankName
     * @return
     */
    private static String getResetBankName(String bankName) {
    	String reset = "";
    	BankNameResetEnum bank = BankNameResetEnum.getByValue(bankName);
    	if(bank != null) {
    		reset = bank.getResetName();
    	}else {
    		reset = bankName;
    	}
    	return reset;
    }

}
