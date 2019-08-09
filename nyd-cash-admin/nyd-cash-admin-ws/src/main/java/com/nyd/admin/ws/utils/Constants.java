package com.nyd.admin.ws.utils;

import com.nyd.admin.service.utils.OSUtil;

import java.io.File;

/**
 * Cong Yuxiang
 * 2017/11/23
 **/
public class Constants {
    public static final String BILL_WSM_PREFIX="bill_wsm_";
    public static final String CHARGE_BACK_WSM_PREFIX = "chargeback_wsm_";

//    //账单 上传路径
//    public static final String uploadPath = "E:" + File.separator + "upload";
//    static
    public static String getUploadPath(){
        if(OSUtil.isLinux()){
            return "/data"+ File.separator+"upload";
        }else if(OSUtil.isWindows()){
            return "E:" + File.separator + "upload";
        }else {
            return null;
        }
    }

}
