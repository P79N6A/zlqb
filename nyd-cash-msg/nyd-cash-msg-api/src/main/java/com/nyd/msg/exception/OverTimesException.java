package com.nyd.msg.exception;

/**
 * 多次验证次数超了
 */
public class OverTimesException extends Exception{
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
