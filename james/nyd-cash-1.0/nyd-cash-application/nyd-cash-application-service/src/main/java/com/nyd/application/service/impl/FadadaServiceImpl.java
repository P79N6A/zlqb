package com.nyd.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nyd.application.model.request.ExTSignAutoModel;
import com.nyd.application.model.request.FadadaNotifyModel;
import com.nyd.application.model.request.GenerateContractModel;
import com.nyd.application.model.request.MuBanRequestModel;
import com.nyd.application.service.FadadaService;
import com.nyd.application.service.commonEnum.ResultCode;
import com.nyd.application.service.util.AppProperties;
import com.nyd.application.service.util.FadadaApi;
import com.nyd.application.service.util.MongoApi;
import com.nyd.application.service.util.QiniuApi;
import com.nyd.application.service.validation.Verify;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwei on 2017/11/17.
 */
@Service
public class FadadaServiceImpl implements FadadaService{
    private static Logger LOGGER = LoggerFactory.getLogger(FadadaServiceImpl.class);

    @Autowired
    FadadaApi fadadaApi;

    @Autowired
    AppProperties appProperties;

    @Autowired
    QiniuApi qiniuApi;

    @Autowired
    MongoApi mongoApi;

    /**
     * 模板上传
     * @param muBanRequestModel
     * @return
     */
    @Override
    public ResponseData uploadPdfTemplate(MuBanRequestModel muBanRequestModel) {
        ResponseData responseData = ResponseData.success();
        if (!Verify.MuBanRequestModelverify(muBanRequestModel)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        try {
            String response = fadadaApi.getClienBase().invokeUploadTemplate(muBanRequestModel.getTemplateId(),muBanRequestModel.getFile(),muBanRequestModel.getPdfUrl());
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject!=null&&jsonObject.containsKey("code")) {
                if (!"1".equals(jsonObject.getString("code"))) {
                    responseData.setStatus(ResultCode.RESPONSE_EXCEPTION.getCode());
                    responseData.setMsg(ResultCode.RESPONSE_EXCEPTION.getMessage());
                }
            }
            responseData.setData(response);
        } catch (Exception e) {
            LOGGER.error("FadadaServiceImpl uploadPdfTemplate has except,request param is "+new Gson().toJson(muBanRequestModel),e);
            responseData.setStatus(ResultCode.REQUEST_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_EXCEPTION.getMessage());
        }

        return responseData;
    }

    /**
     * 生成合同  输入参数为json的 key  value
     * @return
     */
    @Override
    public ResponseData generateContract(GenerateContractModel generateContractModel) {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!Verify.GenerateContractModelverify(generateContractModel)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("contractId",generateContractModel.getContractId());
            String response = fadadaApi.getClienBase().invokeGenerateContract(generateContractModel.getTemplateId(),generateContractModel.getContractId(),
                    generateContractModel.getDocTitle(),generateContractModel.getFontSize(),generateContractModel.getFontType(),generateContractModel.getParameterMap().toJSONString(),null);
            LOGGER.info("generateContract response is "+response);
            map.put("docTitle",generateContractModel.getDocTitle());
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject!=null&&jsonObject.containsKey("code")) {
                if (!"1000".equals(jsonObject.getString("code"))) {
                    responseData.setStatus(ResultCode.RESPONSE_EXCEPTION.getCode());
                    responseData.setMsg(ResultCode.RESPONSE_EXCEPTION.getMessage());
                } else {
                    map.put("downloadUrl",jsonObject.getString("download_url"));
                    map.put("viewPdfUrl",jsonObject.getString("viewpdf_url"));
                }
            } else {
                responseData.setStatus(ResultCode.RESPONSE_EXCEPTION.getCode());
                responseData.setMsg(ResultCode.RESPONSE_EXCEPTION.getMessage());
            }
            responseData.setData(response);
        } catch (Exception e) {
            LOGGER.error("FadadaServiceImpl generateContract has except,request param is "+new Gson().toJson(generateContractModel),e);
            responseData.setStatus(ResultCode.REQUEST_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_EXCEPTION.getMessage());
        }
        //保存mongo
        try {
            mongoApi.upsertContractId(map,"fadada_Result");
        } catch (Exception e) {
            LOGGER.error("generateContract mongo upsertContractId has except ",e);
        }
        return responseData;
    }
    /**
     * 合同签署（自动签）
     */
    @Override
    public ResponseData extSignAuto(ExTSignAutoModel exTSignAutoModel) {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!Verify.ExTSignAutoModelverify(exTSignAutoModel)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        try {
            String response = fadadaApi.getClienBase().invokeExtSignAuto(exTSignAutoModel.getTransactionId(),appProperties.getFadadaKehuNumber(),
                    exTSignAutoModel.getClientRole(),exTSignAutoModel.getContractId(),exTSignAutoModel.getDocTitle(),exTSignAutoModel.getSignKeyword(),exTSignAutoModel.getKeyWordStrategy(),appProperties.getFadadaNotifyUrl());
            LOGGER.info("extSignAuto response is "+response);
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject!=null&&jsonObject.containsKey("code")) {
                if (!"1000".equals(jsonObject.getString("code"))) {
                    responseData.setStatus(ResultCode.RESPONSE_EXCEPTION.getCode());
                    responseData.setMsg(ResultCode.RESPONSE_EXCEPTION.getMessage());
                }
            } else {
                responseData.setStatus(ResultCode.RESPONSE_EXCEPTION.getCode());
                responseData.setMsg(ResultCode.RESPONSE_EXCEPTION.getMessage());
            }
            responseData.setData(response);
        } catch (Exception e) {
            LOGGER.error("FadadaServiceImpl extSignAuto has except,request param is "+new Gson().toJson(exTSignAutoModel),e);
            responseData.setStatus(ResultCode.REQUEST_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_EXCEPTION.getMessage());
        }
        return responseData;
    }

    /**
     * 签章之后法大大回调接口
     * @param fadadaNotifyModel
     * @return
     */
    @Override
    public void fadadaNotify(FadadaNotifyModel fadadaNotifyModel) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().disableHtmlEscaping().create();
        LOGGER.info("FadadaServiceImpl fadadaNotify result is : "+ gson.toJson(fadadaNotifyModel));
        Map<String, Object> map = new HashMap<>();
        if (fadadaNotifyModel!=null) {
            if (fadadaNotifyModel.getResult_code()!=null&&"3000".equals(fadadaNotifyModel.getResult_code())) {  //3000签章成功
                try {
                    //成功以后调用合同归档接口，归档之后合同不能再进行签署操作
                    String json = fadadaApi.getClienBase().invokeContractFilling(fadadaNotifyModel.getContract_id());
                    LOGGER.info("FadadaServiceImpl fadadaNotify invokeContractFilling result is "+json );
                    String qiniuKey = downLoadAndUploanQiniu(fadadaNotifyModel.getDownload_url());
                    map.put("qiniuKey",qiniuKey);
                    map.put("signSuccessFlag",true);
                } catch (Exception e) {
                    LOGGER.error("FadadaServiceImpl fadadaNotify invokeContractFilling has except,request param is "+new Gson().toJson(fadadaNotifyModel),e);
                    map.put("qiniuKey",null);
                    map.put("signSuccessFlag",false);
                }

            } else { //签章失败
                map.put("qiniuKey",null);
                map.put("signSuccessFlag",false);
            }
            //将签章相关信息存mongo
            map.put("contractId",fadadaNotifyModel.getContract_id());//合同编号
            map.put("transactionId",fadadaNotifyModel.getTransaction_id());//交易编号
            map.put("resultCode",fadadaNotifyModel.getResult_code());//结果码
            map.put("resultDesc",fadadaNotifyModel.getResult_desc());//结果描述
            map.put("downloadUrl",fadadaNotifyModel.getDownload_url());//下载地址
            map.put("viewPdfUrl",fadadaNotifyModel.getViewpdf_url());//查看地址
            try {
                mongoApi.upsertContractId(map,"fadada_Result");
            } catch (Exception e) {
                LOGGER.error("fadadaNotify mongo upsertContractId has except ",e);
            }
        }
    }


    //从法大大下载合同 上传到七牛
    @Override
    public String downLoadAndUploanQiniu(String downLoadUrl){
        try {
            URL url = new URL(downLoadUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取字节数组
            byte[] getData = readInputStream(inputStream);
            String res = qiniuApi.upload(getData);
            if(inputStream!=null){
                inputStream.close();
            }
            return res;
        } catch (IOException e) {
            LOGGER.error("FadadaServiceImpl downLoadAndUploanQiniu has except,downLoadUrl is "+downLoadUrl,e);
        }
        return null;

    }

    /**
     * 查看合同
     * @param contractId
     * @return
     */
    @Override
    public ResponseData viewContract(String contractId) {
        ResponseData responseData = ResponseData.success();
        if (contractId!=null&&!"".equals(contractId)) {
            try {
                String viewUrl = fadadaApi.getClientExtra().invokeViewPdfURL(contractId);
                responseData.setData(viewUrl);
            } catch (Exception e) {
                LOGGER.error("viewContract has except,contractId is "+contractId,e);
                responseData = ResponseData.error();
            }
        }
        return responseData;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
