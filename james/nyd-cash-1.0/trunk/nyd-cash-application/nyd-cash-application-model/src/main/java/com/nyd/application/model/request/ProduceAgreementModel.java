package com.nyd.application.model.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * Created by hwei on 2017/11/25.
 */
@Data
public class ProduceAgreementModel {
    private String userId;//用户id
    private String orderId;//订单id
    private JSONObject parameterMap;//pdf需要的参数
}
