package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.FundDetailDao;
import com.nyd.admin.entity.FundDetail;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2018/1/2.
 */
@Repository
public class FundDetailDaoImpl implements FundDetailDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveFundDetails(List<FundDetail> fundDetails) throws Exception {
        if (fundDetails!=null&&fundDetails.size()>0) {
            for (FundDetail fundDetail : fundDetails) {
                crudTemplate.save(fundDetail);
            }
        }
    }
}
