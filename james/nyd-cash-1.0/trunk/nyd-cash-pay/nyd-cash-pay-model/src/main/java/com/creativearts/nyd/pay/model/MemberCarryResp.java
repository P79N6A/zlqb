package com.creativearts.nyd.pay.model;

import lombok.Data;

/**
 * Cong Yuxiang
 * 2018/4/23
 **/
@Data
public class MemberCarryResp {
    private String productCode;
    private String mobile;
    private String status;//0为成功 1为失败
    private String memberType;
}
