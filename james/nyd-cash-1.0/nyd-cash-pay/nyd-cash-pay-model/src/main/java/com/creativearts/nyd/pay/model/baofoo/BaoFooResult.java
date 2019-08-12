package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooResult {
    /**
     * 版本号
     */
    private String version;
    /**
     * 请求方保留域
     */
    private String req_reserved;
    /**
     * 附加字段
     */
    private String additional_info;
    /**
     * 应答码
     */
    private String resp_code;
    /**
     * 应答信息
     */
    private String resp_msg;
    /**
     * 商户号
     */
    private String member_id;
    /**
     * 终端号
     */
    private String terminal_id;
    /**
     * 数据类型
     */
    private String data_type;
    /**
     * 预绑卡唯一码
     */
    private String unique_code;
    /**
     * 商户流水号
     */
    private String trans_serial_no;
}
