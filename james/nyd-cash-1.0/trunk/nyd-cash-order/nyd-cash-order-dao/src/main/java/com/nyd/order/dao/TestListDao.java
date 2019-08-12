package com.nyd.order.dao;

import com.nyd.order.entity.InnerTest;
import com.nyd.order.model.InnerTestInfo;

import java.util.List;

/**
 * Created by Dengw on 2018/1/15
 */
public interface TestListDao {
    List<InnerTest> getObjectsByMobile(String mobile) throws Exception;

    void update(InnerTestInfo innerTestInfo) throws Exception;

    void save(InnerTestInfo innerTestInfo) throws Exception;
}
