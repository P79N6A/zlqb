package com.creativearts.nyd.pay.model.baofoo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class BaoFooParentResult {
    /**
     * 应答码
     */
    private String ret_code;
    /**
     * 应答信息
     */
    private String ret_msg;
    /**
     * 加密数据
     */
    private String data_content;
}
