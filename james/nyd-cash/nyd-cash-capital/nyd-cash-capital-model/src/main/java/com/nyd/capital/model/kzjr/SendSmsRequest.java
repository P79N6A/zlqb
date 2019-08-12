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
public  class SendSmsRequest implements Serializable{
    @RequireField
    private String channelCode;
    @RequireField
    private String mobile;
    @RequireField
    private Integer bizType;

    @RequireField
    private String cardNo;
    @RequireField
    private String sign;
    public String toString(){
        return "channelCode="+channelCode+"&mobile="+mobile+"&bizType="+bizType+"&cardNo="+cardNo+"&sign="+sign;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("mobile",mobile);
        map.put("bizType",bizType+"");
        map.put("cardNo",cardNo);
        return map;
    }
}
