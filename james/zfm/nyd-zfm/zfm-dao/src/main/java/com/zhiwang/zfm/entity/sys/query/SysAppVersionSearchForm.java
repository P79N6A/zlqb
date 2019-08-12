package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysAppVersionSearchForm extends BaseSearchForm {
	
		private String id;			//	private Integer deviceType;			//手机类型 2 ios  1 android	private String version;			//版本号	private Integer status;			//是否需要更新  0不需要  1 需要	private String content;			//更新说明	private String url;			//下载地址	private String publisher;			//更新人	private String fileName;			//文件名	private Integer newData;			//1最新  0不是最新	private java.util.Date createTime;			//创建时间	private java.util.Date updateTime;			//更新时间
	
	
	// 
	private String queryParam;
		public String getQueryParam() {
		return queryParam;
	}
	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}
	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public Integer getDeviceType() {	    return this.deviceType;	}	public void setDeviceType(Integer deviceType) {	    this.deviceType=deviceType;	}	public String getVersion() {	    return this.version;	}	public void setVersion(String version) {	    this.version=version;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public String getContent() {	    return this.content;	}	public void setContent(String content) {	    this.content=content;	}	public String getUrl() {	    return this.url;	}	public void setUrl(String url) {	    this.url=url;	}	public String getPublisher() {	    return this.publisher;	}	public void setPublisher(String publisher) {	    this.publisher=publisher;	}	public String getFileName() {	    return this.fileName;	}	public void setFileName(String fileName) {	    this.fileName=fileName;	}	public Integer getNewData() {	    return this.newData;	}	public void setNewData(Integer newData) {	    this.newData=newData;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}
	
}
