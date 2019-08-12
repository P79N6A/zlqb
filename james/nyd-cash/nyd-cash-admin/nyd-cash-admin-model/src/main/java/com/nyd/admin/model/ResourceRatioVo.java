package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Peng
 * @create 2017-12-27 14:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceRatioVo {

    //注册总数
    private Integer sumRegister;
    //渠道
    private String source;
}
