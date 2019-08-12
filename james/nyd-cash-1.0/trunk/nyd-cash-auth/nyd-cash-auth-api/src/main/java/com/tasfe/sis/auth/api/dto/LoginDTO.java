package com.tasfe.sis.auth.api.dto;

import com.tasfe.zh.base.model.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 客户登录DTO.<br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO extends BaseDto {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 客户ID/客户号 对应Table-customer code
     */
    private String customerCode;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String loginPwd;
}
