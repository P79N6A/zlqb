package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvertisingModel implements Serializable {
    //广告内容
    private String content;
}
