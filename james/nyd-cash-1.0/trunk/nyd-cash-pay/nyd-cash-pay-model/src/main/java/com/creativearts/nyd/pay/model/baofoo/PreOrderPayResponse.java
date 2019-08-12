package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@Data
public class PreOrderPayResponse {
    private String version;//版本号
    private String req_reserved;//请求方保留域
    private String additional_info;//附加字段
    private String resp_code;//应答码
    private String resp_msg;//应答信息
    private String member_id;//商户号
    private String terminal_id;//终端号
    private String data_type;//加密数据类型
    private String trade_date;//交易日期
    private String trans_id;//商户订单号
    private String business_no;//宝付业务流水号
    private String trans_serial_no;//商户流水号
}
