package com.nyd.admin.model.dto;

import com.nyd.admin.model.paging.Paging;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jiaxy
 * @date 20180612
 */
@Data
public class CollectionGroupInfoDto extends Paging implements Serializable {
    //分组id
    @Id
    private Long id;
    //分组名称
    private String groupName;
    //所属机构id
    private Long companyId;
    //所属机构名称
    private String companyName;
    //删除标记
    private Integer deleteFlag;

    //机构集合
    private List<Long> companyIdList;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //最后修改人
    private String updateBy;
}
