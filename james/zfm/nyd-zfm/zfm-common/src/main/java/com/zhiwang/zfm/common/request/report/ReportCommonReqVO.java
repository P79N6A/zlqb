package com.zhiwang.zfm.common.request.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;
import com.zhiwang.zfm.common.util.DateUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("报表通用分页查询请求")
public class ReportCommonReqVO extends PageCommon implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "开始查询时间(yyyy-MM-dd)")
	// @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBegin;
	@ApiModelProperty(value = "结束查询时间(yyyy-MM-dd)")
	// @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEnd;
	@ApiModelProperty(value = "平台编码集合")
	private List<String> platCodeList;

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) throws Exception {
		if (null != dateBegin)
			this.dateBegin = DateUtils.formatDate(DateUtils.format(dateBegin, DateUtils.STYLE_2) + " 00:00:00",
					DateUtils.DATE_TIME_PATTERN);
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) throws Exception {
		if (null != dateEnd)
			this.dateEnd = DateUtils.formatDate(DateUtils.format(dateEnd, DateUtils.STYLE_2) + " 23:59:59",
					DateUtils.DATE_TIME_PATTERN);
	}

	public List<String> getPlatCodeList() {
		return platCodeList;
	}

	public void setPlatCodeList(List<String> platCodeList) {
		this.platCodeList = platCodeList;
	}

}
