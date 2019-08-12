package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 17/11/1.
 * 用户工作信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_job")
public class Job{
    @Id
    private Long id;
    //用户ID
    private String userId;
    //行业
    private String industry;
    //职业
    private String profession;
    //公司名称
    private String company;
    //公司详细地址
    private String companyAddress;
    //省份
    private String companyProvince;
    //城市
    private String companyCity;
    //地区
    private String companyDistrict;
    //职位
    private String position;
    //薪资
    private String salary;
    //公司电话区号
    private String telDistrictNo;
    //公司电话
    private String telephone;
    //分机号
    private String telExtNo;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
