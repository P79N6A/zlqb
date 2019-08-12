package com.zhiwang.zfm.entity.sys;

public class SysPlatform {
		private String id;			//id	private String platCode;			//平台编码	private String platName;			//平台名称	private String platStatus;			//Y:有效,N:无效	private String platUser;			//ftp账户目录名	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getPlatCode() {	    return this.platCode;	}	public void setPlatCode(String platCode) {	    this.platCode=platCode;	}	public String getPlatName() {	    return this.platName;	}	public void setPlatName(String platName) {	    this.platName=platName;	}	public String getPlatStatus() {	    return this.platStatus;	}	public void setPlatStatus(String platStatus) {	    this.platStatus=platStatus;	}	public String getPlatUser() {	    return this.platUser;	}	public void setPlatUser(String platUser) {	    this.platUser=platUser;	}
}
