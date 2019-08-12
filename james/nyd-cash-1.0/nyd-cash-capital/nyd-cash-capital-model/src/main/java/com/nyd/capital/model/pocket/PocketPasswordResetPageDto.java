package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PocketPasswordResetPageDto {
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 跳转页面
     */
    private String retUrl;
    /**
     * 是否需要url:1或者0 地址只能使用一次，并且3分钟内有效
     */
    private String isUrl;
}
