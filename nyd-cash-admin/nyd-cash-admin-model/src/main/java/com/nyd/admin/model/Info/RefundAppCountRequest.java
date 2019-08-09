package com.nyd.admin.model.Info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefundAppCountRequest implements Serializable {
    
    private Date startDate;
    
    private String appCode;
    
    private Date endDate;
    
    //起始页
    private Integer pageNum;

    //每页和数
    private Integer pageSize;

}
