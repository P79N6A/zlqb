package com.nyd.admin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 客服操作记录表
 * @author cm
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_customer_service_log")
public class CustomerServiceLog {
    //主键id
    @Id
    private Long id;

    //客户id
    private String userId;

    //客户姓名
    private String userName;

    //备注
    private String remark;

    //客服操作人员
    private String operationPerson;

    //添加时间
    private Date createTime;

    //是否已删除 0代表未删除，1代表已删除
    private Integer deleteFlag;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

}
