package com.nyd.order.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/12/13
 * 借款确认返回对象
 */
@Data
public class BorrowConfirmVo implements Serializable{
    //订单编号
    private String orderNo;
    //是否发送p2p短信，返回给H5;
    private Boolean p2pSendFlag;
    //是否发送p2p短信;0需要发,1开户,2正常
    private Integer flag;
    //p2p账号Id
    private String p2pId;
    //p2p接口状态
    private String p2pStatus;

    private String url;

    private String fundCode;
}
