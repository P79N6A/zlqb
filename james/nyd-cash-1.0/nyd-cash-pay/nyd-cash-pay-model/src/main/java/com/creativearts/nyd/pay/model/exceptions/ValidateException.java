package com.creativearts.nyd.pay.model.exceptions;

/**
 * Yuxiang Cong
 **/
public class ValidateException extends Exception{
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
