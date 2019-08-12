package com.nyd.admin.service;

import com.nyd.admin.model.dto.DownloadAgreementLogDto;
import com.tasfe.framework.support.model.ResponseData;

public interface DownloadAgreementLogService {

    /**
     * 保存代扣协议下载信息
     * @param downloadAgreementLogDto
     * @return
     */
    ResponseData saveDownloadAgreementLog(DownloadAgreementLogDto downloadAgreementLogDto);
}
