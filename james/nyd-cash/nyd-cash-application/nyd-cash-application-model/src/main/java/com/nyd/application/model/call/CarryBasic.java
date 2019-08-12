package com.nyd.application.model.call;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Table;

import org.springframework.data.annotation.Id;
/**
 * 运营商基础信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
@Data
@Table(name = "t_carry_basic")
@NoArgsConstructor
@AllArgsConstructor
public class CarryBasic implements Serializable{

	private static final long serialVersionUID = -2702998692914978046L;

	@Id
	private String  id;//
	private String  name;//机主姓名
	private String  idCard;//身份证号码
	private String  mobile;//电话号码
	private String userId; //用户id
	private String carrier;//运营商标识(1-中国移动 2-中国电信 3-中国联通)
	private String lastModifyTime;//数据获取时间
	private String  address;//地址
	private String  openTime;//入网时间，格式：yyyy-MM-dd
	private String status;
	private Date createTime;
	private Date updateTime;
	
}
