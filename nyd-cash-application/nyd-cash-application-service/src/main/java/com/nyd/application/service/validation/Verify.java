package com.nyd.application.service.validation;

import com.nyd.application.model.enums.AgreementType;
import com.nyd.application.model.request.AgreementTemplateModel;
import com.nyd.application.model.request.ExTSignAutoModel;
import com.nyd.application.model.request.GenerateContractModel;
import com.nyd.application.model.request.MuBanRequestModel;
import com.nyd.user.model.BankInfo;

/**
 * Created by hwei on 2017/11/9.
 */
public class Verify {
    public static boolean MuBanRequestModelverify(MuBanRequestModel muBanRequestModel) {
        if (muBanRequestModel!=null){
            if (!Verify.isNotNull(muBanRequestModel.getTemplateId())){
                return false;
            }
            if (!Verify.isNotNull(muBanRequestModel.getPdfUrl())&&muBanRequestModel.getFile()==null){
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean BankInfoverify(BankInfo bankInfo) {
        if (bankInfo!=null){
            if (!Verify.isNotNull(bankInfo.getUserId())){
                return false;
            }
            if (!Verify.isNotNull(bankInfo.getBankAccount())){
                return false;
            }
            if (!Verify.isNotNull(bankInfo.getReservedPhone())){
                return false;
            }
            if (!Verify.isNotNull(bankInfo.getAccountName())){
                return false;
            }
            if (!Verify.isNotNull(bankInfo.getBankName())){
                return false;
            }
            return true;
        }
        return false;
    }


    public static boolean GenerateContractModelverify(GenerateContractModel generateContractModel) {
        if (generateContractModel!=null) {

            if (!Verify.isNotNull(generateContractModel.getDocTitle())) {
                return false;
            }
            if (!Verify.isNotNull(generateContractModel.getTemplateId())) {
                return false;
            }
            if (!Verify.isNotNull(generateContractModel.getContractId())) {
                return false;
            }
            if (generateContractModel.getParameterMap()==null||generateContractModel.getParameterMap().size()==0) {
                return false;
            }
            return true;
        }
        return false;
    }


    public static boolean ExTSignAutoModelverify(ExTSignAutoModel exTSignAutoModel) {
        if (exTSignAutoModel!=null) {

            if (!Verify.isNotNull(exTSignAutoModel.getDocTitle())) {
                return false;
            }
            if (!Verify.isNotNull(exTSignAutoModel.getTransactionId())) {
                return false;
            }
            if (!Verify.isNotNull(exTSignAutoModel.getContractId())) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean AgreementTemplateModelverify(AgreementTemplateModel agreementTemplateModel) {
        if (agreementTemplateModel!=null) {

            if (!Verify.isNotNull(agreementTemplateModel.getKey())) {
                return false;
            }
            if (!Verify.contains(agreementTemplateModel.getType())) {
                return false;
            }
            if (!Verify.isNotNull(agreementTemplateModel.getAppName())) {
                return  false;
            }

            return true;
        }
        return false;
    }


    public static boolean isNotNull(String str){
        if (str!=null&&!"".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static boolean contains(String type){
        if (Verify.isNotNull(type)) {
            for (AgreementType agreementType :AgreementType.values()) {
                if(agreementType.getType().equals(type)){
                    return true;
                }
            }
        }
        return  false;
    }

}
