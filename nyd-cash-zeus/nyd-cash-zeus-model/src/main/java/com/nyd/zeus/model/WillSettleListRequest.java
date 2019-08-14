package com.nyd.zeus.model;

import com.nyd.zeus.model.common.PageCommon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by dengqingfeng on 2019/8/13.
 * 待销账
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value="待销账列表查询")
public class WillSettleListRequest extends PageCommon implements Serializable {
    //  客户姓名
    @ApiModelProperty("客户姓名")
    private String userName;
    // 手机号码
    @ApiModelProperty("手机号码")
    private String userMobile;
    // 贷款编号
    @ApiModelProperty("贷款编号")
    private String orderNo;
}
