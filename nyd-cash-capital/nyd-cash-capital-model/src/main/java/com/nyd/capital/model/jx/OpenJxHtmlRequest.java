package com.nyd.capital.model.jx;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
@ToString
public class OpenJxHtmlRequest implements Serializable {
    private String userId;
    private String mobile;
    private String realName;
    private String idCardNumber;
    private String bankCardNumber;
    private String stage;
}
