package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/18.
 * 账单表
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillDistributionRecordVo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//主键id
    @Id
    private Long id;
    // bill_id
    private String orderNo;
    // 状态 1 有效 0 失效
    private Integer status;
    // 创建时间
    private Date createTime;
    // 更新时间
    private Date updateTime;
    // 分配人的id
    private String distributionUserId;
    // 分配人的姓名
    private String distributionUserName;
    // 接收人的id
    private String receiveUserId;
    // 接收人的名字
    private String receiveUserName;
    // 备注
    private String remark;
    
    private String payStatus;// 1贷中2贷后

}
