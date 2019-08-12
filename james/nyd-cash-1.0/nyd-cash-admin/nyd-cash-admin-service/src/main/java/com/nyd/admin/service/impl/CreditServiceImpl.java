package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.CreditMapper;
import com.nyd.admin.model.Info.CreditInfo;
import com.nyd.admin.model.Info.UserInfo;
import com.nyd.admin.model.dto.CreditDto;
import com.nyd.admin.model.dto.CreditRemarkDto;
import com.nyd.admin.service.CreditService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:11
 */
@Service
public class CreditServiceImpl implements CreditService {

    private static Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);

    @Autowired
    private CreditMapper creditMapper;

    /**
     * 授信查询
     * @param creditDto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData findCreditDetails(CreditDto creditDto) {
        ResponseData responseData = ResponseData.success();
        if(creditDto == null){
            return responseData.error("creditDto对象为空！");
        }
        logger.info("授信查询start param is：" + JSON.toJSONString(creditDto));
        List<CreditInfo> list = new ArrayList<>();
        try {
            Integer pageNum = creditDto.getPageNum();
            Integer pageSize = creditDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 5;
            }

            String beginTime = creditDto.getCreateBeginTime();
            String endTime = creditDto.getCreateEndTime();
            if (StringUtils.isBlank(endTime)) {
                //将时间设置当前时间
                endTime = getEndTime();
                creditDto.setCreateEndTime(endTime);
                logger.info("查询结束时间是1：" + endTime);
            } else {
                //如果有传入结束时间，那么就判断传入的结束时间是否在近3天的范围内
                boolean isFlag = isEndTime(endTime);
                if (isFlag) {
                    return responseData.error("您所选的时间太早，请重新选择时间！");
                }
            }

            if (StringUtils.isBlank(beginTime)) {
                //将时间设置成当前时间的前三天的时间
                beginTime = getStartTime();
                creditDto.setCreateBeginTime(beginTime);
                logger.info("查询开始时间是1：" + beginTime);
            } else {
                //判断起始时间是否在近3天的起始时间内
                boolean isFlag = isEndTime(beginTime);
                if (isFlag) {
                    beginTime = getStartTime();
                    creditDto.setCreateBeginTime(beginTime);
                    logger.info("查询的开始时间是2：" + beginTime);
                }
            }
            //进行查询个数
            Integer total = creditMapper.findCount(creditDto);
            logger.info("授信查询总个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            //查询详情
            PageHelper.startPage(pageNum, pageSize);
            List<CreditInfo> creditDetailResult = creditMapper.findCreditDetails(creditDto);
            logger.info("授信查询结果 result is ：" + creditDetailResult);
            if (creditDetailResult != null && creditDetailResult.size() > 0) {
                for (CreditInfo creditInfo:creditDetailResult) {
                    if (creditInfo.getOs() == null) {
                        creditInfo.setOs("");
                    }
                    if (creditInfo.getUserId() != null) {
                        List<UserInfo> userInfoList = creditMapper.findRealNameAndGender(creditInfo.getUserId());
                        if (userInfoList != null && userInfoList.size() > 0) {
                            UserInfo userInfo = userInfoList.get(0);
                            if (userInfo.getRealName() != null) {
                                creditInfo.setRealName(userInfo.getRealName());
                            } else {
                                creditInfo.setRealName("");
                            }
                            if (userInfo.getGender() != null) {
                                creditInfo.setGender(userInfo.getGender());
                            } else {
                                creditInfo.setGender("");
                            }
                        }
                    } else {
                        creditInfo.setRealName("");
                        creditInfo.setGender("");
                    }
                    //查询操作状态
                    String remark = creditMapper.findRemark(creditInfo.getAccountNumber());
                    if (remark != null) {
                        creditInfo.setRemark(remark);
                    } else {
                        creditInfo.setRemark("");
                    }
                    list.add(creditInfo);
                }
            }
            logger.info("授信获取的结果集为：" + list);
            PageInfo pageInfo = new PageInfo(list);
            pageInfo.setTotal(total);
            responseData.setData(pageInfo);
        } catch (Exception e) {
            logger.error("授信查询出错~~~~", e);
            return responseData.error("服务器开小差了！");
        }
        return responseData;
    }

    /**
     * 操作接口
     * @param creditRemarkDto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData updateCreditRemark(CreditRemarkDto creditRemarkDto) {
        ResponseData responseData = ResponseData.success();
        if(creditRemarkDto == null){
            return responseData.error("creditRemarkDto对象为空！");
        }
        if(creditRemarkDto.getAccountNumber() == null){
            return responseData.error("accountNumber为空！");
        }
        if(creditRemarkDto.getRemark() == null || "".equals(creditRemarkDto.getRemark())){
            return responseData.error("请选择一个操作选项！");
        }
        try {
            //1.先判断t_credit_log表中是否有记录，如果没有，则新增一条
            String remark = creditMapper.findRemark(creditRemarkDto.getAccountNumber());
            if (remark != null) {
                //表示有记录，那么根据手机号进行更改
                Integer rows = creditMapper.updateCreditRemark(creditRemarkDto);
                if(rows == 1){
                    logger.info("t_credit_log表中修改一条记录成功，accountNumber为:" + creditRemarkDto.getAccountNumber());
                } else {
                    logger.info("t_credit_log表中修改一条记录失败，accountNumber为:" + creditRemarkDto.getAccountNumber());
                    return responseData.error("操作失败，accountNumber为:" + creditRemarkDto.getAccountNumber());
                }
            } else {
                //没有记录则直接生成一条记录
                Integer rows = creditMapper.insertCreditLog(creditRemarkDto);
                if(rows == 1){
                    logger.info("t_credit_log表中新增一条记录成功，accountNumber为:" + creditRemarkDto.getAccountNumber());
                } else {
                    logger.info("t_credit_log表中新增一条记录失败，accountNumber为:" + creditRemarkDto.getAccountNumber());
                    return responseData.error("新增操作失败，accountNumber为:" + creditRemarkDto.getAccountNumber());
                }
            }
        } catch (Exception e) {
            logger.error("操作状态失败！！！~~~~", e);
            return responseData.error("服务器开小差了！");
        }
        return responseData;
    }

    private boolean isEndTime(String time){
        boolean isFlag = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 3);
        calendar.getTime();
        String threeDay = sdf.format(calendar.getTime());
        System.out.println("三天前的时间是：" + threeDay);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(threeDay, dtf);
        LocalDateTime localTime = LocalDateTime.parse(time, dtf);
        //判断传入的结束时间是否在三天前的时间的前面
        if (localTime.isBefore(startTime)) {
            isFlag = true;
        }
        return isFlag;
    }

    /**
     * 获取当前时间
     * @return
     */
    private String getEndTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date time = calendar.getTime();
        String nowTime = sdf.format(time);
        logger.info("获取的当前时间是:" + nowTime);
        return nowTime;
    }

    /**
     * 获取起始时间（3天前的时间）
     * @return
     */
    private String getStartTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        calendar1.set(Calendar.DATE, calendar1.get(Calendar.DATE) - 3);
        calendar1.getTime();
        String threeDay = sdf.format(calendar1.getTime());
        System.out.println("三天前的时间是：" + threeDay);
        return threeDay;
    }
}
