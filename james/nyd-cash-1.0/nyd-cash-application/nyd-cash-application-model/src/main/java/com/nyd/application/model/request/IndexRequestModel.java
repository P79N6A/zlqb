package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 首页请求参数包
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndexRequestModel implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 产品类型
     */
    private int type;
    /**
     * app名称
     */
    private String appName;
}
