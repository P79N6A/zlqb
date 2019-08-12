package com.nyd.settlement.model.dto;

import com.nyd.settlement.model.paging.Paging;
import lombok.Data;

/**
 * 推荐费查询，前台所传参数实体类
 */
@Data
public class RecommendFeeQueryDto extends Paging {

    private String startDate;//开始日期

    private String endDate;//结束日期

    private String mobile;//手机号

    private String orderNo;//订单号
}
