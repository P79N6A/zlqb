package com.creativearts.nyd.pay.service.helibao;

import com.creativearts.nyd.pay.model.helibao.*;
import com.nyd.pay.api.enums.WithHoldType;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
public interface HelibaoPayService {
    ResponseData withHold(CreateOrderVo orderVo, WithHoldType type);
    ResponseData withHoldQuery(QueryOrderVo vo);
    ResponseData withHoldBatch(WithholdBatchVo vo);
    ResponseData withHoldBatchQuery(QueryBatchOrderVo vo);

    String callBack(String callback);

    ResponseData firstPayType(FirstPayCreateOrderVo vo, WithHoldType type);

    ResponseData firstPay(FirstPayCreateOrderVo vo);

    ResponseData firstPayMessage(SendValidateCodeVo vo);

//    ResponseData confirmPay(ConfirmPayVo vo,WithHoldType type);

    ResponseData confirmPay(ConfirmPayVo vo);

    String hlbCallback(String callback);

    ResponseData bindCardMessage(BindCardMessageVo vo);

    ResponseData bindCard(BindCardVo vo);

    ResponseData bindCardPayMessage(BindCardPayMessageVo vo);

    ResponseData bindCardPay(BindCardPayVo vo);

    ResponseData queryOrder(OrderQueryVo vo);

    ResponseData unBindCard(UnBindCardVo vo);

    ResponseData bindCardInformationQuery(BindCardInformationQueryVo vo);
//    ResponseData withHold

}
