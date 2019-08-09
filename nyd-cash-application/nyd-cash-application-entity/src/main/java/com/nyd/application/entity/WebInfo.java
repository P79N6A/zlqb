package com.nyd.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2017/12/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_webinfo")
public class WebInfo {
    @Id
    private Long id;
    //描述
    private String webDescription;
    //key
    private String webKey;
    //值
    private String webValue;

    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
