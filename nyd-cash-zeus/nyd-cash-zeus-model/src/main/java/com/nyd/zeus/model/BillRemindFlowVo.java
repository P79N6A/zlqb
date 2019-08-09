package com.nyd.zeus.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillRemindFlowVo implements Serializable{

    // 提醒内容
    private String remindMsg;

    // 用户名
    private String remindUserLoginName;

    // 添加时间
    private String createTime;

}
