package com.nyd.capital.service.kzjr;

import com.nyd.capital.model.kzjr.OpenPageInfo;
import com.nyd.capital.model.kzjr.SubmitSmsInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Cong Yuxiang
 * 2018/5/9
 **/
public interface KzjrOpenPageService {

    ResponseData getSmsCode(OpenPageInfo openPageInfo);

    ResponseData submitSmsCode(SubmitSmsInfo submitSmsInfo);

}
