package com.nyd.capital.model.pocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PocketHtmlMessage {
    private String userId;
    private String orderNo;
}
