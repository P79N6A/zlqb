package com.nyd.capital.service.pocket.service;

import com.nyd.capital.model.pocket.*;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface Pocket2Service {

    /**
     * 口袋理财手机号码查询用户开户信息
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> queryAccountOpenDetailByMobile(PocketQueryAccountOpenDetailByMobileDto dto);

    /**
     * 口袋理财身份证号码查询用户开户信息
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> queryAccountOpenDetail(PocketQueryAccountOpenDetailDto dto);

    /**
     * 口袋理财借款合规页面
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> complianceBorrowPage(PocketComplianceBorrowPageDto dto);

    /**
     * 口袋理财用户密码设置/重置密码
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> passwordResetPage(PocketPasswordResetPageDto dto);

    /**
     * 口袋理财用户开户设密页面
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> accountOpenEncryptPage(PocketAccountOpenEncryptPageDto dto);

    /**
     * 口袋理财用户还款授权接口
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> termsAuthPage(PocketTermsAuthPageDto dto);

    /**
     * 口袋理财提现接口
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> withdraw(PocketWithdrawDto dto);

    /**
     * 口袋理财查询提现状态接口
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> queryOrderWithdrawStatus(PocketQueryOrderWithdrawStatusDto dto);

    /**
     * 口袋理财推单;放款;提现（异步接口)
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> createOrderLendPay(PocketCreateOrderLendPayDto dto);

    /**
     * 口袋理财还款计划
     * @param  dto
     * @return
     */
    ResponseData<PocketParentResult> pushAssetRepaymentPeriod(PocketPushAssetRepaymentPeriodDto dto);

}
