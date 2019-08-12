package com.tasfe.zh.base.service;

import com.tasfe.zh.base.model.code.Checkcode;

/**
 * 校验码服务（页面码或短信校验码）
 * Created by Lait on 2017/7/31.
 */
public interface CheckcodeService {
    boolean verify(Checkcode checkcode);
}
