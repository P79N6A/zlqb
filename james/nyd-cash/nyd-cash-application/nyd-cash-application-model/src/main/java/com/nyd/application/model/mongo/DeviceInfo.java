package com.nyd.application.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户机型等设备信息
 * @author shaoqing.liu
 * @date 2018/7/11 14:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceInfo implements Serializable {

    /**设备类型**/
    private String deviceIdType;
    /**手机号码**/
    private String phoneNo;
}
