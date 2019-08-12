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
 * 2018/2/25
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KzjrOpenAcountRequest implements Serializable {
    @RequireField
    private String channelCode;
    @RequireField
    private Integer idType;
    @RequireField
    private String idNo;
    @RequireField
    private String name;
    @RequireField
    private String mobile;
  /*  @RequireField
    private String cardNo;*/
    @RequireField
    private String returnUrl;
    @RequireField
    private String sign;


    public String toString(){
        return "channelCode="+channelCode+"&mobile="+mobile+"&idType="+idType+"&idNo="+idNo+"&name="+name+"&returnUrl="+returnUrl+"&sign="+sign;
    }
    public String toStringNew(){
        return "channelCode="+channelCode+"&mobile="+mobile+"&idType="+idType+"&idNo="+idNo+"&name="+name+"&returnUrl="+returnUrl;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("mobile",mobile);
        map.put("idType",idType+"");
        map.put("idNo",idNo);
        map.put("name",name);
        /*map.put("cardNo",cardNo);*/
        map.put("returnUrl",returnUrl);
        return map;
    }
}
