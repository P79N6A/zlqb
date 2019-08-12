package com.nyd.order.model.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestData<T> implements Serializable {
    private static final long serialVersionUID = -720807478055084231L;
    
    private String requestId;
    private String requestTime;
    private String requestAppId;
    private T data;

    public RequestData(){}

    public RequestData(String requestId,String requestAppId,T data){
            this.requestId=requestId;
            this.requestAppId=requestAppId;
            this.data=data;
    }

    public static <T> RequestData<T> newRequest(T data){
    	String requestId = UUID.randomUUID().toString();
    	RequestData<T> requestData = new RequestData<T>();
    	requestData.setRequestId(requestId);
        requestData.setData(data);
        return requestData;
    }

    
}
