package com.nyd.admin.model.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class JiGuangParamDto implements Serializable{
	
	//起始时间
    private String startTime;

    //结束时间
    private String endTime;

    //app名称
    private String appName;

    //标记
    private int flag;

    //起始页
    private Integer pageNum;

    //每页查询数
    private Integer pageSize;

}
