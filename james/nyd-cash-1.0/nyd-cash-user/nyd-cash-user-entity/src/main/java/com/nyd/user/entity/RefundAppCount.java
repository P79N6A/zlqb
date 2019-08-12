package com.nyd.user.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_refund_app_count")
public class RefundAppCount implements Serializable{
    @Id
    private Long id;
    //app code
    private String appCode;
    //统计日期
    private Date countDate;
    //点击数
    private Integer clickCount;
    //注册数
    private Integer registerCount;

}
