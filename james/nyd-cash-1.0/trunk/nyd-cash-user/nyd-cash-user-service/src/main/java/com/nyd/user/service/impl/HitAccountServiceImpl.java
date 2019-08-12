package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.user.dao.HitAccountDao;
import com.nyd.user.entity.HitAccount;
import com.nyd.user.model.HitAccountInfo;
import com.nyd.user.service.HitAccountService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 12:30 2018/9/6
 */
@Service
public class HitAccountServiceImpl implements HitAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HitAccountServiceImpl.class);

    @Autowired
    private HitAccountDao hitAccountDao;

    @Override
    public ResponseData saveHitAccount(HitAccountInfo hitAccountInfo) {
        LOGGER.info("HitAccountServiceImpl saveHitAccount，param is :"+ JSON.toJSONString(hitAccountInfo));
        ResponseData responseData = ResponseData.success();
        if(null==hitAccountInfo|| StringUtils.isBlank(hitAccountInfo.getAccountNumber())){
            responseData = ResponseData.error("参数异常");
            return responseData;
        }
        try {
            List<HitAccountInfo> hitAccountInfos= hitAccountDao.getByMobile(hitAccountInfo.getAccountNumber());
            if(null!=hitAccountInfos && hitAccountInfos.size()>0){
                LOGGER.warn("该手机号已经存在，不可再新增 mobile："+hitAccountInfo.getAccountNumber());
                responseData = ResponseData.error("服务器开小差");
                return responseData;
            }
            HitAccount hitAccount = new HitAccount();
            BeanUtils.copyProperties(hitAccountInfo,hitAccount);
            hitAccountDao.insert(hitAccount);
        } catch (Exception e) {
            LOGGER.error("HitAccountServiceImpl saveHitAccount,mobile "+hitAccountInfo.getAccountNumber(),e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<HitAccountInfo> getByStr(HitAccountInfo hitAccountInfo, Integer type) {
        LOGGER.info("HitAccountServiceImpl getByStr，param hitAccountInfo:{}  type:{}",hitAccountInfo,type);
        ResponseData responseData = ResponseData.success();
        if(null==hitAccountInfo){
            responseData = ResponseData.error("参数异常");
            return responseData;
        }
        try {
            List<HitAccountInfo>  hitAccountInfos = null;
            if(null==type||type==0){
                if(StringUtils.isNotBlank(hitAccountInfo.getAccountNumber())){
                    hitAccountInfos = hitAccountDao.getByMobile(hitAccountInfo.getAccountNumber());
                }
            }else if(type==1){
                if(StringUtils.isNotBlank(hitAccountInfo.getSecretShaStr())){
                    hitAccountInfos = hitAccountDao.getBySha256Str(hitAccountInfo.getSecretShaStr());
                }
            }else if (type ==2){
                if(StringUtils.isNotBlank(hitAccountInfo.getSecretMdStr())){
                    hitAccountInfos = hitAccountDao.getByMd5Str(hitAccountInfo.getSecretMdStr());
                }
            }
            LOGGER.info("HitAccountServiceImpl getByStr hitAccountInfos:"+JSON.toJSONString(hitAccountInfos));
            if(null!=hitAccountInfos && hitAccountInfos.size()>0){
                responseData.setData(hitAccountInfos.get(0));
            }
        }catch (Exception e){
            LOGGER.error("HitAccountServiceImpl getByStr exception ，param is "+JSON.toJSONString(hitAccountInfo),e);
            ResponseData.error("服务器开小差");
        }

        return responseData;
    }
}
