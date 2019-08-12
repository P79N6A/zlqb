package com.nyd.settlement.model.dto;

import com.nyd.settlement.model.paging.Paging;
import lombok.Data;

/**
 * 退款详情，前台所传参数实体类
 */
@Data
public class RefundDetailDto extends Paging {

    private String startDate;//开始日期

    private String endDate;//结束日期

    private String mobile;//手机号

    private String orderNo;//客户编号

    private String refundType;//费用类型(2：目前只有推荐费，)




}
