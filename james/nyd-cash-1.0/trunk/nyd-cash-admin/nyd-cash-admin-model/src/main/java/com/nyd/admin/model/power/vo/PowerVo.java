package com.nyd.admin.model.power.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-03 14:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PowerVo{
    //物理主键
    private Long id;
    //父id
    private Integer pid;
    //权限名称
    private String powerName;
    //权限url
    private String powerUrl;
    //跳转
    private Integer jmp;
    //添加时间
    private Date createTime;
    //状态
    private String status;

}
