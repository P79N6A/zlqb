package com.zhiwang.zfm.common.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * APP公用返回参数 
 * @author psb
 *
 */
public class AppCommonResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5411715740623837830L;
	// 是否成功
    @ApiModelProperty(value="是否成功[true:成功，false:失败]", required=true)
    private boolean success = false;
    // 返回消息
    @ApiModelProperty(value="返回消息")
    private String msg;
    // 返回编码
    @ApiModelProperty(required=true)
    private String code;
    // 返回内容
    @ApiModelProperty(value="返回内容")
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
}
