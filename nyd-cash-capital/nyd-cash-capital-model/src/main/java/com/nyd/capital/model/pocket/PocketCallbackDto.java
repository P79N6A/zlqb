package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PocketCallbackDto implements Serializable{
    //放款状态，0：打款成功；1003：打款失败；
    private Integer code;

    //放款订单号
    private String order_id;

    //放款日期如20161228
    private String opr_dat;

    //侬要贷订单号
    private String result;

    //签名（无需md5）
    private String sign;

}
