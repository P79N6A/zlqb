package com.nyd.user.model.ordercheck;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zlq on 2019/07/17
 * 联系人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactsInfoVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//序号
	private String userid;
    //private String seqNumber;
	//联系人关系
	private String contactRelation;
	//联系人姓名
	private String contactName;
	//联系人电话号码
	private String contactPhone;

	
	
}
