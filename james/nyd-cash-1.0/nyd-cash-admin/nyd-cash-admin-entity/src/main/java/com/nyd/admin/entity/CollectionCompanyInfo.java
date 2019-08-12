package com.nyd.admin.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiaxy
 * @date 20180611
 */
@Data
@Table(name = "t_collection_company_info")
public class CollectionCompanyInfo implements Serializable {
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
    private Date createTime;
    //更新时间
    private Date updateTime;
    //最后修改人
    private String updateBy;
}
