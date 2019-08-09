package com.nyd.order.api;

import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/11/26.
 */
public interface TestStatusContract {
    ResponseData<Boolean> judgeTestUserFlag(String mobile);

    ResponseData removeInnerTest(String mobile);

    ResponseData<Boolean> saveInnerTest(String mobile);

}
