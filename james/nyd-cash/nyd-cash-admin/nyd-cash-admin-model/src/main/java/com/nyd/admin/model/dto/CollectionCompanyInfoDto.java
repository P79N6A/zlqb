package com.nyd.admin.model.dto;

import com.nyd.admin.model.paging.Paging;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiaxy
 * @date 20180609
 */
@Data
public class CollectionCompanyInfoDto extends Paging implements Serializable {

    //主键id
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
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;

}
