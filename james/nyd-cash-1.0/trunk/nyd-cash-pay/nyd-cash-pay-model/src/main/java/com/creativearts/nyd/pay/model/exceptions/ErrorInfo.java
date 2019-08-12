package com.creativearts.nyd.pay.model.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

    @Setter
    @Getter
    private String errorCode;

    @Setter
    @Getter
    private String errorMsg;

    @Setter
    @Getter
    private String field;

    @Setter
    @Getter
    private Object invalidValue;

    public ErrorInfo() {
    }

    public ErrorInfo(String errorCode, String errorMsg, String field, Object invalidValue) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.field = field;
        this.invalidValue = invalidValue;
    }
    public ErrorInfo(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}