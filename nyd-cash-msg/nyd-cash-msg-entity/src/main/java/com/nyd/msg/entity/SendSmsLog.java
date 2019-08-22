package com.nyd.msg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "send_sms_log")
/**
 * 马甲包名称配置表
 */
public class SendSmsLog implements Serializable {

    @Id
    private Long id;

    /**
     * 创建时间
     */
    private  Date createTime;

    /**
     * 请求参数
     */
    private  String request;

    /**
     * 返回参数
     */
    private String response;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 渠道
     */
    private int channel;
}


