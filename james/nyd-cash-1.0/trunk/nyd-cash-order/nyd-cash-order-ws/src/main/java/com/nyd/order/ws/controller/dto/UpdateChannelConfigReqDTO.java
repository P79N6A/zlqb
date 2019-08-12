package com.nyd.order.ws.controller.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author shaoqing.liu
 * @date 2018/10/12 10:58
 */
@Data
@ToString
public class UpdateChannelConfigReqDTO {

    /**主键**/
    private Long id;

    /**
     *渠道名称
     */
    private String channelName;

    /**
     *渠道分流比例
     */
    private int channelRatio;
    /**
     *渠道分流上限数
     */
    private int channelLimit;
}
