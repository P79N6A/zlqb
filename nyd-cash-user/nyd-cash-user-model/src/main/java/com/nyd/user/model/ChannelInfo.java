package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by zhujx on 2017/12/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChannelInfo {

    //渠道编号
    private String channelNo;

    //渠道名称
    private String channelName;

    //引流页h5地址
    private String url;

    //操作系统
    private String os;

    //价格
    private BigDecimal price;

    //计费方式
    private String chargeType;
}
