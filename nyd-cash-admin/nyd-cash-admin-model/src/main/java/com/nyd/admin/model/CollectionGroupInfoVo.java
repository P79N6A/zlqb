package com.nyd.admin.model;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author jiaxy
 * @date 20180612
 */
@Data
public class CollectionGroupInfoVo implements Serializable {

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
    private String createTime;
    //修改时间
    private String updateTime;
    //最后修改人
    private String updateBy;

    //所属机构名称
    private String companyName;
}
