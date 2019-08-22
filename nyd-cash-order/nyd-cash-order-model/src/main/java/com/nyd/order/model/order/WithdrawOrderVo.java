package com.nyd.order.model.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WithdrawOrderVo implements Serializable {
    @ApiModelProperty(value = "订单编号集合")
    private List<String> orderNoList;
}
