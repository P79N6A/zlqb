package com.nyd.user.model.request;

import lombok.Data;

@Data
public class NewAttachmentModel {
	private String  _id;
	  //用户ID
    private String userId;
    //手机号码
    private String accountNumber;
    //附件类型
    private String type;
    //附件
    private String file;
    //附件名称
    private String fileName;
}
