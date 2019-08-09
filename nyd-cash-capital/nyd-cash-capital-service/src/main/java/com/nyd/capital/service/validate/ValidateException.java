package com.nyd.capital.service.validate;


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
