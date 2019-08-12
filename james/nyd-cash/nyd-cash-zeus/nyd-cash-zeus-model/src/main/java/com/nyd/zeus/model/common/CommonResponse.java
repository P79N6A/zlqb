package com.nyd.zeus.model.common;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 不带分页的返回结果模型
 * @author fxw
 *
 */
public class CommonResponse<T> implements Serializable {

	private static final long serialVersionUID = 7223184927894637746L;
	
	// 是否成功
    @ApiModelProperty(value="是否成功[true:成功，false:失败]", required=true)
    private boolean success;
    // 返回消息
    @ApiModelProperty(value="返回消息")
    private String msg;
    // 返回编码
    @ApiModelProperty(value="返回编码",required=true)
    private String code;
    // 返回内容
    @ApiModelProperty(value="返回内容")
    private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CommonResponse [success=" + success + ", msg=" + msg + ", code=" + code + ", data=" + data + "]";
	}

	public static CommonResponse<?> error(String msg) {
		CommonResponse<?> result=new CommonResponse<>();
		result.setSuccess(false);
		result.setMsg(msg);
		result.setCode("500");
		return result;
	}
	
	public static CommonResponse<?> error(String msg,String code) {
		CommonResponse<?> result=new CommonResponse<>();
		result.setSuccess(false);
		result.setMsg(msg);
		result.setCode(code);
		return result;
	}

	public static CommonResponse<?> success(String msg) {
		CommonResponse<?> result=new CommonResponse<>();
		result.setSuccess(true);
		result.setMsg(msg);
		return result;
	}
}
