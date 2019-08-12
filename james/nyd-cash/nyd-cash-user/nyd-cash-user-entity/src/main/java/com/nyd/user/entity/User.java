package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hwei on 2017/7/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_user")
public class User {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //证件类型
    private String certificateType;
    //性别
    private String gender;
    //民族
    private String nation;
    //生日
    private String birth;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
