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
public class RefundAppVo extends BaseInfo implements Serializable{
	
    List<RefundAppInfo> appList;
    //推荐app个数
    private Integer requestStatus;
    
    private String refundNo;
    
    private String reason;
    
}
