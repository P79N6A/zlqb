package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_channel")
public class Channel {

    @Id
    private Long id;

    //渠道编号
    private String channelNo;

    //渠道名称
    private String channelName;

    //引流页h5地址
    private String url;

    //操作系统
    private String os;

    //价格
    private BigDecimal price;

    //计费方式
    private String chargeType;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

}
