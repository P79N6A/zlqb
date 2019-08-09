package com.nyd.msg.exception;



import java.util.ArrayList;
import java.util.List;

/**
 * 校验异常
 */
public class ValidateException extends Exception {

    private ErrorInfo errorInfo = new ErrorInfo();

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    @Override
    public String toString() {
        return "{\"errorCode\":"+errorInfo.getErrorCode()+",\"errorMsg\":"+errorInfo.getErrorMsg()+"}";
    }
}
