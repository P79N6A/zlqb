package com.nyd;

import com.nyd.msg.dao.ISysSmsConfigDao;
import com.nyd.msg.dao.ISysSmsParamDao;
import com.nyd.msg.entity.SysSmsConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/nyd/msg/configs/dao/xml/spring-mybatis.xml"})
public class Test {
//    @Resource(name="mysql")
//    CrudTemplate crudTemplate;
    @Autowired
    private ISysSmsParamDao sysSmsParamDao;
    @Autowired
    private ISysSmsConfigDao sysSmsConfigDao;
    @org.junit.Test
    @Transactional
    @Rollback(false)
    public void testInsert() throws Exception {
//        System.out.println(sysSmsParamDao.selectBySourceType(1));
//        crudTemplate.save(get());
        System.out.println(sysSmsConfigDao.queryList().size());

//        crudTemplate.save(getUsers(10));


    }
    private SysSmsConfig get(){
        SysSmsConfig u = new SysSmsConfig();
        u.setSmsPlatAccount("account");
        u.setSmsPlatPwd("pwd");
        u.setSmsPlatUrlSingle("http://www.baidu.com");
        return u;
    }

}
