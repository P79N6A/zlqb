package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.time.DateUtils;

import com.nyd.admin.model.annotation.ExportConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/8.
 * 订单
 */
@Data
public class OrderExceptionReportVo implements Serializable {
    //订单编号
    @ExportConfig("订单号")
    private String orderNo;
    //用户ID
    @ExportConfig("用户ID")
    private String userId;
    //借款金额
    @ExportConfig("借款金额")
    private BigDecimal loanAmount;
  //手机号
    @ExportConfig("手机号")
    private String accountNumber;
    //真实姓名
    @ExportConfig("真实姓名")
    private String realName;
    //用户银行账户
    @ExportConfig("银行账户")
    private String bankAccount;
    //银行名称
    @ExportConfig("银行名称")
    private String bankName;
    //资金渠道
    @ExportConfig("资金渠道")
    private String fundCode;
    //打款失败时间
    @ExportConfig("打款时间")
    private Date failTime;
   /* //打款失败时间
    @ExportConfig("打款时间")
    private String failTimeStr;*/
    //失败原因
    @ExportConfig("失败原因")
    private String payFailReason;
}
