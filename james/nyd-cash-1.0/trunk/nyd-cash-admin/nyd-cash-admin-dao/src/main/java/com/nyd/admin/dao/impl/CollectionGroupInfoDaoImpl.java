package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.CollectionGroupInfoDao;
import com.nyd.admin.entity.CollectionGroupInfo;
import com.nyd.admin.model.CollectionGroupInfoVo;
import com.nyd.admin.model.dto.CollectionGroupInfoDto;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jiaxy
 * @date 20180611
 */
@Repository
public class CollectionGroupInfoDaoImpl implements CollectionGroupInfoDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    /**
     * 分组保存
     * @param dto
     */
    @Override
    public void save(CollectionGroupInfoDto dto) throws Exception {
        CollectionGroupInfo collectionGroupInfo = new CollectionGroupInfo();
        BeanUtils.copyProperties(dto,collectionGroupInfo);
        crudTemplate.save(collectionGroupInfo);
    }

    /**
     * 根据所属机构id查询分组列表
     * @param dto
     * @return
     */
    @Override
    public List<CollectionGroupInfoDto> queryGroupInfoListByCompanyId(CollectionGroupInfoDto dto) throws Exception {
        return crudTemplate.find(dto,Criteria.from(CollectionGroupInfo.class).whioutId().orderBy("create_time desc"));
    }

    /**
     * 根据条件查询所有分组
     *
     * @param dto
     * @return
     */
    @Override
    public Page<CollectionGroupInfoDto> queryGroupInfoList(CollectionGroupInfoDto dto, int pageNum, int pageSize) throws Exception {
        Criteria criteria = Criteria.from(CollectionGroupInfo.class).limit(Page.resolve(pageNum, pageSize));
        if (!StringUtils.isBlank(dto.getGroupName())) {
            criteria = criteria.where().camelCase(true)
                    .and("groupName", Operator.LIKE, dto.getGroupName())
                    .endWhere();
        }

        return crudTemplate.paging(dto, criteria.orderBy("create_time desc"));
    }

    /**
     * 修改分组信息
     * @param dto
     */
    @Override
    public void updateGroupInfo(CollectionGroupInfoDto dto) throws Exception {
        CollectionGroupInfo collectionGroupInfo = new CollectionGroupInfo();
        BeanUtils.copyProperties(dto, collectionGroupInfo);
        crudTemplate.update(collectionGroupInfo,Criteria.from(CollectionGroupInfo.class));
    }

    /**
     * 根据机构id修改分组信息
     * @param vo
     */
    @Override
    public void updateGroupInfoByCompanyId(CollectionGroupInfoVo vo) throws Exception {
        CollectionGroupInfo collectionGroupInfo = new CollectionGroupInfo();
        BeanUtils.copyProperties(vo, collectionGroupInfo);
        Criteria criteria = Criteria.from(CollectionGroupInfo.class)
                                    .whioutId()
                                    .where().camelCase(true)
                                    .and("companyId",Operator.EQ,vo.getCompanyId())
                                    .endWhere();
        crudTemplate.update(collectionGroupInfo,criteria);
    }
}
