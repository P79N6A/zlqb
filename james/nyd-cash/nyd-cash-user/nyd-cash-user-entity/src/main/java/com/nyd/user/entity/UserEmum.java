package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hwei on 2017/11/1.
 * 用户枚举信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_emum")
public class UserEmum {
    @Id
    private Long id;
    //枚举域
    private String catagory;
    //枚举域描叙
    private String catagoryName;
    //枚举值
    private String key;
    //枚举描述
    private String value;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
