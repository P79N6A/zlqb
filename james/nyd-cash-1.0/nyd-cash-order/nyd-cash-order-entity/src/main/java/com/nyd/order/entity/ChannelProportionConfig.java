package com.nyd.order.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
public class ChannelProportionConfig implements Serializable {
    /**
     *  主键id
     */
    @Id
    private Long id;
    /**
     *渠道编码
     */
    private String channelCode;
    /**
     *渠道名称
     */
    private String channelName;
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
     * 添加时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 资金渠道关闭时间
     */
    private String closeTimes;
    /**
     * 是否需要资金风控
     */
    private Integer ifRisk;
    /**
     *最大放款金额/天
     */
    private BigDecimal maxAmount;
}
