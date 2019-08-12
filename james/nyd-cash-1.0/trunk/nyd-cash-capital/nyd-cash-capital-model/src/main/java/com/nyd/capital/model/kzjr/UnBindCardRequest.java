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
 * 2017/12/18
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnBindCardRequest implements Serializable{
    @RequireField
    private String channelCode;
    @RequireField
    private String accountId;
    @RequireField
    private String sign;
    public String toString(){
        return "channelCode="+channelCode+"&accountId="+accountId+"&sign="+sign;
    }
    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("accountId",accountId);
        return map;
    }
}
