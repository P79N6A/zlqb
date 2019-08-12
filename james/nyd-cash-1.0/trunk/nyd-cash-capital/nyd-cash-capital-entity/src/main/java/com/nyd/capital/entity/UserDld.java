package com.nyd.capital.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_dld")
public class UserDld implements Serializable {
	@Id
    private Long id;

    private String userId;

    private String dldCustomerId;

    private String mobile;
    /**
     * 用户进度(0-未开户,1-已开户)
     */
    private Integer stage;
    private String contractUrl;
    private Date createTime;
    private Date updateTime;
    private String updateBy;
    //是否已删除
    private Integer deleteFlag;
    
}
