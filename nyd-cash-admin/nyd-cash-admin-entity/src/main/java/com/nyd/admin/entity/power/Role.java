package com.nyd.admin.entity.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author hwei
 * @create 2018-01-04
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_role")
public class Role {
    @Id
    private Long id;
    //角色名称
    private String roleName;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
