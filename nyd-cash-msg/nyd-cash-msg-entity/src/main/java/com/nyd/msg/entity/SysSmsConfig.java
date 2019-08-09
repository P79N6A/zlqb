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
@Table(name="sys_sms_config")
public class SysSmsConfig {
    @Id
    private Long id;
    private String smsPlatUrlSingle;
    private String smsPlatUrlBatch;
    private String smsPlatAccount;
    private String smsPlatPwd;
    private Integer status;
    private String channelFlag;
}
