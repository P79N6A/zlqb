package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 * 账单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillDistributionRecordRequest implements Serializable {

  
    // 接收人的id
	@ApiModelProperty(value = "接收人id")
    private String receiveUserId;
    // 接收人的名字
	@ApiModelProperty(value = "接收人姓名")
    private String receiveUserName;
    // 备注
	@ApiModelProperty(value = "分配的orderNos (订单编号)集合 逗号隔开")
    private List<String> orderNos;

}
