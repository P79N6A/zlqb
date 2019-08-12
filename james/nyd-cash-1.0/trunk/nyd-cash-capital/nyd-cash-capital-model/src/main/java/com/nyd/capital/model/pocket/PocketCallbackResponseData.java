package com.nyd.capital.model.pocket;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;

/**
 * 口袋理财回调返回参数
 * @author zhangch
 *
 * @param <T>
 */
@Getter
public class PocketCallbackResponseData<T> implements Serializable {
    private int code;
    private T resp;
    
    public PocketCallbackResponseData(){}
    
    public void success(){
        this.code=200;
    }
    
    public void sendParam(T resp){
        this.code=200;
        this.resp=resp;
    }

	public PocketCallbackResponseData(int code, T resp) {
		super();
		this.code = code;
		this.resp = resp;
	}

	
}
