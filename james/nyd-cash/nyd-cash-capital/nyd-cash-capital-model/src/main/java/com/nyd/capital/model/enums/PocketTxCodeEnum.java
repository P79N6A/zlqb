package com.nyd.capital.model.enums;


/**
 * @author liuqiu
 */

public enum PocketTxCodeEnum {
    queryAccountOpenDetailByMobile("queryAccountOpenDetailByMobile", "口袋理财手机号码查询用户开户信息接口"),
    complianceBorrowPage("complianceBorrowPage", "口袋理财借款合规页面接口"),
    passwordResetPage("passwordResetPage", "口袋理财用户密码设置/重置密码接口"),
    accountOpenEncryptPage("accountOpenEncryptPage", "口袋理财用户开户设密页面接口"),
    termsAuthPage("termsAuthPage", "口袋理财用户还款授权接口接口"),
    createOrderLendPay("createOrderLendPay", "口袋理财推单接口"),
    withdraw("withdraw", "口袋理财提现接口"),
    queryOrderWithdrawStatus("queryOrderWithdrawStatus", "口袋理财查询提现状态接口"),
    pushAssetRepaymentPeriod("pushAssetRepaymentPeriod", "口袋理财创建债权还款计划接口"),
    queryAccountOpenDetail("queryAccountOpenDetail", "口袋理财身份证号码查询用户开户信息接口");

    private String code;
    private String name;

    PocketTxCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static PocketTxCodeEnum toEnum(String code) {
        for (PocketTxCodeEnum type : PocketTxCodeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }

        }
        return null;
    }

}
