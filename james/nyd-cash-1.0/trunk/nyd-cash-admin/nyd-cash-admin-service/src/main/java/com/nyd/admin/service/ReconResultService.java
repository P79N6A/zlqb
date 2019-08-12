package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.ReconQueryVo;
import com.nyd.admin.model.dto.ReconResultDto;

import java.text.ParseException;

/**
 * Cong Yuxiang
 * 2017/12/6
 **/
public interface ReconResultService {
    PageInfo<ReconResultDto> findPage(ReconQueryVo vo) throws ParseException;
}
