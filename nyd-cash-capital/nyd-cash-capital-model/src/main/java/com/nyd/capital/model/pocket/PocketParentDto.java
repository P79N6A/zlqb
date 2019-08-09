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
public class PocketParentDto{

    /**
     * 版本号
     */
    private String version;

    /**
     * 业务类型码
     */
    private String txCode;

    /**
     * 平台标识
     */
    private String platNo;

    /**
     * 请求时间戳
     */
    private String txTime;

    /**
     * 业务参数
     */
    private String pack;

    /**
     * 签名
     */
    private String sign;

}
