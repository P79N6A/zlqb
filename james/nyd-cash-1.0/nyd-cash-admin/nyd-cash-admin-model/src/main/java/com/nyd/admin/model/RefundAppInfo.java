package com.nyd.admin.model;

import java.io.Serializable;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundAppInfo implements Serializable{
    //app code
    private String appCode;
    //app名称
    private String refundAppName;
    //app url
    private String appUrl;
    //logo
    private String appLogo;
    //logo url
    private String appLogoUrl;
    //logo
    private String appLogoImge;
    //排序
    private Integer appSort;
    //推荐次数
    private Integer recomNum;
    //实际推荐次数
    private Integer realRecomNum;
    //是否更新实际推荐次数0不更新，1更新
    private Integer realRecomNumFlag;
    //状态0启用，1停用
    private Integer appStatus;
    //最后修改人
    private String updateBy;
    
    //页码
    private Integer pageNum;
    //page size
    private Integer pageSize;
    
    private String reason;
    
    private List<String> imgeList;
    
    //推荐app个数
    private Integer count;
    //描述
    private String message;
    
    private String appList;

}
