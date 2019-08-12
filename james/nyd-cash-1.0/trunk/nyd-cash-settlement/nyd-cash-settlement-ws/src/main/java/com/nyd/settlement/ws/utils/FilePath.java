package com.nyd.settlement.ws.utils;

import java.io.File;

public class FilePath {

    /**
     * 推荐费批量退款路径（仅为保存批量记录）
     * @return
     */
    public static String getUploadPath(){
        if(OSUtil.isLinux()){
            return "/data"+ File.separator+"recommendUpload";
        }else if(OSUtil.isWindows()){
            return "E:" + File.separator + "recommendUpload";
        }else {
            return null;
        }
    }

    /**
     * 支付宝推荐费批量退款路径（线上进行真实扣款）
     */
    public static String zfbUploadPath(){
        if(OSUtil.isLinux()){
            return "/data"+ File.separator+"zfbRefundRecommendUpload";
        }else if(OSUtil.isWindows()){
            return "E:" + File.separator + "zfbRefundRecommendUpload";
        }else {
            return null;
        }
    }

    /**
     * 微信推荐费批量退款路径（线上进行真实扣款）
     */
    public static String wxUploadPath(){
        if(OSUtil.isLinux()){
            return "/data"+ File.separator+"wxRefundRecommendUpload";
        }else if(OSUtil.isWindows()){
            return "E:" + File.separator + "wxRefundRecommendUpload";
        }else {
            return null;
        }
    }


}
