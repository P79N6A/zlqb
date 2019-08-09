package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Table(name = "t_fund")
public class Fund {

//    @Id
    private Long id;

    //资金源名称
    private String fundName;

    //资金源编码
    private String fundCode;

    //利率
    private BigDecimal interestRate;

    //放款开始时间
    private Date remitStartTime;

    //放款结束时间
    private Date remitEndTime;

    //最大总放款金额
    private BigDecimal maxRemitAmount;

    /*//已放款总金额
    private BigDecimal alreadyRemitAmount;

    //最大金额/天
    private BigDecimal maxAmountPerDay;

    //当天已放款金额
    private BigDecimal todayRemitAmount;
    */

    //支付通道
    private String payChannel;

 /*   //最大笔数/天
    private BigDecimal maxItemsPerDay;

    //当天已放款笔数
    private BigDecimal todayRemitItems;
*/
    //支付通道费/笔
    private BigDecimal payChannelFeePerItem;

    //资金方服务费率
    private BigDecimal fundServiceInterestRate;

    //资金是否启用
    private Integer isInUse;

    //垫资方服务费率
    private BigDecimal padFundServiceInterestRate;

    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}
