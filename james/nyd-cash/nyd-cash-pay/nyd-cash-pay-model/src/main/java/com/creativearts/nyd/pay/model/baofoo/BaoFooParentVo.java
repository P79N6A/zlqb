package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooParentVo {
    /**
     * 版本号
     */
    private String version;
    /**
     * 字符集
     */
    private String input_charset;
    /**
     * 终端号
     */
    private String terminal_id;
    /**
     * 商户号
     */
    private String member_id;
    /**
     * 加密数据类型
     */
    private String data_type;
    /**
     * 加密数据
     */
    private String data_content;
}
