package com.nyd.user.dao.impl;

import com.nyd.user.dao.ClickDao;
import com.nyd.user.entity.Click;
import com.nyd.user.model.ClickInfo;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zhujx on 2017/12/12.
 */
@Repository
public class ClickDaoImpl implements ClickDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public void save(ClickInfo clickInfo) throws Exception {
        Click click = new Click();
        BeanUtils.copyProperties(clickInfo,click);
        crudTemplate.save(click);
    }
}
