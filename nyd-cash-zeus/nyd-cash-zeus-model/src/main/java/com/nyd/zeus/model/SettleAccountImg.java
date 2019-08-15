package com.nyd.zeus.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="平账凭据参数对象")
public class SettleAccountImg implements Serializable {
    @ApiModelProperty(value="账单编号",required=true)
    private String billNo;
}
