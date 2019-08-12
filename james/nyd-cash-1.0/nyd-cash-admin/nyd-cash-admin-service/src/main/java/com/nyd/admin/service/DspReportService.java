package com.nyd.admin.service;

import com.nyd.admin.model.DspReportVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/12/18.
 */
public interface DspReportService {
    ResponseData queryDspSucess(DspReportVo vo);
}
