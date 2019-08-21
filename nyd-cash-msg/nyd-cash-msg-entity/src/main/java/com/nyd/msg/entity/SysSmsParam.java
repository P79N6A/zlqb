package com.nyd.msg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="sys_sms_param")
public class SysSmsParam {
    @Id
    private Long id;
    private Integer smsType;
    private String smsTemplate;
    private Integer status;

    private String desc;
    private Integer codeFlag;
    private Integer count;
    private Integer during;
    private String tianRuiYunCode;
}
