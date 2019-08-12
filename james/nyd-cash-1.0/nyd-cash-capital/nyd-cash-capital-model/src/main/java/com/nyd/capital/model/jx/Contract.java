package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
public class Contract implements Serializable {
    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * 合同编号
     */
    private String code;
}
