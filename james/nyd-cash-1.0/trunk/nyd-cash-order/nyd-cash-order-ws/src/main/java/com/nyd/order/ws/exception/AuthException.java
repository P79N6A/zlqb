package com.nyd.order.ws.exception;

import lombok.Data;

/**
 * Created by Dengw on 2017/12/8.
 */
@Data
public class AuthException extends Exception{
    private String errorCode;
    private String errorMessage;
    public AuthException(){
        super();
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public AuthException(String errorCode,String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
