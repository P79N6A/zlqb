package com.nyd.capital.model.kzjr;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/18
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchAssetSubmitRequest implements Serializable{
    private String channelCode;
    private List<BatchAssetDetail> batchData;
    private String sign;

    public String toString(){
        return "channelCode="+channelCode+"&batchData="+JSON.toJSONString(batchData)+"&sign="+sign;
    }

    public Map<String,String> getMapWithoutSign(){
        Map<String,String> map = new HashMap<>();
        map.put("channelCode",channelCode);
        map.put("batchData", JSON.toJSONString(batchData));

        return map;
    }
}
