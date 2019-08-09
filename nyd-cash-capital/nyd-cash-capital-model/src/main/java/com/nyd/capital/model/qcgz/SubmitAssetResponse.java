package com.nyd.capital.model.qcgz;

import lombok.Data;

/**
 * 资产提交响应参数
 * @author cm
 */

@Data
public class SubmitAssetResponse {

    //状态     0:请求成功;  非0为请求异常
    private int status;

    //返回内容
    private Datas data;

    //错误提示(在status为非0时才有值,为具体失败原因)
    private String msg;


    @Data
    public static class Datas {

        //资产编号
        private String assetId;
    }
}
