package com.nyd.capital.service.kzjr;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.kzjr.*;

import java.io.UnsupportedEncodingException;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
public interface KzjrService {
    //发送短信
    JSONObject sendSmsCode(SendSmsRequest request);

    //开户
    JSONObject accountOpen(OpenAccountRequest request) throws UnsupportedEncodingException;

    //开户查询
    JSONObject queryAccount(QueryAccountRequest request);

    //资产提交
    JSONObject assetSubmit(AssetSubmitRequest request);

    //获取渠道产品列表
    JSONObject productList(GetProductRequest request);

    //还款通知
    JSONObject repayNotify(RepayNotifyRequest request);

    //放款查询
    JSONObject queryMatchResult(QueryRemitResult remitResult);

    //放款批量查询
    JSONObject queryMatchResultList(QueryRemitResultBatch remitResultBatch);

    //按天获取渠道列表
    JSONObject queryAssetList(QueryAssetListRequest request);

    //查询单个资产
    JSONObject queryAsset(QueryAssetRequest request);

    //绑定银行卡
    JSONObject bindCard(BindCardRequest request);

    //解绑银行卡
    JSONObject unBindCard(UnBindCardRequest request);

    JSONObject batchAssetSubmit(BatchAssetSubmitRequest request);

    //开户页面
    String openAccountPage(KzjrOpenAcountRequest request);

}
