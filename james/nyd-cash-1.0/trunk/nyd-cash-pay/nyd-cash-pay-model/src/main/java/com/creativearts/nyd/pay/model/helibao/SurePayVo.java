package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class SurePayVo implements Serializable {

   //订单号
   private String orderNo;

   //短信验证码
   private String messageCode;
}
