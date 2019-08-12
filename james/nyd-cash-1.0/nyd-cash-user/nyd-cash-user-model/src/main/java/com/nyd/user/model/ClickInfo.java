package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by zhujx on 2017/12/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClickInfo {

    //渠道编号
    private String channelNo;

    //点击次数
    private Integer count;

    //页面类型 0 引流页  1 营销页
    private Integer pageFlag;

    //操作系统编号
    private String osNo;

    //APP名称
    private String appName;

    //原始信息
    private String referOrigin;
    //ip
    private String ip;

}
