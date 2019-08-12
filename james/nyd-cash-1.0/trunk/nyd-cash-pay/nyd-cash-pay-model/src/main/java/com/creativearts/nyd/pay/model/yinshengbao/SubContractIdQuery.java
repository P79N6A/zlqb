package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;


@Data
public class SubContractIdQuery implements Serializable {
    private String cardNo;
    private String name;
    private String idCardNo;
    private String accountId;
    private String mac;
}
