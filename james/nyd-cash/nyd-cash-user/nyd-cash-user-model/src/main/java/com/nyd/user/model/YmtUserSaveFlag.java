package com.nyd.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;

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
@AllArgsConstructor
@ToString
public class YmtUserSaveFlag implements Serializable {
	
	private boolean saveUser;
	private boolean updateUser;
	private boolean saveUserDetail;
	private boolean updateUserDetail;
	private boolean saveAccount;
	private boolean updateAccount;
	private boolean saveUserContract;
	private boolean updateUserContract;
	private boolean saveUserJob;
	private boolean updateUserJob;
	private boolean saveUserBank;
	private boolean updateUserBank;
	private boolean saveAccountPassword;
	private boolean updateAccountPassword;
	private boolean saveUserStep;
	private boolean updateUserStep;
	private boolean resetAccountNumber;
	
	public YmtUserSaveFlag() {
		this.saveUser = false;
		this.updateUser = false;
		this.saveUserDetail = false;
		this.updateUserDetail = false;
		this.saveAccount = false;
		this.updateAccount = false;
		this.saveUserContract = false;
		this.updateUserContract = false;
		this.saveUserJob = false;
		this.updateUserJob = false;
		this.saveUserBank = false;
		this.updateUserBank = false;
		this.saveAccountPassword = false;
		this.updateAccountPassword = false;
		this.resetAccountNumber = false;
		this.saveUserStep = false;
		this.updateUserStep = false;
	}
	public YmtUserSaveFlag(boolean flag) {
		this.saveUser = flag;
		this.updateUser = flag;
		this.saveUserDetail = flag;
		this.updateUserDetail = flag;
		this.saveAccount = flag;
		this.updateAccount = flag;
		this.saveUserContract = flag;
		this.updateUserContract = flag;
		this.saveUserJob = flag;
		this.updateUserJob = flag;
		this.saveUserBank = flag;
		this.updateUserBank = flag;
		this.saveAccountPassword = flag;
		this.updateAccountPassword = flag;
		this.saveUserStep = flag;
		this.updateUserStep = flag;
		this.resetAccountNumber = false;
	}
	public YmtUserSaveFlag(boolean saveFlag,boolean updateFlag) {
		this.saveUser = saveFlag;
		this.updateUser = updateFlag;
		this.saveUserDetail = saveFlag;
		this.updateUserDetail = updateFlag;
		this.saveAccount = saveFlag;
		this.updateAccount = updateFlag;
		this.saveUserContract = saveFlag;
		this.updateUserContract = updateFlag;
		this.saveUserJob = saveFlag;
		this.updateUserJob = updateFlag;
		this.saveUserBank = saveFlag;
		this.updateUserBank = updateFlag;
		this.saveAccountPassword = saveFlag;
		this.updateAccountPassword = updateFlag;
		this.saveUserStep = saveFlag;
		this.updateUserStep = updateFlag;
		this.resetAccountNumber = false;
	}

}
