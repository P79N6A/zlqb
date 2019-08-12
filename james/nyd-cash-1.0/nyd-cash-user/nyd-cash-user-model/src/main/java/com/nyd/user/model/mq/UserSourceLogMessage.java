package com.nyd.user.model.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * mq消息传输
 * @author shaoqing.liu
 * @date 2018/7/3 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSourceLogMessage implements Serializable {

    /**账号**/
    private String accountNumber;

    /**appName**/
    private String appName;

    /**用户来源**/
    private String source;

    /**添加时间**/
    private Date createTime;
    
}
