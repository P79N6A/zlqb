package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 17/11/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_step")
public class Step {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //身份证信息是否填写
    private String identityFlag;
    //工作信息是否填写
    private String jobFlag;
    //联系人信息是否填写
    private String contactFlag;
    //银行卡信息是否填写
    private String bankFlag;
    //信用认证
    private String authFlag;
    //芝麻分是否认证
    private String zmxyFlag;
    //芝麻分权重
    private Integer zmxyWeight;
    //手机是否认证
    private String mobileFlag;
    //手机认证权重
    private Integer mobileWeight;
    //淘宝是否认证
    private String tbFlag;
    //淘宝认证权重
    private Integer tbWeight;
    //网银是否认证
    private String onlineBankFlag;
    //网银认证权重
    private Integer onlineBankWeight;
    //公信宝认证
    private String gxbFlag;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //手机认证时间
    private  Date mobileTime;
    //淘宝认证
    private  Date tbTime;
    //公信宝认证
    private  Date gxbTime;
    //风控是否审核0：否 1 是
    private String preAuditFlag;
    //风控审核结果等级
    private String preAuditLevel;
}
