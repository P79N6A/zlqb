package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_target")
public class UserTarget implements Serializable {

    private Integer id;
    //手机号码
    private String mobile;
    //MD5手机号
    private String md5Mobile;
    //sha手机号
    private String shaMobile;
    //
    private Integer ifRegister;
    //活跃度情况1
    private Integer targetOne;
    //活跃度情况2
    private Integer targetTwo;
    //活跃度情况3
    private Integer targetThree;
    //活跃度情况4
    private Integer targetFour;
    //活跃度情况5
    private Integer targetFive;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}