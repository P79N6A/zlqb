package com.creativearts.nyd.pay.service.baofoo;

import com.creativearts.nyd.pay.model.baofoo.*;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface JoinPayWithholdService {

    /**
     * 查询用户是否绑定宝付代扣银行卡
     * @param dto
     * @return
     */
    ResponseData<SelectBankCardResult> selectBankCard(SelectBankCardDto dto);

    /**
     * 发起宝付代扣
     * @param dto
     * @return
     */
    ResponseData withHold(BaoFooWithHoldDto dto);

    ResponseData payCallback(PayCallbackDTO dto);

    ResponseData withHoldResult(BaoFooWithHoldResultDto dto);

    /**
     * 查询交易状态代扣中的代扣单
     * @return
     */
    ResponseData selectStatusOne();

    /**
     * 查询代扣结果
     * @return
     */
    ResponseData withHoldOrder(String payOrderNo);
}
