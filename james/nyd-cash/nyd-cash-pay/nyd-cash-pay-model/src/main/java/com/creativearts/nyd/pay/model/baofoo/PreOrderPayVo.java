package com.creativearts.nyd.pay.model.baofoo;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@Data
public class PreOrderPayVo implements Serializable{
    @RequireField
    private String userId;

    @RequireField
    private String amount;//借款金额 单位元  到宝付的是分

    @RequireField
    private String billNo;

    @RequireField
    private String accNo;

    @RequireField
    private String cardName;//持卡人姓名

    @RequireField
    private String idCard;

    @RequireField
    private String mobile;

    


}
