package com.nyd.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class ChannelInfo implements Serializable {
    /**
     *渠道编码
     */
    private String channelCode;
    /**
     *渠道名称
     */
    private String channelName;
    /**
     *支持银行列表
     */
    private String channelBank;
    /**
     *支持银行列表
     */
    private List<String> channelBanks;
    /**
     *支持银行列表
     */
    private List<BankCodeInfo> bankList;
    /**
     *渠道分流比例
     */
    private Integer channelRatio;
    /**
     *渠道分流上限数
     */
    private Integer channelLimit;
    /**
     *是否使用(0-使用，1-不使用）
     */
    private Integer ifUse;
    /**
     *是否使用(0-使用，1-不使用）
     */
    private String ifUseStr;
    /**
     *使用比例(0-使用，1-不使用)
     */
    private Integer ratioUse;
    /**
     *使用份数上限（0-使用，1-不使用）
     */
    private Integer limitUse;
    /**
     * 是否已删除
     */
    private Integer deleteFlag;
    /**
     * 是否需要资金风控
     */
    private Integer ifRisk;
    /**
     * 不支持时间
     */
    private String closeTimes;
    
    private List<String> timesList;
    
    /**
     *最大放款金额/天
     */
    private BigDecimal maxAmount;
    /**
     *最大放款金额/天
     */
    private String maxAmountStr;
}
