package com.nyd.order.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zlq on 2019/07/17
 * 订单审核返回对象
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserNoParam implements Serializable{
	//用户Id
	private String userId;

}
