package com.nyd.settlement.entity.repay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_ibank_repay")
public class YmtPayFlow {

    //主键id
    @Id
    private Long Id;

    private String userId;//用户id

    private String mobile;//电话

    private String repayNo;//支付编号

    private String tradeNo;//账单编号

    private Date repayTime;//支付时间

    private BigDecimal repayAmount;//支付金额

    private String repayName;//支付姓名

    private String repayIdNumber;//代付身份证号

    private String repayAccount;//银行卡号

    private String repayChannel;//支付通道zfb

    private Integer repayStatus;//'还款状态  0：成功；1：失败',

    private Integer repayLevel;

    private Integer repayType;//'评估费 推荐费',

    private String buyerZfbId;

    private String buyerLogonId;//支付宝账号

    private Date actualRecordedTime;//实际入账日

    private BigDecimal thirdProcedureFee;//第三方手续费

    private Integer deleteFlag;//0：正常；1：已删除',

    private Date createTime;//添加时间

    private Date updateTime;//修改时间

    private String updateBy;//最后修改人

    private Integer derateType;//减免类型

    private BigDecimal derateAmount;//减免金额

    private String remark;


}
