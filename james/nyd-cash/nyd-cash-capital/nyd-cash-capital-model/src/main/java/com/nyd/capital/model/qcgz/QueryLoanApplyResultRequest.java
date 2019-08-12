package com.nyd.capital.model.qcgz;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询放款状态请求参数
 * @author cm
 */

@Data
public class QueryLoanApplyResultRequest implements Serializable{

    //渠道号
    private String channelCode;

    //资产编号
    private String assetId;

    //签名
    private String sign;


}
