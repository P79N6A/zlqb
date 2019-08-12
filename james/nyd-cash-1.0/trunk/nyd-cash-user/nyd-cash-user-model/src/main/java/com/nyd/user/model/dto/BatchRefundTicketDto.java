package com.nyd.user.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 批量退券实体类
 * @author cm
 */

@Data
public class BatchRefundTicketDto implements Serializable {


/*
    //退券金额
    private BigDecimal returnTicketAmount;

    //退券类型  1:小银券   2:其他类型的券
    private Integer returnTicketType;

    //批量退券的手机号(人)
    private List<String> accountNumbers;*/


    //退券金额
    private BigDecimal returnTicketAmount;

    //退券类型  1:小银券   2:其他类型的券
    private Integer returnTicketType;

    //批量退券的手机号(人)
    private List<BatchObject> accountNumbers;


    @Data
    public static class BatchObject implements Serializable{
        private String accountNumber;

        private String premiumId;
    }


}
