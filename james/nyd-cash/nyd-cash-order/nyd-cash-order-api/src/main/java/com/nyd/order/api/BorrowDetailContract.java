package com.nyd.order.api;

import com.nyd.order.model.vo.BorrowDetailVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author chaiming
 */
public interface BorrowDetailContract {

    /**
     * 借款详情(备注：其他人在调用前需要验证是根据订单编号前去查找还是银码头订单号查找)
     */

    /**
     * 根据订单编号查找的
     * @param orderNo
     * @return
     */
    ResponseData<BorrowDetailVo> getBorrowDetailByOrderNo(String orderNo);


    /**
     * 根据银码头订单号查找的
     * @param ibankOrderNo
     * @return
     */
    ResponseData<BorrowDetailVo> getBorrowDetailByIbankOrderNo(String ibankOrderNo);

}
