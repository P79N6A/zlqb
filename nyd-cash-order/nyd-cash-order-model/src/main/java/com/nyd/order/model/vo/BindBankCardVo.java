package com.nyd.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BindBankCardVo {
    //银行卡号
    private String cardNo;
    //渠道编码
    private String channelCode;
    //渠道名称
    private String channelName;
    //身份证号
    private String idcardNo;
}
