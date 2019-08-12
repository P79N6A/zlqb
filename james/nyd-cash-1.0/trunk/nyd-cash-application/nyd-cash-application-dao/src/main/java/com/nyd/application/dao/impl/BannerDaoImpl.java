package com.nyd.application.dao.impl;

import com.nyd.application.dao.BannerDao;
import com.nyd.application.entity.Banner;
import com.nyd.application.model.request.BannerModel;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/11/28
 */
@Repository
public class BannerDaoImpl implements BannerDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<BannerModel> getBannersForApp(String appName) throws Exception {
        BannerModel banner = new BannerModel();
        Criteria criteria = Criteria.from(Banner.class)
                .where()
                .and("delete_flag", Operator.EQ,0)
                .and("app_name",Operator.EQ,appName)
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(banner,criteria);
    }

    @Override
    public List<BannerModel> getBanners() throws Exception {
        BannerModel banner = new BannerModel();
        Criteria criteria = Criteria.from(Banner.class)
                .where()
                .and("delete_flag", Operator.EQ,0)
                .and("app_name",Operator.EQ,"xxd")
                .endWhere()
                .orderBy("create_time desc");
        return crudTemplate.find(banner,criteria);
    }
}
