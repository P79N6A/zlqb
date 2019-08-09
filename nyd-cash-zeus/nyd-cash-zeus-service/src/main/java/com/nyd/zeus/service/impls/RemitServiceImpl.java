package com.nyd.zeus.service.impls;

import com.nyd.zeus.api.RemitContract;
import com.nyd.zeus.dao.RemitDao;
import com.nyd.zeus.dao.mapper.RemitMapper;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RemitModel;
import com.nyd.zeus.service.RemitService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
@Service(value = "remitContract")
public class RemitServiceImpl implements RemitContract, RemitService {

    private static Logger LOGGER = LoggerFactory.getLogger(RemitServiceImpl.class);

    @Autowired
    RemitDao remitDao;
    @Autowired
    private RemitMapper remitMapper;
    /**
     * 保存放款信息
     * @param remitInfo
     * @return
     * @throws Exception
     */
    @Override
    public ResponseData save(RemitInfo remitInfo) throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
            remitDao.save(remitInfo);
        } catch (Exception e) {
            responseData = ResponseData.error("保存放款信息失败");
            LOGGER.error("save remitInfo error! orderNo = "+ remitInfo.getOrderNo(), e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取放款信息
     * @param orderNo
     * @return
     * @throws Exception
     */
    @Override
    public ResponseData getRemitInfoByOrderNo(String orderNo) throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
            List<RemitInfo> remitInfoList = remitDao.getRemitInfoByOrderNo(orderNo);
            responseData.setData(remitInfoList);
        } catch (Exception e) {
            responseData = ResponseData.error("获取放款信息失败");
            LOGGER.error("getRemitInfoByOrderNo  error! orderNo = " + orderNo, e.getMessage());
        }
        return responseData;
    }

    @Override
    public ResponseData updateStatus(String remitNo) throws Exception {
        ResponseData responseData = ResponseData.success();
        try {
            remitDao.update(remitNo);
        } catch (Exception e) {
            responseData = ResponseData.error("更改放款信息失败");
            LOGGER.error("save remitInfo error! orderNo = "+ remitNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData selectTime(String assetNo) {
        ResponseData responseData = ResponseData.success();
        try {
            List<RemitInfo> remitInfoList = remitMapper.selectTime(assetNo);
            if (remitInfoList.size()>0) {
                responseData.setData(remitInfoList.get(0).getRemitTime());
            }
        } catch (Exception e) {
            responseData = ResponseData.error("获取放款时间失败");
            LOGGER.error("获取放款时间失败! assetNo = " + assetNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<RemitModel>> getByCreateTime(int state, Date startTime, Date endTime) {
        LOGGER.info("getRemitByCreateTime start,param is state:{} startTime:{} endTime:{}",state,startTime,endTime);
        ResponseData responseData = ResponseData.success();
        try {
            if(null==endTime){
                endTime = new Date();
            }
            SimpleDateFormat sdf;
            if (state == 1) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }else {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            if(null!=startTime){
                startTime = sdf.parse(sdf.format(startTime));
            }
            if(null!=endTime){
                endTime = sdf.parse(sdf.format(endTime));
            }
            List<RemitModel> remitInfos = remitDao.getByCreateTimeAndStatus(state,startTime,endTime);
            if(null!=remitInfos && remitInfos.size()>0){
                responseData.setData(remitInfos);
            }
        } catch (Exception e) {
            LOGGER.error("getRemitByCreateTime exception ",e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<List<RemitInfo>> getSuccessRemit(Date startTime, Date endTime) {
        ResponseData responseData = ResponseData.success();
        List<RemitInfo> successRemit = remitMapper.getSuccessRemit(startTime, endTime);
        responseData.setData(successRemit);
        return responseData;
    }
}
