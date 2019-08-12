package com.nyd.zeus.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReminderDistributionRequest extends ReadyDistributionRequest implements
		Serializable {

	@ApiModelProperty("信审人员")
	private String creditTrialUserName;
	@ApiModelProperty("受理人员")
	private String receiveUserName;

}
