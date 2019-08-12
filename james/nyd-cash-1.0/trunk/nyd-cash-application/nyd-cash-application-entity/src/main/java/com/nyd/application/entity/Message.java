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
@Table(name = "t_message")
public class Message {
    @Id
    private Long id;
    //手机号
    private String mobile;
    //留言信息
    private String content;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
