package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
/**
 * @author liuqiu
 */
@Data
@ToString
public class PocketWithdrawQueryRequest implements Serializable {

    /**
     * 项目名称 koudai_test
     */
    private String project_name;

    /**
     * 订单号
     */
    private String yur_ref;

    /**
     * 签名
     */
    private String sign;
}
