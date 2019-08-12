package com.zhiwang.zfm.common.response.bean.report;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("资产概况查询返回")
public class ReportAssetsOverviewRespVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String platCode; // 平台编码
	@ApiModelProperty(value = "平台名称")
	private String platName; // 平台名称
	@ApiModelProperty(value = "统计日期(yyyy-MM-dd)")
	private String runDate; // 统计日期
	@ApiModelProperty(value = "注册数")
	private long registerNum; // 注册数
	@ApiModelProperty(value = "申请数")
	private long applyNum; // 申请数
	@ApiModelProperty(value = "通过数")
	private long passNum; // 通过数
	@ApiModelProperty(value = "放款数")
	private long loanMoneyNum; // 放款数
	@ApiModelProperty(value = "放款金额")
	private BigDecimal loanMoneySum; // 放款金额
	@ApiModelProperty(value = "实付金额")
	private BigDecimal actualAmountPaid; // 实付金额
	@ApiModelProperty(value = "服务费")
	private BigDecimal serviceCharge; // 服务费
	@ApiModelProperty(value = "复贷数")
	private long loanAgainNum; // 复贷数
	@ApiModelProperty(value = "展期数")
	private long extensionNum; // 展期数
	@ApiModelProperty(value = "已还数")
	private long alreadyMoneyNum; // 已还数
	@ApiModelProperty(value = "已还金额")
	private BigDecimal alreadyMoneySum; // 已还金额
	@ApiModelProperty(value = "总待收数")
	private long allCollectedCount; // 总待收数
	@ApiModelProperty(value = "总待收金额")
	private BigDecimal allCollectedNum; // 总待收金额
	@ApiModelProperty(value = "正常待收数")
	private long normalCollectedCount; // 正常待收数
	@ApiModelProperty(value = "正常待收金额")
	private BigDecimal normalCollectedNum; // 正常待收金额

	/**
	 * 四舍五入
	 * 
	 * @param bd
	 * @return
	 */
	private BigDecimal setScale(BigDecimal bd) {
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public long getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(long registerNum) {
		this.registerNum = registerNum;
	}

	public long getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(long applyNum) {
		this.applyNum = applyNum;
	}

	public long getPassNum() {
		return passNum;
	}

	public void setPassNum(long passNum) {
		this.passNum = passNum;
	}

	public long getLoanMoneyNum() {
		return loanMoneyNum;
	}

	public void setLoanMoneyNum(long loanMoneyNum) {
		this.loanMoneyNum = loanMoneyNum;
	}

	public BigDecimal getLoanMoneySum() {
		return loanMoneySum;
	}

	public void setLoanMoneySum(BigDecimal loanMoneySum) {
		this.loanMoneySum = setScale(loanMoneySum);
	}

	public BigDecimal getActualAmountPaid() {
		return actualAmountPaid;
	}

	public void setActualAmountPaid(BigDecimal actualAmountPaid) {
		this.actualAmountPaid = setScale(actualAmountPaid);
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = setScale(serviceCharge);
	}

	public long getLoanAgainNum() {
		return loanAgainNum;
	}

	public void setLoanAgainNum(long loanAgainNum) {
		this.loanAgainNum = loanAgainNum;
	}

	public long getExtensionNum() {
		return extensionNum;
	}

	public void setExtensionNum(long extensionNum) {
		this.extensionNum = extensionNum;
	}

	public long getAlreadyMoneyNum() {
		return alreadyMoneyNum;
	}

	public void setAlreadyMoneyNum(long alreadyMoneyNum) {
		this.alreadyMoneyNum = alreadyMoneyNum;
	}

	public BigDecimal getAlreadyMoneySum() {
		return alreadyMoneySum;
	}

	public void setAlreadyMoneySum(BigDecimal alreadyMoneySum) {
		this.alreadyMoneySum = setScale(alreadyMoneySum);
	}

	public long getAllCollectedCount() {
		return allCollectedCount;
	}

	public void setAllCollectedCount(long allCollectedCount) {
		this.allCollectedCount = allCollectedCount;
	}

	public BigDecimal getAllCollectedNum() {
		return allCollectedNum;
	}

	public void setAllCollectedNum(BigDecimal allCollectedNum) {
		this.allCollectedNum = setScale(allCollectedNum);
	}

	public long getNormalCollectedCount() {
		return normalCollectedCount;
	}

	public void setNormalCollectedCount(long normalCollectedCount) {
		this.normalCollectedCount = normalCollectedCount;
	}

	public BigDecimal getNormalCollectedNum() {
		return normalCollectedNum;
	}

	public void setNormalCollectedNum(BigDecimal normalCollectedNum) {
		this.normalCollectedNum = setScale(normalCollectedNum);
	}

}
