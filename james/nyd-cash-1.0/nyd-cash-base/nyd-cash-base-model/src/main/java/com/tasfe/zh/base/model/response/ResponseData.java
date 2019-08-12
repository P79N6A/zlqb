package com.tasfe.zh.base.model.response;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created by Lait on 2017/7/27.
 */
@Getter
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = -720807478055084231L;
    private String status;
    private String error;
    private String msg;
    private String code;
    private T data;

    public static ResponseData<?> success() {
        ResponseData responseData = new ResponseData();
        responseData.setStatus("0");
        responseData.setMsg("success");
        responseData.setData(null);
        return responseData;
    }
    public static ResponseData<?> success(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setStatus("0");
        responseData.setMsg(msg);
        return responseData;
    }

    public static ResponseData<?> success(Object data) {
        ResponseData responseData = new ResponseData();
        responseData.setStatus("0");
        responseData.setMsg("success");
        responseData.setData(data);
        return responseData;
    }

    public static ResponseData<?> error() {
        ResponseData responseData = new ResponseData();
        responseData.setStatus("1");
        responseData.setMsg("");
        responseData.setData(null);
        return responseData;
    }
    public static ResponseData<?> error(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.setStatus("1");
        responseData.setMsg(msg);
        return responseData;
    }

    public ResponseData<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public ResponseData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public ResponseData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public ResponseData<T> setError(String error) {
        this.error = error;
        return this;
    }

    public ResponseData setCode(String code) {
        this.code = code;
        return this;
    }
}
