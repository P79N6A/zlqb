package com.nyd.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author jiaxy
 * @date 20180609
 */
@Data
public class CollectionCompanyInfoVo implements Serializable {

    //主键id
    @Id
    private Long id;
    //机构名称
    private String companyName;
    //机构类型
    private String companyType;
    //联系人
    private String contactPerson;
    //联系电话
    private String contactPhone;
    //电子邮箱
    private String contactEmail;
    //联系地址
    private String contactAddr;
    //删除标记
    private Integer deleteFlag;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //最后修改人
    private String updateBy;
}
