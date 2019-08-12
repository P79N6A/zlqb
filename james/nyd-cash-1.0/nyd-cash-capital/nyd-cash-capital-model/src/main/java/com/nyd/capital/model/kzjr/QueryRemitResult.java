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
public class QueryRemitResult implements Serializable{
    @RequireField
    private String channelCode;
    @RequireField
    private String orderId;
    @RequireField
    private String sign;

    public String toString(){
        return "channelCode="+channelCode+"&orderId="+orderId+"&sign="+sign;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);

        map.put("orderId",orderId);

        return map;
    }

}
