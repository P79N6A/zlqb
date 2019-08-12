package com.nyd.admin.model.Info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author zhangdk
 *
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderExceptionRequst implements Serializable {

    //审核结果:0待审1通过2拒绝
    private Integer auditStatus;
    //审核结果:0待审1通过2拒绝
    private String auditStatu;

    //订单状态异常状态 1000初始状态／1002处理中／1001已放款／1003放款失败
    private Integer orderStatus;

    //订单列表
    private List<String> orderNos;
    
    //渠道编码
    private String fundCode;
    
    private String startDate;
    
    private String endDate;
    
    //起始页
    private Integer pageNum;

    //每页和数
    private Integer pageSize;


}
