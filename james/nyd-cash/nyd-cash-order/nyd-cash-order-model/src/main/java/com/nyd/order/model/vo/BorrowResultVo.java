package com.nyd.order.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Dengw on 2017/11/20
 * 借款结果返回对象
 */
@Data
public class BorrowResultVo {
    //订单编号
    private String orderNo;
    //借款金额
    private BigDecimal loanAmount;
    //借款天数（时长）
    private Integer borrowTime;
    //综合费用
    private BigDecimal syntheticalFee;
    //审核结果
    private String auditStatus;
    //银行卡号
    private String bankAccount;
    //银行名称
    private String bankName;
    //订单状态列表
    List<OrderLogVo> orderStatusList;

    /**
     * 机审还是人审(0：机审；1：人审)
     */
    private Integer whoAudit;
    private String fundCode;
    private String realName;
    private String idNumber;
    private String mobile;
    private String ifOpenPage;

    //新增风险等级
    private String finalEvaluation;
}
