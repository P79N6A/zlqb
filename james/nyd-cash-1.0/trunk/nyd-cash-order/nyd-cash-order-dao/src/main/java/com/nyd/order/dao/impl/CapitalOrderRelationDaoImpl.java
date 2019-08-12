package com.nyd.order.dao.impl;

import com.nyd.order.dao.CapitalOrderRelationDao;
import com.nyd.order.entity.CapitalOrderRelation;
import com.nyd.order.model.dto.CapitalOrderRelationDto;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CapitalOrderRelationDaoImpl implements CapitalOrderRelationDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(CapitalOrderRelation capitalOrderRelation) throws Exception {
        crudTemplate.save(capitalOrderRelation);
    }

    @Override
    public List<CapitalOrderRelationDto> getCapitalOrderRelationByAssetId(String assetId) throws Exception{
        CapitalOrderRelationDto dto = new CapitalOrderRelationDto();
        Criteria criteria = Criteria.from(CapitalOrderRelation.class)
                .where()
                .and("asset_id", Operator.EQ,assetId)
                .endWhere()
                .orderBy("create_time desc");
        List<CapitalOrderRelationDto> list =crudTemplate.find(dto,criteria);
        return list;
    }
}
