package com.nyd.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.application.dao.*;
import com.nyd.application.entity.Advertising;
import com.nyd.application.entity.Version;
import com.nyd.application.model.PushJiGuangInfo;
import com.nyd.application.model.request.*;
import com.nyd.application.service.IndexService;
import com.nyd.application.service.consts.ApplicationConsts;
import com.nyd.application.service.util.AppProperties;
import com.nyd.application.service.util.HttpsUtils;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Dengw on 2017/11/28
 */
@Service
public class IndexServiceImpl implements IndexService {
    private static Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private AdvertisingDao advertisingDao;

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private WebDao webDao;

    @Autowired(required = false)
    private ProductContract productContract;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private VersionDao versionDao;
    @Autowired
    private ActivityDao activityDao;
    @Override
    public ResponseData<IndexModel> getIndexInfo(IndexRequestModel indexRequestModel){
        LOGGER.info("begin to get indexInfo and indexRequestModel is!" + JSON.toJSONString(indexRequestModel));
        ResponseData responseData = ResponseData.success();
        IndexModel model = new IndexModel();
        model.setMaxCredit("20000");
        //首页消息是否红点
//        try {
//            if (StringUtils.isNotBlank(indexRequestModel.getUserId())) {
//                ResponseData data = noticeForIndexContract.selectIfRedForIndex(indexRequestModel.getUserId(), indexRequestModel.getType());
//                String ifRed = (String) data.getData();
//                model.setIfRed(ifRed);
//            }else {
//                model.setIfRed("0");
//            }
//        }catch (Exception e){
//            LOGGER.error("noticeForIndexContract get ifRed has exception !",e);
//        }
        //是否展示版权，0展示，1不展示
        if(appProperties.getAppShowCopyRight() != null && !"".equals(appProperties.getAppShowCopyRight())){
            model.setShowCopyRight(appProperties.getAppShowCopyRight());
        }
        try {
            List<Version> list = null;
            if (StringUtils.isNotBlank(indexRequestModel.getAppName())) {
                list = versionDao.getVersion("ios", indexRequestModel.getAppName());
            }else {
                list = versionDao.getVersion("ios", "nyd");
            }
            if (list!=null&&list.size()>0) {
                Version version = list.get(0);
                if (version!=null&& StringUtils.isNotBlank(version.getVersionName())){
                    model.setMasterVersion(version.getVersionName());
                }
            }
        } catch (Exception e) {
            LOGGER.error("versionDao get version has exception !",e);
        }
        //获取金融产品信息
        LOGGER.info("get productInfo,business is nyd !");
        ResponseData<List<ProductInfo>> response = productContract.getProductInfoByBusiness("nyd");
        LOGGER.info("金融产品信息"+JSON.toJSONString(response));
        if("0".equals(response.getStatus())){
                List<ProductInfo> productInfoList = response.getData();
                List<ProductModel> productList = new ArrayList<>();
                if(productInfoList != null && productInfoList.size()>0){
                    ProductInfo product = productInfoList.get(0);
                    ProductModel productModel = new ProductModel();
                        BeanUtils.copyProperties(product,productModel);
                        StringBuffer principal = new StringBuffer();
                        if(product.getMinPrincipal().compareTo(product.getMaxPrincipal()) == 0){
                            principal.append(product.getMaxPrincipal());
                            principal.append("元");
                        }else{
                            principal.append(product.getMinPrincipal());
                            principal.append("元-");
                            principal.append(product.getMaxPrincipal());
                            principal.append("元");
                        }
                        productModel.setPrincipal(principal.toString());
                        StringBuffer loanDay = new StringBuffer();
                        if(1 == product.getProductType()){
                            if(product.getMinLoanDay() == product.getMaxLoanDay()){
                                loanDay.append(product.getMaxLoanDay());
                                loanDay.append("期");
                            }else {
                                loanDay.append(product.getMinLoanDay());
                                loanDay.append("期-");
                                loanDay.append(product.getMaxLoanDay());
                                loanDay.append("期");
                            }
                            productModel.setLoanDay(loanDay.toString());
                            productList.add(productModel);
                        }else{
                            if(product.getMinLoanDay() == product.getMaxLoanDay()){
                                loanDay.append(product.getMaxLoanDay());
                                loanDay.append("天");
                            }else{
                                loanDay.append(product.getMinLoanDay());
                                loanDay.append("天-");
                                loanDay.append(product.getMaxLoanDay());
                                loanDay.append("天");
                            }
                            productModel.setLoanDay(loanDay.toString());
                            productList.add(productModel);
                        }
                }
                model.setProductList(productList);
        }
        try {
            List<Advertising> advertList = advertisingDao.getAdvertisings();
            List<String> adList = new ArrayList<>();
            for(Advertising ad : advertList){
                adList.add(ad.getContent());
            }
            model.setAdList(adList);
        } catch (Exception e) {
            LOGGER.error("get advertising error !",e);
        }
        try {
            List<BannerModel> bannerList = null;
            if (StringUtils.isNotBlank(indexRequestModel.getAppName())) {
                 bannerList = bannerDao.getBannersForApp(indexRequestModel.getAppName());
            }else {
                bannerList = bannerDao.getBanners();
            }
            model.setBannerList(bannerList);
        } catch (Exception e) {
            LOGGER.error("get banner error !",e);
        }       
        //组装活动信息
        try {
            List<ActivityModel> activityList = activityDao.getActivitiesByAppName(indexRequestModel.getAppName());
            if(activityList != null && activityList.size()>0){
                model.setActivity(activityList.get(0));
            } else {
                activityList = activityDao.getNydActivities();
                if(activityList != null && activityList.size()>0){
                	model.setActivity(activityList.get(0));
                }
            }
        } catch (Exception e) {
            LOGGER.error("get activity error !",e);
        }              
        responseData.setData(model);
        LOGGER.info("get versionInfo success !");
        return responseData;
    }

    @Override
    public ResponseData saveMessage(MessageModel model) {
        LOGGER.info("begin to save message !");
        ResponseData responseData = ResponseData.success();
        try {
            messageDao.save(model);
        } catch (Exception e) {
            LOGGER.error("save message error ! info = "+model.toString(),e);
            return ResponseData.error(ApplicationConsts.ERROR_MSG);
        }
        LOGGER.info("save message success !");
        return responseData;
    }

    @Override
    public ResponseData<Map<String,String>> getWebInfo() {
        LOGGER.info("begin to get webKeyList !");
        ResponseData responseData = ResponseData.success();
        try {
            List<WebModel> list = webDao.getWebInfos();
            Map<String,String> map = new HashMap<>();
            if(list != null && list.size()>0){
                for(WebModel model : list){
                    map.put(model.getWebKey(),model.getWebValue());
                }
                responseData.setData(map);
            }
        } catch (Exception e) {
            LOGGER.error("get webKeyList error !",e);
            responseData = ResponseData.error(ApplicationConsts.ERROR_MSG);
            return responseData;
        }
        LOGGER.info("get webKeyList success !");
        return responseData;
    }

    @Override
    public ResponseData pushBindClient(JiGuangDto jiGuangDto) {
        LOGGER.info("begin to pushBindClient ! param is "+JSON.toJSONString(jiGuangDto));
        if (jiGuangDto==null) {
            return ResponseData.error("参数不能为空");
        }
        ResponseData responseData = ResponseData.success();
        try {
            PushJiGuangInfo pushJiGuangInfo = new PushJiGuangInfo();
            BeanUtils.copyProperties(jiGuangDto,pushJiGuangInfo);
            pushJiGuangInfo.setProviderId("1");
            pushJiGuangInfo.setAppCode(jiGuangDto.getAppName());
            pushJiGuangInfo.setMobile(jiGuangDto.getAccountNumber());
            String url = appProperties.getJiguangPushUrl()+"/push/bindClient";
            String result = doPushJiGuang(url,JSON.toJSONString(pushJiGuangInfo));
            LOGGER.info("pushJiGuangInfo result is "+JSON.toJSONString(result));
        } catch (BeansException e) {
            LOGGER.error("pushBindClient has error!  param is "+JSON.toJSONString(jiGuangDto));
            return ResponseData.error();
        }
        return responseData;
    }

    private String doPushJiGuang(String url, String jsonStr) {
        int retryCount=3;
        String result = null;
        do {
            try {
                result = HttpsUtils.sendPost(url,jsonStr);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    return result;
                } else {
                    retryCount--;
                }
            } catch (Exception e) {
                LOGGER.error("doCreateWithhold has exception",e);
                retryCount--;
            }
        } while (retryCount>0);
        return result;
    }

}
