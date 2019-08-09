package com.nyd.application.service;

import com.nyd.application.model.request.ExTSignAutoModel;
import com.nyd.application.model.request.FadadaNotifyModel;
import com.nyd.application.model.request.GenerateContractModel;
import com.nyd.application.model.request.MuBanRequestModel;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/11/17.
 */
public interface FadadaService {
    //合同模板传输接口  将pdf文档上传到法大大
    ResponseData uploadPdfTemplate(MuBanRequestModel muBanRequestModel);

    //合同生成接口
    ResponseData generateContract(GenerateContractModel generateContractModel);

    //文档签署接口
    ResponseData extSignAuto(ExTSignAutoModel exTSignAutoModel);

    //法大大回调接口
    void fadadaNotify(FadadaNotifyModel fadadaNotifyModel);


    String downLoadAndUploanQiniu(String downLoadUrl);

    //合同查看接口
    ResponseData viewContract(String contractId);
    
	/*
	 * //信用批核服务协议接口 ResponseData generateContract(AddressListModel
	 * addressListModel);
	 */
}
