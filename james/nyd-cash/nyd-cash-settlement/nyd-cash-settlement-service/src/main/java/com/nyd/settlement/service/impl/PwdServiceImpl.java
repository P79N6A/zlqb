package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.PwdDao;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.entity.Pwd;
import com.nyd.settlement.model.dto.PwdDto;
import com.nyd.settlement.service.PwdService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.utils.MD5Utils;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@Service
public class PwdServiceImpl implements PwdService{

    Logger logger = LoggerFactory.getLogger(PwdServiceImpl.class);
    @Autowired
    private PwdDao pwdDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData validatePwd(PwdDto pwdDto) {
        logger.info("111");
        try {
            if (pwdDto.getType() == null) {
                return ResponseData.error("未设置pwd类型");
            }
            logger.info("222");
            Pwd pwd = pwdDao.selectPwdByType(pwdDto.getType());
            logger.info("333");
            String md5Pwd = MD5Utils.MD5(pwdDto.getPwd());
            logger.info("md5Pwd为"+md5Pwd);
            if (md5Pwd.equals(pwd.getPassword())) {
                return ResponseData.success();
            }else {
                return ResponseData.error("密码错误");
            }
        }catch (Exception e){
            return ResponseData.error("异常");
        }
    }
}
