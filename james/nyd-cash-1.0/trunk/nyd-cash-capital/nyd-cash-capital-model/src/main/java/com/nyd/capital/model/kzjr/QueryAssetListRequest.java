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
public class QueryAssetListRequest implements Serializable{
    @RequireField
    private String channelCode;

    private String day;//格式为yyyyMMdd，可不传如果不传，则默认查当天的
    @RequireField
    private Integer pageNum;//页码,
    @RequireField
    private Integer pageSize;//每页数量，最大值为100
    @RequireField
    private String sign;

    public String toString(){
        String result = "channelCode="+channelCode+"&sign="+sign+"&pageNum="+pageNum+"&pageSize="+pageSize;
        if(day!=null){
            result = result+"&day="+day;
        }

        return result;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        if(day!=null) {
            map.put("day", day);
        }

            map.put("pageNum", pageNum + "");


            map.put("pageSize", pageSize + "");


        return map;
    }
}
