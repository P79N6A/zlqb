package com.nyd.user.model.vo;

import java.io.Serializable;
import java.util.List;

import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.RefundAppInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundOrClickVipVo extends BaseInfo implements Serializable{
	
    
    private boolean ifClickVip;
    
    private boolean ifShowRefund;
    
    private boolean ifRefund;
    
}
