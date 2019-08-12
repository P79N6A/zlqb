package com.nyd.settlement.model.dto;

import com.nyd.settlement.model.paging.Paging;
import lombok.Data;

/**
 * 评估费查询，前台所传参数实体类
 */
@Data
public class ValuationFeeQueryDto extends Paging {
    private String startDate;//开始日期

    private String endDate;//结束日期

    private String mobile;//手机号

    private String userId;//客户编号
}
