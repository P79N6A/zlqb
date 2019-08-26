package com.nyd.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "绑定银行卡查询", description = "绑定银行卡查询")
public class UserBankReq implements Serializable {
    @ApiModelProperty("用户id")
    private String userId;
}
