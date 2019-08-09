package com.nyd.order.api;

import com.nyd.order.model.OrderWentongInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
public interface OrderWentongContract {
    /**
     * 大于等于 startDate  小于endDate
     *
     * @param startDate yyyy-MM-dd HH:mm:ss
     * @param endDate   yyyy-MM-dd HH:mm:ss
     * @return
     */
    ResponseData<List<OrderWentongInfo>> getOrderWTByTime(String startDate, String endDate, String name, String mobile);

    ResponseData save(OrderWentongInfo orderWentongInfo);

    /**
     * 获取资金渠道方
     * @return
     */
    ResponseData<String> getChannel();
}
