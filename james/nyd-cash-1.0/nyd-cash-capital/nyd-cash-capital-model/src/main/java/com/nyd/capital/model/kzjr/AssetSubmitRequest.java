package com.nyd.capital.model.kzjr;

import com.nyd.capital.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssetSubmitRequest implements Serializable{
    @RequireField
    private String channelCode; //渠道号
//    @RequireField
//    private String productCode;//产品id
    @RequireField
    private String orderId;//渠道订单号
    @RequireField
    private String accountId;//借款人账户id
    @RequireField
    private BigDecimal amount;//借款金额
    private Integer type; //1、自然资产 2、通路资产 3、过账资产，默认为资产资产
    private Integer refundDay;//当资产为过账资产或通路资产时，系统会在refundDay后自动退款，默认为2
    @RequireField
    private Integer duration; //借款期限
    @RequireField
    private String sign;

    public String toString(){
        String result = "channelCode="+channelCode+"&orderId="+orderId+"&accountId="+accountId+"&amount="+amount+"&duration="+duration+"&sign="+sign;
        if(type!=null){
            result = result+"&type="+type;
        }
        if(refundDay!=null){
            result = result+"&refundDay="+refundDay;
        }
        return result;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
//        map.put("productCode",productCode);
        map.put("orderId",orderId);
        map.put("accountId",accountId);
        map.put("amount",String.valueOf(amount));
        if(type!=null) {
            map.put("type", type + "");
        }
        if(refundDay!=null) {
            map.put("refundDay", refundDay + "");
        }
        map.put("duration",duration+"");
        return map;
    }

}
