package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.CollectionCompanyInfoDao;
import com.nyd.admin.entity.CollectionCompanyInfo;
import com.nyd.admin.model.dto.CollectionCompanyInfoDto;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.criteria.Where;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author jiaxy
 * @date 20180611
 */
@Repository
public class CollectionCompanyInfoDaoImpl implements CollectionCompanyInfoDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    /**
     * 新增机构
     * @param dto
     */
    @Override
    public void save(CollectionCompanyInfoDto dto) throws Exception {
        CollectionCompanyInfo collectionCompanyInfo = new CollectionCompanyInfo();
        BeanUtils.copyProperties(dto, collectionCompanyInfo);
        crudTemplate.save(collectionCompanyInfo);
    }

    @Override
    public Page<CollectionCompanyInfoDto> queryCompanyInfo(CollectionCompanyInfoDto dto, int pageNum, int pageSize) throws Exception {
        Criteria criteria = Criteria.from(CollectionCompanyInfo.class).where().camelCase(true).endWhere().limit(Page.resolve(pageNum, pageSize));
        Where where = criteria.getWhere();
        where.camelCase(true);
        if(!StringUtils.isBlank(dto.getCompanyName())){
            where = where.and("companyName",Operator.LIKE, dto.getCompanyName());
        }
        if(!StringUtils.isBlank(dto.getContactPerson())){
            where = where.and("contactPerson",Operator.LIKE, dto.getContactPerson());
        }
        if(!StringUtils.isBlank(dto.getContactPhone())){
            where = where.and("contactPhone",Operator.LIKE, dto.getContactPhone());
        }
        if(!StringUtils.isBlank(dto.getCompanyType())){
            where = where.and("companyType",Operator.LIKE, dto.getCompanyType());
        }

        return crudTemplate.paging(dto,where.endWhere().orderBy("create_time desc"));
    }

    @Override
    public void update(CollectionCompanyInfoDto dto) throws Exception {
        CollectionCompanyInfo collectionCompanyInfo = new CollectionCompanyInfo();
        BeanUtils.copyProperties(dto, collectionCompanyInfo);
        crudTemplate.update(collectionCompanyInfo,Criteria.from(CollectionCompanyInfo.class));
    }


}
