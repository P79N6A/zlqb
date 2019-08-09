package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.KzjrProductConfigVo;
import com.nyd.admin.model.KzjrQueryVo;

/**
 * Created by Dengw on 2017/12/15
 */
public interface KzjrService {
    boolean saveKzjr(KzjrProductConfigVo vo);

    boolean updateKzjr(KzjrProductConfigVo vo);

    PageInfo<KzjrProductConfigVo> findPage(KzjrQueryVo vo) throws Exception;
}
