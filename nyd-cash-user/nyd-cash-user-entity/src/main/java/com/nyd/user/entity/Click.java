package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_click")
public class Click {

    @Id
    private Long id;

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

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;


}
