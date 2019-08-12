package com.nyd.user.service;


import com.nyd.user.model.dto.InnerTestDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/11/26.
 */
public interface InnerTestUserService {
    ResponseData removeInnerTestUser(InnerTestDto innerTestDto);

    ResponseData unBindTestUser(InnerTestDto innerTestDto);
}
