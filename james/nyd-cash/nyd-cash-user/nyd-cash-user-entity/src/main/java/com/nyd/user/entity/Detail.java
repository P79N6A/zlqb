package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/11/1.
 * 用户详细信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_detail")
public class Detail {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //身份证姓名
    private String realName;
    //身份证号码
    private String idNumber;
    //身份证详细地址
    private String idAddress;
    //签发机关
    private String signOrg;
    //身份证生效日期
    private String effectTime;
    //居住详细地址
    private String livingAddress;
    //居住地省code
    private String livingProvince;
    //居住地市code
    private String livingCity;
    //居住地区code
    private String livingDistrict;
    //婚姻状况
    private String maritalStatus;
    //最高学历
    private String highestDegree;
    //人脸识别结果
    private String faceRecognition;
    //人脸识别相似度
    private BigDecimal faceRecognitionSimilarity;

    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    
    private String qq;
    
    private String weChat;
    
    private String mailBox;
}
