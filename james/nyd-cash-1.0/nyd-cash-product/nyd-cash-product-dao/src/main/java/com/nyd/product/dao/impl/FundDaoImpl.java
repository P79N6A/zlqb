package com.nyd.product.dao.impl;

import com.nyd.product.dao.FundDao;
import com.nyd.product.entity.Fund;
import com.nyd.product.model.FundInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhujx on 2017/11/22.
 */
@ResponseBody
public class FundDaoImpl implements FundDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(FundInfo fundInfo) throws Exception {
        Fund fund = new Fund();
        BeanUtils.copyProperties(fundInfo,fund);
        crudTemplate.save(fund);
    }

    @Override
    public void update(FundInfo fundInfo) throws Exception {
        Criteria criteria = Criteria.from(Fund.class)
                .whioutId()
                .where().and("fund_code", Operator.EQ,fundInfo.getFundCode())
                .endWhere();
        fundInfo.setFundCode(null);
        crudTemplate.update(fundInfo,criteria);
    }

    @Override
    public List<FundInfo> getFundInfoLs() throws Exception {
        FundInfo fundInfo = new FundInfo();
        Criteria criteria = Criteria.from(Fund.class)
                .where().and("is_in_use", Operator.EQ,0)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(fundInfo,criteria);
    }
}
