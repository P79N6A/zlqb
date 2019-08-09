package com.tasfe.zh.base.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Options {
    private Object lable;
    private Object value;

    public Options(Object lable, Object value) {
        this.lable = lable;
        this.value = value;
    }
}
