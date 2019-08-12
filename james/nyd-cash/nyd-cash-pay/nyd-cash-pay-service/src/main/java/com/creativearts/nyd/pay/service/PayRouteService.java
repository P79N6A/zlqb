package com.creativearts.nyd.pay.service;

import com.creativearts.nyd.pay.model.helibao.NydHlbVo;
import com.creativearts.nyd.pay.model.yinshengbao.NydYsbVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Cong Yuxiang
 * 2018/4/20
 **/
public interface PayRouteService {
    ResponseData HlbMemberFeePay(NydHlbVo vo);
    ResponseData HlbReturnPay(NydHlbVo vo);
    ResponseData YsbMemberFeePay(NydYsbVo vo);
    ResponseData YsbReturnPay(NydYsbVo vo);


    ResponseData PayHlbMemberFee(NydHlbVo vo);//支付会员费
    ResponseData PayHlbReturn(NydHlbVo vo);   //还款

    ResponseData PayHlbCashCoupon(NydHlbVo vo);//充值现金券(通过合利宝)

    ResponseData YsbCashCouponPay(NydYsbVo nydYsbVo);//充值现金券(通过银生宝)

    /**
     * 银生宝快捷支付支付评估费(之前的会员费)
     * @param nydYsbVo
     * @return
     */
    ResponseData YsbQuickPayMemberFee(NydYsbVo nydYsbVo);

    /**
     *银生宝快捷支付还款
     * @param nydYsbVo
     * @return
     */
    ResponseData YsbQuickPayReturnPay(NydYsbVo nydYsbVo);


    /**
     *银生宝快捷充值现金券
     * @param nydYsbVo
     * @return
     */
    ResponseData YsbQuickPayCouponPay(NydYsbVo nydYsbVo);

    /**
     * 通过合利宝空中金融还款
     * @param vo
     * @return
     */
    ResponseData PayHlbKzjrRepay(NydHlbVo vo);
}
