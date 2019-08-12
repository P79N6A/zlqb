package com.nyd.admin.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/10/31 19:59
 */
@Data
public class WithholdDetails implements Serializable {

    private List<ResponseVo> data;

    private String status;

    @Data
    public static class ResponseVo {

        //交易订单号
        private String orderNo;
        //代扣金额
        private BigDecimal amount;
        //状态
        private Integer state;
        //代扣时间
        private Date updateTime;
        //支付渠道
        private String channel;

    }
}
