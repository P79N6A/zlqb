package com.nyd.admin.model;

import com.nyd.admin.model.annotation.ExportConfig;
import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Data
public class RemitReportVo {
    /**
     * 系统 我方订单号
     */
    @ExportConfig("贷款号")
    private String orderNo;

    /**
     * 放款号（与对方关联的号码）
     */
    @ExportConfig("放款订单号")
    private String remitNo;

    /**
     * 客户姓名
     */
    @ExportConfig("客户姓名")
    private String customerName;

    /**
     * 1 单期
     */
    @ExportConfig("产品类型")
    private Integer productType;

    /**
     *
     */
    @ExportConfig("标签")
    private String label;

    /**
     * 手机号
     */
    @ExportConfig("手机号码")
    private String mobile;

    /**
     * 开户行
     */
    @ExportConfig("开户行")
    private String depositBank;

    /**
     * 资金方名称(先空着)
     */
    @ExportConfig("资方名称")
    private String capitalName;

    /**
     * 0测试标 1非测试标
     */
    @ExportConfig("是否测试标")
    private Integer testStatus;

    /**
     *
     */
    @ExportConfig("放款渠道")
    private String remitChannel;

    /**
     *
     */
    @ExportConfig("合同起始日期")
    private String contractStartDate;

    /**
     *
     */
    @ExportConfig("合同截止日期")
    private String contractEndDate;

    /**
     *
     */
    @ExportConfig("借款期限")
    private Integer borrowTime;

    /**
     * 借款金额
     */
    @ExportConfig("借款金额")
    private String borrowAmount;

    /**
     * 放款金额
     */
    @ExportConfig("放款金额")
    private String remitAmount;

    /**
     * 费用项（服务费+资方利息）
     */
    @ExportConfig("费用项")
    private String feeItem;

    /**
     * 活动减免
     */
    @ExportConfig("活动减免")
    private String activityDerate;

    /**
     * 费用合计（费用项-活动减免）
     */
    @ExportConfig("费用合计")
    private String feeTotal;

    /**
     * 代转手续费  （放款渠道代付的手续费 提现手续费）
     */
    @ExportConfig("代转手续费")
    private String feeTransfer;

    /**
     *
     */
    @ExportConfig("通道费用")
    private String feeChannel;

    /**
     * 利率
     */
    @ExportConfig("利率")
    private String interestRate;

    /**
     * 利息
     */
    @ExportConfig("利息")
    private String interest;

    /**
     * 服务费
     */
    @ExportConfig("服务费")
    private String feeService;

    /**
     *
     */
    @ExportConfig("会员费ID")
    private String memberFeeId;

    /**
     *
     */
    @ExportConfig("应收会员费")
    private String memberFee;
    @ExportConfig("实收会员费")
    private String memberFeePay;
    @ExportConfig("应退会员费")
    private String memberFeeDrawBack;

    @ExportConfig("会员费支付日期")
    private String memberFeePayDate;
    @ExportConfig("资金产品编码")
    private String productCode;

    @ExportConfig("借款次数")
    private Integer debitCount;
}
