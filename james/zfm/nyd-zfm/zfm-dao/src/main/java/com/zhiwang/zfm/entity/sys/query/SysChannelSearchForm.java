package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysChannelSearchForm extends BaseSearchForm {
	
		private String id;			//id	private String platCode;			//平台编码	private String platName;			//平台名称	private String channelCode;			//渠道编码	private String channelName;			//渠道名称	private String channelStatus;			//Y:有效,N:无效	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getPlatCode() {	    return this.platCode;	}	public void setPlatCode(String platCode) {	    this.platCode=platCode;	}	public String getPlatName() {	    return this.platName;	}	public void setPlatName(String platName) {	    this.platName=platName;	}	public String getChannelCode() {	    return this.channelCode;	}	public void setChannelCode(String channelCode) {	    this.channelCode=channelCode;	}	public String getChannelName() {	    return this.channelName;	}	public void setChannelName(String channelName) {	    this.channelName=channelName;	}	public String getChannelStatus() {	    return this.channelStatus;	}	public void setChannelStatus(String channelStatus) {	    this.channelStatus=channelStatus;	}
	
}
