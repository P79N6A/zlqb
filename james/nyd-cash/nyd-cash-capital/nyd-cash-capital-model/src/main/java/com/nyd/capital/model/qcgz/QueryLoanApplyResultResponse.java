package com.nyd.capital.model.qcgz;

import lombok.Data;

@Data
public class QueryLoanApplyResultResponse {

    //状态     0:请求成功; 非0为请求异常
    private int status;

    //返回内容
    private Datas data;

    //错误提示(在status为非0时才有值,为具体失败原因)
    private String msg;

    @Data
    public static class Datas {

        //放款处理状态  1:处理中 2:放款成功 3:放款失败
        private int loanStatus;

        //资产编号
        private String assetId;

        //放款时间
        private String loanTime;
    }



}
