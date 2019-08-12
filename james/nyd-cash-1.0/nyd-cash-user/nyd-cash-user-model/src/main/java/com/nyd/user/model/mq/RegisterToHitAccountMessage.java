package com.nyd.user.model.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 19:27 2018/9/6
 */
@Data
public class RegisterToHitAccountMessage implements Serializable {
    /**
     * 手机号
     */
    private String accountNumber;

    private String token;
}
