package com.nyd.application.service;

import com.tasfe.framework.support.model.ResponseData;

import java.util.Map;

/**
 * Created by Dengw on 2018/2/25
 */
public interface AssessService {
    ResponseData getAssessInfo(Map<String, String> map);
}
