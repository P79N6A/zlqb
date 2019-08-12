package com.creativearts.nyd.pay.model.baofoo;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class SelectBankCardResult {
    /**
     * 是否宝付绑卡:0-未绑卡,1-绑卡
     */
    private Integer ifBindCard;
    /**
     * 身份证号码
     */
    private String idNumber;

    private List<QueryBuindCard> cardList;
}
