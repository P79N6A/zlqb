package com.nyd.admin.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.nyd.admin.model.annotation.ExportConfig;

import lombok.Data;
@Data
public class RefundOrderExportVo implements Serializable{
	
	@ExportConfig("姓名")
	private String userName;
	@ExportConfig("手机号")
	private String accountNumber;
	@ExportConfig("代扣商编")
	private String merchantCode;
	@ExportConfig("业务类型")
	private String businessOrderType;
	@ExportConfig("借款订单号")
	private String refundNo;
	@ExportConfig("支付商户订单号")
	private String payChannelOrder;
	@ExportConfig("银行卡号")
	private String bankAccount;
	@ExportConfig("银行名称")
	private String bankName;
	@ExportConfig("退款金额")
	private BigDecimal refundAmonut;
	
}
