package com.nyd.admin.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiaxy
 * @date 20180612
 */
@Data
@Table(name = "t_collection_group_info")
public class CollectionGroupInfo implements Serializable {

    //分组id
    @Id
    private Long id;
    //分组名称
    private String groupName;
    //所属机构id
    private Long companyId;
    //删除标记
    private Integer deleteFlag;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;

}
