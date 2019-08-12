package com.nyd.zeus.model.sms;

import java.io.Serializable;
import java.util.List;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "sendMsgLoanVO", description = "发送贷中短信")
public class SendMsgLoanVO  implements Serializable {
	
	private static final long serialVersionUID = 8424976147652818772L;

	private List<MsgListQueryVO> orderLists;


}
