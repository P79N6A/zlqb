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
public class Pocket2CallbackDto {

    /**
     * 状态码:0成功，其他失败
     */
    private String retCode;
    /**
     * 状态码解释:received,rejected
     */
    private String retMsg;
    /**
     * 返回数据
     */
    private String retData;
    /**
     * 接口版本号
     */
    private String version;
    /**
     * 平台标识
     */
    private String platNo;
    /**
     * 业务类型码
     */
    private String txCode;
    /**
     * sign
     */
    private String sign;
}
