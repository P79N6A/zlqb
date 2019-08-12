package com.nyd.user.entity;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description: 撞库用户手机号
 * @Date: 16:25 2018/9/5
 */
@Data
@Table(name = "t_hit_account")
public class HitAccount {
    private Long id;
    //手机号
    private String accountNumber;
    //sha256加密后的字符串
    private String secretShaStr;
    //md5加密后的字符串
    private String secretMdStr;
    //其他方式
    private String secretOtherStr;
    //备注
    private String description;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
