package com.nyd.capital.service.kzjr.component;

import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.utils.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@Component
public class KzjrComponent {
    @Autowired
    private KzjrConfig kzjrConfig;

    public String getSign(Map<String,String> map){
        if(map==null||map.size()==0){
            return null;
        }
        List<String> list = new ArrayList<>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getValue()==null||entry.getValue().trim().length()==0){
                continue;
            }
            list.add(entry.getKey());
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String str:list){
            sb.append(str).append("=").append(map.get(str)).append("&");
        }
        String result = sb.substring(0,sb.length()-1);
        result = result+"key="+kzjrConfig.getPrivateKey();
        result = ShaUtils.getSha(result);
        return result.toUpperCase();
    }
}
