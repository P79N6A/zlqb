package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hwei on 2017/11/1.
 * 用户联系人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_contact")
public class Contact  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    private Long id;
    //用户ID
    private String userId;
    //联系人类型
    private String type;
    //联系人姓名
    private String name;
    //联系人手机号
    private String mobile;
    //与联系人的关系
    private String relationship;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
