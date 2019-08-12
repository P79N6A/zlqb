package com.nyd.member.model;

public class WZResponse {
    private String msg;
    private Integer status;
    private Data data;
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private String totalBalance;
        public String getTotalBalance() {
            return totalBalance;
        }
        public void setTotalBalance(String totalBalance) {
            this.totalBalance = totalBalance;
        }

    }
}
