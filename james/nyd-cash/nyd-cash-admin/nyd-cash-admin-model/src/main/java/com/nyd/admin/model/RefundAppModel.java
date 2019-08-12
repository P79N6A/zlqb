package com.nyd.admin.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefundAppModel implements Serializable{
    //app code
    private String appCode;
    //app名称
    private String appName;
    //app url
    private String appUrl;
    //logo
    private String appLogo;
    //logo
    private String appLogoImge;
    
    private String reason;
    
    private List<String> imgeUrlList;

}
