package com.nyd.msg.service.channel.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模板类短信配置模型
 * @author san
 * @version V1.0
 */
@Data
@Accessors(chain = true)
public class SmsConfigDto {
    /** 短信url 我这个注释可信 2019 - 8 - 21 */
    private String smsPlatUrlSingle;
    /** 开发key */
    private String smsPlatAccount;
    /** 开发秘钥 */
    private String smsPlatPwd;
    /** 短信模板id */
    private String templateId;
    /** 手机号 */
    private String mobile;
    /** 内容 */
    private String content;
    /** 签 */
    private String sign;
}
