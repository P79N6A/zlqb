package com.nyd.user.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class HitRuleConfigRequest implements Serializable {
    private String appName;

    private String source;
}
