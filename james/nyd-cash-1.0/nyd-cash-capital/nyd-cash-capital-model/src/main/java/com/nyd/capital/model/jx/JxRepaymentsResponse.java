package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
public class JxRepaymentsResponse extends JxCommonResponse implements Serializable {
    private String url;
}
