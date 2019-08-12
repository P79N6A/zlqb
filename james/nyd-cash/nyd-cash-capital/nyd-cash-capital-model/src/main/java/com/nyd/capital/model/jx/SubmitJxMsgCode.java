package com.nyd.capital.model.jx;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@ToString
public class SubmitJxMsgCode implements Serializable {
    /**
     * 验证码
     **/
    private String smsCode;
    /**
     * key
     **/
    private String driverUuid;
    /**
     * key
     **/
    private String userId;
}
