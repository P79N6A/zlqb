package com.nyd.capital.model.kzjr;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description: 批量放款返回结果
 * @Date: 21:31 2018/5/14
 */
@Data
public class BathFundResult {
    private List<String> sendOrderNos = new ArrayList<String>();

    private Map<String,String> exceptionOrderNos = new HashMap<String,String>();

}
