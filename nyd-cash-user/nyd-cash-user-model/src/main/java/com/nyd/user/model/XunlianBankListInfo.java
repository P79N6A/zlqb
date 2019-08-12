package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 17/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class XunlianBankListInfo implements Serializable {

	
	private String id;
	
    private String bankName;

    private String bankNo;
    
    private String status;


}
