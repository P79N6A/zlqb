package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.dao.CustomerServiceLogDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.entity.CustomerServiceLog;
import com.nyd.admin.model.Info.CustomerServiceLogInfo;
import com.nyd.admin.model.Vo.CustomerServiceLogVo;
import com.nyd.admin.model.dto.CustomerServiceLogDto;
import com.nyd.admin.service.CustomerServiceLogService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceLogServiceImpl implements CustomerServiceLogService {
    private static Logger logger = LoggerFactory.getLogger(CustomerServiceLogServiceImpl.class);

    @Autowired
    private CustomerServiceLogDao customerServiceLogDao;

    /**
     * 保存客服记录
     * @param customerServiceLogDto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveCustomerServiceLog(CustomerServiceLogDto customerServiceLogDto) {
        logger.info("保存客服记录入参:"+ JSON.toJSON(customerServiceLogDto));
        ResponseData response = ResponseData.success();
        if (!StringUtils.isNotBlank(customerServiceLogDto.getRealName())){
            return ResponseData.error("请传用户姓名");
        }
        if (!StringUtils.isNotBlank(customerServiceLogDto.getUserId())){
            return ResponseData.error("请传用户编号");
        }
        if (!StringUtils.isNotBlank(customerServiceLogDto.getOperationPerson())){
            return ResponseData.error("获取操作人员姓名失败");
        }
        try {
            CustomerServiceLog customerServiceLog = new CustomerServiceLog();
            if (StringUtils.isNotBlank(customerServiceLogDto.getRemark())){
                customerServiceLog.setRemark(customerServiceLogDto.getRemark());
            }
            customerServiceLog.setUserId(customerServiceLogDto.getUserId());
            customerServiceLog.setUserName(customerServiceLogDto.getRealName());
            if (StringUtils.isNotBlank(customerServiceLogDto.getOperationPerson())){
                customerServiceLog.setOperationPerson(customerServiceLogDto.getOperationPerson());
            }
            logger.info("保存客服记录对象:"+JSON.toJSON(customerServiceLog));
            customerServiceLogDao.save(customerServiceLog);
            logger.info("保存客服记录成功");


        }catch (Exception e){
            logger.error("保存客服操作记录出错",e);
        }
        return response;
    }

    /**
     *
     * 查询客服记录
     * @param userId
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findCustomerServiceList(String userId) {
        ResponseData response = ResponseData.success();
        if (!StringUtils.isNotBlank(userId) ){
            return ResponseData.error("缺少查询参数");
        }
        List<CustomerServiceLogVo> customerServiceLogVoList = new ArrayList<>();
        try {
            List<CustomerServiceLogInfo> list = customerServiceLogDao.findCustomerServiceListByUserId(userId);
            logger.info("查询客服记录集合:"+JSON.toJSON(list));
            if (list != null && list.size() > 0){
                for (CustomerServiceLogInfo customerServiceLogInfo : list){
                    CustomerServiceLogVo customerServiceLogVo = new CustomerServiceLogVo();
                    BeanUtils.copyProperties(customerServiceLogInfo,customerServiceLogVo);
                    //操作日期
                    Date time = customerServiceLogInfo.getCreateTime();
                    String operationTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(time);
                    customerServiceLogVo.setOperationTime(operationTime);
                    logger.info("客服操作记录对象:"+JSON.toJSON(customerServiceLogVo));
                    customerServiceLogVoList.add(customerServiceLogVo);
                }
            }
            response.setData(customerServiceLogVoList);
        }catch (Exception e){
            logger.info("查询客服记录出错",e);
        }
        return response;
    }
}
