package com.nyd.capital.model.kzjr;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2018/5/9
 **/
@Data
public class SubmitSmsInfo {
    private String userId;
    private String smsCode;
    private Integer channel;
    private String accountNumber;
}
