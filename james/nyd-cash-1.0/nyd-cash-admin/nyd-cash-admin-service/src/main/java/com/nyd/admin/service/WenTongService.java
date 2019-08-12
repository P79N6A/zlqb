package com.nyd.admin.service;

import com.nyd.admin.model.WenTongExcelVo;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface WenTongService {

    List<WenTongExcelVo> queryWenTongExcelVo(String startDate, String endDate,String name,String mobile);

    ResponseData importExcel(CommonsMultipartFile file);
}
