package com.nyd.order.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 信审人员信息
 * @author admin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JudgePeople implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	
}
