package com.nyd.user.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "绑定银行卡出参", description = "绑定银行卡出参")
public class UserBankVo implements Serializable {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("户名")
    private String accountName;
    @ApiModelProperty("卡号")
    private String bankAccount;
    @ApiModelProperty("账户类型")
    private String accountType;
    @ApiModelProperty("预留手机号")
    private String reservedPhone;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("是否默认账号 0 否 1 是")
    private String defaultFlag;
    @ApiModelProperty("是否已删除 0有效")
    private Integer deleteFlag;
    @ApiModelProperty("添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty("用户身份证")
    private String accountIc;
    @ApiModelProperty("银行卡类型 1 畅捷 2 合利宝")
    private Integer soure;
    @ApiModelProperty("业务订单号")
    private String protocolNo;
    @ApiModelProperty("商户单号")
    private String merchantNumber;
    @ApiModelProperty("合利宝 user_id")
    private String hlbUserId;
    @ApiModelProperty("银行编码")
    private String bankCode;
    @ApiModelProperty("渠道")
    private String channelCode;
    @ApiModelProperty("最后修改人")
    private String updateBy;
}
