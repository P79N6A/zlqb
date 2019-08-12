package com.nyd.capital.model.kzjr;

import lombok.Data;
import lombok.ToString;

/**
 * Cong Yuxiang
 * 2018/5/9
 **/
@Data
@ToString
public class OpenPageInfo {
    private String userId;
    private String bankCardNo;
    private String url;
    private Integer channel;
    private String accountNumber;
}
