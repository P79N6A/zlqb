package com.nyd.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.SalesPlatformMapper;
import com.nyd.admin.model.SalesPlatformInfo;
import com.nyd.admin.model.dto.SalesPlatformDto;
import com.nyd.admin.service.SalesPlatformService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by hwei on 2018/12/3.
 */
@Service
public class SalesPlatformServiceImpl implements SalesPlatformService {
    private static Logger logger = LoggerFactory.getLogger(SalesPlatformServiceImpl.class);
    @Autowired
    private SalesPlatformMapper salesPlatformMapper;

    @Override
    public ResponseData findRegisterUnfilledData(SalesPlatformDto salesPlatformDto) {
        ResponseData responseData = ResponseData.success();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = salesPlatformDto.getStartTime();
            if (StringUtils.isBlank(startTime)) {
                startTime = getStartTime();
            }
            String endTime = salesPlatformDto.getEndTime();
            if (StringUtils.isBlank(endTime)) {
                endTime = sdf.format(new Date());
            }
            logger.info("查询的开始时间是：" + startTime + ";结束时间是：" + endTime);
            Integer pageNum = salesPlatformDto.getPageNum();
            Integer pageSize = salesPlatformDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 100;
            }
            Integer total = salesPlatformMapper.findRegisterUnfilledDataCount(startTime, endTime);
            logger.info("注册未填写资料用户个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<SalesPlatformInfo> resultList = salesPlatformMapper.findRegisterUnfilledData(startTime, endTime);
            logger.info("查询到注册未填写资料的用户数据是：" + resultList);
            responseData.setData(resultList);
        } catch (Exception e) {
            logger.error("查询注册未填写资料用户出错！",e);
            return ResponseData.error("服务器开小差，请稍后重试！");
        }
        return responseData;
    }

    @Override
    public ResponseData findDataIncomplete(SalesPlatformDto salesPlatformDto) {
        ResponseData responseData = ResponseData.success();
        try {
            String startTime = salesPlatformDto.getStartTime();
            if (StringUtils.isBlank(startTime)) {
                startTime = getStartTime();
            }
            String endTime = salesPlatformDto.getEndTime();
            if (StringUtils.isBlank(endTime)) {
                endTime = oneMonth();
            }
            logger.info("开始时间是：" + startTime + "结束时间是：" + endTime);
            Integer pageNum = salesPlatformDto.getPageNum();
            Integer pageSize = salesPlatformDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 100;
            }
            Integer total = salesPlatformMapper.findDataIncompleteCount(startTime, endTime);
            logger.info("资料填写不完整用户个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<SalesPlatformInfo> resultList = salesPlatformMapper.findDataIncomplete(startTime, endTime);
            logger.info("查询到注册资料不完整的用户数据是：" + resultList);
            responseData.setData(resultList);
        } catch (Exception e) {
            logger.error("查询资料填写不完整客户出错！",e);
            return ResponseData.error("服务器开小差，请稍后重试！");
        }
        return responseData;
    }

    @Override
    public ResponseData findLoadSuccess(SalesPlatformDto salesPlatformDto) {
        ResponseData responseData = ResponseData.success();
        try {
            String startTime = salesPlatformDto.getStartTime();
            if (StringUtils.isBlank(startTime)) {
                startTime = getStartTime();
            }
            String endTime = salesPlatformDto.getEndTime();
            if (StringUtils.isBlank(endTime)) {
                endTime = twentyDay();
            }
            logger.info("开始时间是：" + startTime + "结束时间是：" + endTime);
            Integer pageNum = salesPlatformDto.getPageNum();
            Integer pageSize = salesPlatformDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 100;
            }
            Integer total = salesPlatformMapper.findLoadSuccessCount(startTime,endTime);
            logger.info("借款成功的用户个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<SalesPlatformInfo> resultList = salesPlatformMapper.findLoadSuccess(startTime, endTime);
            logger.info("查询成功借款超过20天的用户数据是：" + resultList);
            responseData.setData(resultList);
        } catch (Exception e) {
            logger.error("查询成功借款超过20天的用户数据出错！",e);
            return ResponseData.error("服务器开小差，请稍后重试！");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public boolean checkUser(String mobile) {
        boolean flag = false;
        Map<String, String> userMap = new HashMap<>();
        userMap.put("mobile", mobile);
        List<String> userList = salesPlatformMapper.selectByMobile(userMap);
        if (userList.size() > 0) {
            flag = true;
        }
        return flag;
    }


    /**
     * 获取每天的零点时间
     * @return
     */
    private String getStartTime(){
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        logger.info("获取起始时间是：" + sdf.format(startTime));
        return sdf.format(startTime);
    }

    private String oneMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);
        Date data = calendar.getTime();
        String startTime = sdf.format(data);
        logger.info("1个月之前的时间是：" + startTime);
        return startTime;
    }

    private String twentyDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-20);
        Date data = calendar.getTime();
        String startTime = sdf.format(data);
        logger.info("20天之前的时间是：" + startTime);
        return startTime;
    }

}
