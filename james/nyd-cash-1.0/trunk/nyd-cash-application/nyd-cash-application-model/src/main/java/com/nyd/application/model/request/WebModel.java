package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/12/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WebModel implements Serializable {

    //描述
    private String webDescription;
    //key
    private String webKey;
    //值
    private String webValue;

}
