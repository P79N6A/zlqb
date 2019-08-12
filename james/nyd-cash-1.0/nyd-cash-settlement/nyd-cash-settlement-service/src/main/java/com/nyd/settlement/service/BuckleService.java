package com.nyd.settlement.service;

import com.nyd.settlement.model.vo.NydHlbVo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
public interface BuckleService {
    List<String>  queryBanks(String userId);

    ResponseData withHold(NydHlbVo vo) throws Exception;
}
