package com.nyd.application.model.request;

import java.io.Serializable;
import java.util.List;

import com.nyd.application.model.mongo.SmsInfo;

import lombok.Data;

/**
 * @author liuqiu
 */
@Data
public class SmsRequest implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appName;
    private List<SmsInfo> content;
    private String userId;
}
