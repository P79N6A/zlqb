package com.nyd.application.entity;

import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 通讯录表
 * @author admin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_address_book")
public class AddressBook {
	
	
	    @Id
	    private Long id;
	    //手机号
	    private String phoneNo;
	    //留言信息
	    private String content;
	    //添加时间
	    private Date createTime;
	    //更新时间
	    private Date updateTime;
	    //
	    private String versionName;
	    //
	    private String tel;
	    
	    private String userId;
	    
	    private String name;

}
