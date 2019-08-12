package com.nyd.admin.model;

import com.nyd.admin.model.annotation.ExportConfig;
import lombok.Data;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Data
public class RepayReportVo {
    /**
     *
     */
    @ExportConfig("订单号")
    private String orderNo;

    /**
     *
     */
    @ExportConfig("客户姓名")
    private String customerName;

    /**
     *
     */
    @ExportConfig("手机号码")
    private String mobile;

    /**
     *
     */
    @ExportConfig("贷款号")
    private String loanNo;

    /**
     *
     */
    @ExportConfig("资方名称")
    private String capitalName;

    /**
     * 产品类型 1 单期
     */
    @ExportConfig("产品类型")
    private Integer productType;

    /**
     *
     */
    @ExportConfig("标签")
    private String label;

    /**
     * 0测试标 1非测试标
     */
    @ExportConfig("是否测试标")
    private Integer testStatus;

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
     * 逾期天数  不逾期为0
     */
    @ExportConfig("最大逾期天数")
    private Integer overdueDay;

    /**
     * M0......
     */
    @ExportConfig("逾期阶段")
    private String overduePeriod;

    /**
     * 借款天数 合同期限
     */
    @ExportConfig("合同期限")
    private Integer contractTime;

    /**
     *
     */
    @ExportConfig("交易流水号")
    private String serialNo;

    /**
     * 还款渠道
     */
    @ExportConfig("还款渠道")
    private String repayChannel;

    /**
     *
     */
    @ExportConfig("实际收款日")
    private String actualReceiptDay;

    /**
     * 合同金额
     */
    @ExportConfig("合同金额")
    private String contractAmount;

    /**
     *
     */
    @ExportConfig("放款金额")
    private String remitAmount;

    /**
     *
     */
    @ExportConfig("费用项")
    private String feeItem;

    /**
     * 逾期应还金额
     */
    @ExportConfig("逾期应还金额")
    private String overdueShouldAmount;

    /**
     * 本笔实收金额
     */
    @ExportConfig("本笔实收金额")
    private String receiveAmount;

    /**
     * 累计还款金额
     */
    @ExportConfig("累计还款金额")
    private String accumRepayAmount;

    /**
     * 第三方手续费
     */
    @ExportConfig("第三方手续费")
    private String thirdPartyPoundage;

    /**
     * 本笔实收金额
     */
    @ExportConfig("本笔实收合同金额")
    private String thisContractAmount;

    /**
     * 本笔实收服务费金额
     */
    @ExportConfig("本笔实收服务费金额")
    private String thisFeeService;

    /**
     * 本笔实收利息金额
     */
    @ExportConfig("本笔实收利息金额")
    private String thisInterestAmount;

    /**
     * 本笔实收滞纳金
     */
    @ExportConfig("本笔实收滞纳金")
    private String thisFeeLate;

    /**
     * 本笔实收逾期罚息
     */
    @ExportConfig("本笔实收逾期罚息")
    private String thisOverdueFaxi;

    /**
     * 代偿金额
     */
    @ExportConfig("代偿金额")
    private String compensatoryAmount;

    /**
     * 活动减免
     */
    @ExportConfig("活动减免")
    private String activityDerate;

    /**
     *
     */
    @ExportConfig("催收减免")
    private String collectionDerate;

    /**
     *
     */
    @ExportConfig("应退款")
    private String drawbackAmount;

    /**
     * 其他收入
     */
    @ExportConfig("其他收入")
    private String otherIncome;

    /**
     *
     */
    @ExportConfig("会员费ID")
    private String memberFeeId;

    /**
     * 会员费 应付金额
     */
    @ExportConfig("会员费应付金额")
    private String memberFeeAmount;

    /**
     * 会员费支付金额
     */
    @ExportConfig("会员费支付金额")
    private String memberFeePay;

    /**
     * 会员费支付日期
     */
    @ExportConfig("会员费支付日期")
    private String memberFeePayDate;

    /**
     *
     */
    @ExportConfig("会员费支付渠道")
    private String memberFeePayChannel;

    /**
     * 过期时间
     */
    @ExportConfig("会员费到期日")
    private String expireTime;

}
