package com.tasfe.zh.base.dao.test;

//import com.google.common.collect.Maps;

import com.tasfe.zh.base.dao.mybatis.plugins.page.MybatisPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-db.xml"})
@Slf4j
//@Transactional
public class TestTmpUserDao {

    @Autowired
    private TmpUserDao tmpUserDao;


    private List<TmpUser> getUsr(int count) {
        List<TmpUser> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TmpUser tmpUser = new TmpUser();
            tmpUser.setUsername("lp," + UUID.randomUUID().toString());
            tmpUser.setPhoto("my photo path" + i);
            users.add(tmpUser);
        }
        return users;

    }


    //@Transactional(readOnly=false)
    @Test
    public void testSave() {
        TmpUser tmpUser = new TmpUser();
        tmpUser.setUsername("lp," + UUID.randomUUID().toString());
        tmpUser.setPhoto("my photo path");
        tmpUserDao.save(tmpUser);
    }


    @Test
    public void testBatchSave() {
        List<TmpUser> users = getUsr(100);
        tmpUserDao.save(users);
    }


    @Test
    public void queryAll() {

        //Map<String,Object> searchParam = Maps.newHashMap() ;
        Map<String, Object> searchParam = new HashMap<>();
        Page<TmpUser> result = tmpUserDao.findAll(searchParam, new MybatisPageRequest(0, 1));


        System.out.println(result.getTotalPages());
        //log.info("result->{}", JSON.toJSONString(result , true));

    }


    @Test
    public void query() {

        // 每个数据库的容量
        int total = 8000000;
        // 不停变化sv的值

        int sv = 54495;

        for (int i = 1; i < 8000000; i++) {
            sv = i;
            System.out.println("======" + sv);
            // 规划集群中数据库的数量(一主,三从)
            int dbCount = 4;
            // 规划数据表的数据量,算法不受库的限制
            int tbCount = 2;
            // 倍增因子
            long divisor = Math.round(sv / total);
            // 系数
            long coefficient = sv % total;
            // 库
            long ds = coefficient % dbCount + divisor * dbCount;
            // 表
            long tb = coefficient % tbCount;
            System.out.println("db_" + ds + ".tb_" + tb);
        }
    }


    // 分裤分表
    public static void main(String[] args) {


    }

}
