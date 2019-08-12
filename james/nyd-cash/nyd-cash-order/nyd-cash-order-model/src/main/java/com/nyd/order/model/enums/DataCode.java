package com.nyd.order.model.enums;

/**
 * Created by Dengw on 2017/11/14
 */
public enum DataCode {
    SAVE_ERROR("100","数据库插入数据失败"),
    UPDATE_ERROR("200","数据库更新数据失败"),
    GET_ERROR("300","数据库查询数据失败或无匹配数据");

    private String code;

    private String msg;

    private DataCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return this.code;
    }

    public Integer getCodeInt(){
        return Integer.valueOf(this.code);
    }

    public String getMsg(){
        return this.msg;
    }
}
