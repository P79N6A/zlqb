package com.nyd.admin.entity.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-03 14:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_power")
public class Power {
    @Id
    private Long id;
    //父id
    private Long pid;
    //权限名称
    private String powerName;
    //权限KEY
    private String powerKey;
    //权限url
    private String powerUrl;
    //跳转
    private Integer jmp;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;
}
