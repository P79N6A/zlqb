package com.nyd.application.model.request;

import lombok.Data;
import lombok.ToString;

/**
 * Created by hwei on 2017/12/18.
 */
@Data
@ToString
public class AgreeMentQueryModel {
    private String userId;
    private String orderId;
}
