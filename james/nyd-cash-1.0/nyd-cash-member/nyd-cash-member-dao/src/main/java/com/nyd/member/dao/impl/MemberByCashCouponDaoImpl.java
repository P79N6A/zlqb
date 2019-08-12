package com.nyd.member.dao.impl;

import com.nyd.member.dao.MemberByCashCouponDao;
import com.nyd.member.entity.MemberByCashCoupon;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class MemberByCashCouponDaoImpl implements MemberByCashCouponDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(MemberByCashCoupon memberByCashCoupon) throws Exception {
        crudTemplate.save(memberByCashCoupon);
    }
}
