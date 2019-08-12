package com.nyd.application.service.exception;

import com.nyd.application.service.consts.ApplicationConsts;

/**
 * @author zhujunjie
 * @version 1.0
 * @date 2017年11月8日
 */
public class RpcResponseException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 3658216696054712345L;

    /**
     * Error message.
     */
    public String message;
    public String code;

    public RpcResponseException() {
    }

    public RpcResponseException(String message) {
        super(message);
        this.code = ApplicationConsts.ERROR_CODE.RPC_ERROR;
    }

    public RpcResponseException(String message, Throwable e) {
        super(ApplicationConsts.RET_MSG.FAIL + ":" + message, e);
    }

    public RpcResponseException(Throwable e) {
        super(e);
    }
}
