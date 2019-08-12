/*
 * Created by 成嘉伟 on 2018/3/27 下午9:46.
 * 上海造艺网络科技有限公司
 */

package com.nyd.order.model.enums;

/**
 * 借款来源的枚举
 * Created by cheng_jiawei on 2018/3/27.
 */
public enum BorrowConfirmChannel {
    YMT(1), // 银码头
    NYD(0); // 侬要贷

    private Integer channel;


    private BorrowConfirmChannel(Integer channel) {
        this.channel = channel;
    }

    /**
     * 来源
     * @return
     */
    public Integer getChannel(){
        return this.channel;
    }

}
