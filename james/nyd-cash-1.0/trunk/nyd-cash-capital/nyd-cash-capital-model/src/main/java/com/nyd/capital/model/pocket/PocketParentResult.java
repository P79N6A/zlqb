package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PocketParentResult {

    /**
     * 状态码
     */
    private String retCode;

    /**
     * 返回数据
     */
    private String retData;

    /**
     * 状态码解释
     */
    private String retMsg;

    /**
     * 当前调用的唯一码
     */
    private String retTraceld;

    /**
     * 接口版本号
     */
    private String version;

    /**
     * 业务类型码
     */
    private String txCode;

    /**
     * 签名
     */
    private String sign;

}
