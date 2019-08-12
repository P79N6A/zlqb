package com.nyd.user.model.vo.sms;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "短信查询出参", description = "短信查询出参")
public class SmsRecordingVo implements Serializable {
	
	private static final long serialVersionUID = 8424976147652818772L;

	//联系人
	@ApiModelProperty(value = "联系人姓名")
	private String name;

	//联系电话
	@ApiModelProperty(value = "联系电话")
    private String callNo;
    
    //内容
	@ApiModelProperty(value = "内容")
    private String content;
    
    //创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "创建时间")
    private String createtime;
    
    //短信发送时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "短信发送时间")
    private String time;
    
    //短信类型 1 接收 2 发送
	@ApiModelProperty(value = "短信类型 1 接收 2 发送")
    private String type;
    
    //关键词
	@ApiModelProperty(value = "关键词")
    private List<String> keywordList;
    
    //用户id
	@ApiModelProperty(value = "用户id")
    private String userId;
    
    //页
    private int page;
    
    //
    private int size;

}
