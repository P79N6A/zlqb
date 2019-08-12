package com.zhiwang.zfm.common.response.bean.report;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("费用统计报表返回")
public class ReportCostRespVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String platCode; // 平台编码
	@ApiModelProperty(value = "平台名称")
	private String platName; // 平台名称
	@ApiModelProperty(value = "统计日期(yyyy-MM-dd)")
	private String runDate; // 统计日期
	@ApiModelProperty(value = "总费用")
	private BigDecimal totalMoney; // 总费用
	@ApiModelProperty(value = "已发短信条数")
	private long smsNum; // 已发短信条数
	@ApiModelProperty(value = "短信费用")
	private BigDecimal smsMoney; // 短信费用
	@ApiModelProperty(value = "ocr识别数")
	private long ocrNum; // ocr识别数
	@ApiModelProperty(value = "ocr识别费用")
	private BigDecimal ocrMoney; // ocr识别费用
	@ApiModelProperty(value = "人脸验证数")
	private long faceVerifyNum; // 人脸验证数
	@ApiModelProperty(value = "人脸验证费用")
	private BigDecimal faceVerifyMoney; // 人脸验证费用
	@ApiModelProperty(value = "话单授权数")
	private long callRecordAuthNum; // 话单授权数
	@ApiModelProperty(value = "话单授权费用")
	private BigDecimal callRecordAuthMoney; // 话单授权费用
	@ApiModelProperty(value = "风控审核数")
	private long riskVerifyNum; // 风控审核数
	@ApiModelProperty(value = "风控审核费用")
	private BigDecimal riskVerifyMoney; // 风控审核费用

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

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = setScale(totalMoney);
	}

	public long getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(long smsNum) {
		this.smsNum = smsNum;
	}

	public BigDecimal getSmsMoney() {
		return smsMoney;
	}

	public void setSmsMoney(BigDecimal smsMoney) {
		this.smsMoney = setScale(smsMoney);
	}

	public long getOcrNum() {
		return ocrNum;
	}

	public void setOcrNum(long ocrNum) {
		this.ocrNum = ocrNum;
	}

	public BigDecimal getOcrMoney() {
		return ocrMoney;
	}

	public void setOcrMoney(BigDecimal ocrMoney) {
		this.ocrMoney = setScale(ocrMoney);
	}

	public long getFaceVerifyNum() {
		return faceVerifyNum;
	}

	public void setFaceVerifyNum(long faceVerifyNum) {
		this.faceVerifyNum = faceVerifyNum;
	}

	public BigDecimal getFaceVerifyMoney() {
		return faceVerifyMoney;
	}

	public void setFaceVerifyMoney(BigDecimal faceVerifyMoney) {
		this.faceVerifyMoney = setScale(faceVerifyMoney);
	}

	public long getCallRecordAuthNum() {
		return callRecordAuthNum;
	}

	public void setCallRecordAuthNum(long callRecordAuthNum) {
		this.callRecordAuthNum = callRecordAuthNum;
	}

	public BigDecimal getCallRecordAuthMoney() {
		return callRecordAuthMoney;
	}

	public void setCallRecordAuthMoney(BigDecimal callRecordAuthMoney) {
		this.callRecordAuthMoney = setScale(callRecordAuthMoney);
	}

	public long getRiskVerifyNum() {
		return riskVerifyNum;
	}

	public void setRiskVerifyNum(long riskVerifyNum) {
		this.riskVerifyNum = riskVerifyNum;
	}

	public BigDecimal getRiskVerifyMoney() {
		return riskVerifyMoney;
	}

	public void setRiskVerifyMoney(BigDecimal riskVerifyMoney) {
		this.riskVerifyMoney = setScale(riskVerifyMoney);
	}

}
