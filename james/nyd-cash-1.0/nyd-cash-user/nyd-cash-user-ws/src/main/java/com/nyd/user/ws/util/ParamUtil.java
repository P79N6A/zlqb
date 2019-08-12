package com.nyd.user.ws.util;

import com.nyd.user.model.*;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/22
 */
@Component
public class ParamUtil {
    public static String checkJob(JobInfo jobInfo){
        String msg = null;
        if(jobInfo.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(jobInfo.getCompany() == null){
            return msg = "公司名称不能为空";
        }else if(jobInfo.getIndustry() == null){
            return msg = "行业不能为空";
        }else if(jobInfo.getProfession() == null){
            return msg = "职业不能为空";
        }else if(jobInfo.getCompanyProvince() == null){
            return msg = "公司地址省不能为空";
        }else if(jobInfo.getCompanyCity() == null){
            return msg = "公司地址市不能为空";
        }else if(jobInfo.getCompanyDistrict() == null){
            return msg = "公司地址区不能为空";
        }else if(jobInfo.getCompanyAddress() == null){
            return msg = "公司详细地址不能为空";
        }else if(jobInfo.getTelephone() == null){
            return msg = "公司电话不能为空";
        }else if(jobInfo.getSalary() == null){
            return msg = "月收入不能为空";
        }
        return msg;
    }

    public static String checkContact(ContactInfos contactInfo){
        String msg = null;
        if(contactInfo.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(contactInfo.getDirectContactRelation() == null){
            return msg = "直接联系人关系不能为空";
        }else if(contactInfo.getDirectContactName() == null){
            return msg = "直接联系人姓名不能为空";
        }else if(contactInfo.getDirectContactMobile() == null){
            return msg = "直接联系人电话不能为空";
        }else if(contactInfo.getMajorContactRelation() == null){
            return msg = "重要联系人关系不能为空";
        }else if(contactInfo.getMajorContactName() == null){
            return msg = "重要联系人姓名不能为空";
        }else if(contactInfo.getMajorContactMobile() == null){
            return msg = "重要联系人电话不能为空";
        }
        return msg;
    }

    public static String checkBank(BankInfo bankInfo){
        String msg = null;
        if(bankInfo.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(bankInfo.getAccountName() == null){
            return msg = "姓名不能为空";
        }else if(bankInfo.getBankName() == null){
            return msg = "银行名称不能为空";
        }else if(bankInfo.getAccountType() == null){
            return msg = "银行卡类型不能为空";
        }else if(bankInfo.getBankAccount() == null){
            return msg = "银行卡号不能为空";
        }else if(bankInfo.getReservedPhone() == null){
            return msg = "预留手机号不能为空";
        }else if(bankInfo.getSmsCode() == null){
            return msg = "验证码不能为空";
        }
        return msg;
    }

    public static String checkIdentity(IdentityInfo identityInfo){
        String msg = null;
        if(identityInfo.getUserId() == null){
            return msg = "用户ID不能为空";
        }else if(identityInfo.getAccountNumber() == null){
            return msg = "app账号不能为空";
        }else if(identityInfo.getRealName() == null){
            return msg = "姓名不能为空";
        }else if(identityInfo.getIdNumber() == null){
            return msg = "身份证号不能为空";
        }else if(identityInfo.getAccountNumber() == null){
            return msg = "银行卡号不能为空";
        }else if(identityInfo.getMaritalStatus() == null){
            return msg = "婚姻状况不能为空";
        }else if(identityInfo.getHighestDegree() == null){
            return msg = "最高学历不能为空";
        }else if(identityInfo.getBirth() == null){
            return msg = "出身年月不能为空";
        }else if(identityInfo.getNation() == null){
            return msg = "民族不能为空";
        }else if(identityInfo.getIdAddress() == null){
            return msg = "身份证地址不能为空";
        }else if(identityInfo.getSignOrg() == null){
            return msg = "签发机关不能为空";
        }else if(identityInfo.getEffectTime() == null){
            return msg = "有效期不能为空";
        }else if(identityInfo.getLivingProvince() == null){
            return msg = "常住地址省不能为空";
        }else if(identityInfo.getLivingCity() == null){
            return msg = "常住地址市不能为空";
        }else if(identityInfo.getLivingDistrict() == null){
            return msg = "常住地址区不能为空";
        }else if(identityInfo.getLivingAddress() == null){
            return msg = "常住详细地址不能为空";
        }else if(identityInfo.getIdCardFrontPhoto() == null){
            return msg = "身份证正面照不能为空";
        }else if(identityInfo.getIdCardBackPhoto() == null){
            return msg = "身份证反面照不能为空";
        }else if(identityInfo.getFaceRecognition() == null){
            return msg = "人脸识别未验证";
        }else if(identityInfo.getFaceRecognitionSimilarity() == null){
            return msg = "人脸识别相似度不能为空";
        }
        return msg;
    }
}
