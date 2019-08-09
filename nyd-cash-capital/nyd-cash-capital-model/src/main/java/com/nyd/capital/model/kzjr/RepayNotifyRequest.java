package com.nyd.capital.model.kzjr;

import com.nyd.capital.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepayNotifyRequest implements Serializable{
    @RequireField
    private String channelCode;
    @RequireField
    private String orderId;
    @RequireField
    private Integer repayType;//1、主动还款 2、被动代扣
    @RequireField
    private Integer repayStatus;//1、已还  2、未还
    @RequireField
    private String repayDate;//yyyy-MM-dd
    @RequireField
    private String sign;

    public String toString(){
        return "channelCode="+channelCode+"&orderId="+orderId+"&repayType="+repayType+"&repayStatus="+repayStatus+"&repayDate="+repayDate+"&sign="+sign;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("orderId",orderId);
        map.put("repayType",repayType+"");
        map.put("repayStatus",repayStatus+"");
        map.put("repayDate",repayDate);
        return map;
    }
}
