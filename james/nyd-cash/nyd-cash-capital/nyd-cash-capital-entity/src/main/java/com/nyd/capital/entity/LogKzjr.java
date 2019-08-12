package com.nyd.capital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/5/2
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_log_kzjr")
public class LogKzjr {
    //主键id
    @Id
    private Long id;
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;

    private String mobile;
    private String channel;
    private Integer status;

    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
