package com.nyd.application.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
/**
 * 用户机型等设备信息
 * @author denqingfeng
 * @date 2019/8/15 14:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileImagesInfo implements Serializable {
    private String billNo;
    private String orderNo;
    private String url;
    private String type;
}
