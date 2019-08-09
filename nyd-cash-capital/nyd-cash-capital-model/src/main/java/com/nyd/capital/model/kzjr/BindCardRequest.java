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
 * 2017/12/13
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BindCardRequest implements Serializable{
    @RequireField
    private String channelCode;
    @RequireField
    private String accountId;
    @RequireField
    private String bankCardNo;
    @RequireField
    private String mobile;
    @RequireField
    private String smsCode;
    @RequireField
    private String sign;


    public String toString(){
        return "channelCode="+channelCode+"&mobile="+mobile+"&accountId="+accountId+"&bankCardNo="+bankCardNo+"&smsCode="+smsCode+"&sign="+sign;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("mobile",mobile);
        map.put("accountId",accountId);
        map.put("bankCardNo",bankCardNo);
        map.put("smsCode",smsCode);
        return map;
    }
}
