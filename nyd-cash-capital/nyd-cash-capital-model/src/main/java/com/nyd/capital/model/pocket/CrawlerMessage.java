package com.nyd.capital.model.pocket;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
@ToString
public class CrawlerMessage implements Serializable {

    private String url;
    private String orderNo;
}
