package com.nyd.application.model.request;

import lombok.Data;

/**
 * Created by hwei on 2017/11/18.
 */
@Data
public class FadadaNotifyModel {
    private String transaction_id;//交易号
    private String contract_id;//合同编号
    private String result_code;//签章结果代码
    private String result_desc;//签章结果描述
    private String download_url;//下载地址
    private String viewpdf_url;//查看地址
    private String timestamp;//请求时间 yyyyMMDDHHmmss
    private String msg_digest;//摘要
}
