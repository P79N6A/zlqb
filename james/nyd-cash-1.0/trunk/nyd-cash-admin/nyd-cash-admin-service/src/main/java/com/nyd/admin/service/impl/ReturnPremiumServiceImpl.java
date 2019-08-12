package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ReturnPremiumDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.ReturnPremiumMapper;
import com.nyd.admin.entity.ReturnPremium;
import com.nyd.admin.model.Info.IntegratedVolumeInfo;
import com.nyd.admin.model.dto.*;
import com.nyd.admin.model.enums.RefundTicketType;
import com.nyd.admin.model.enums.ReturnPremiumState;
import com.nyd.admin.service.ReturnPremiumService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.dto.BatchRefundTicketDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReturnPremiumServiceImpl implements ReturnPremiumService {
    private static Logger logger = LoggerFactory.getLogger(ReturnPremiumServiceImpl.class);

    @Autowired
    private ReturnPremiumMapper returnPremiumMapper;

    @Autowired
    private ReturnPremiumDao returnPremiumDao;

    @Autowired
    private UserAccountContract userAccountContract;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveReturnPremiumLabel(ReturnPremiumLabelDto returnPremiumLabelDto) {
        ResponseData response = ResponseData.success();

        if (!StringUtils.isNotBlank(returnPremiumLabelDto.getMobile())){
            return ResponseData.error("手机号为空");
        }
        if (!StringUtils.isNotBlank(returnPremiumLabelDto.getUserId())){
            return ResponseData.error("客户编号为空");
        }

        if (!StringUtils.isNotBlank(returnPremiumLabelDto.getUserName())){
            return ResponseData.error("客户姓名为空");
        }

        try {
            List<ReturnPremium> list = returnPremiumMapper.selectIfLabelToday(returnPremiumLabelDto.getMobile());
            if (list != null && list.size() > 0){
                return ResponseData.error("此用户今天已经做过退费标记");
            }
            ReturnPremium returnPremium = new ReturnPremium();
            returnPremium.setPremiumId(UUID.randomUUID().toString().replace("-",""));
            returnPremium.setMobile(returnPremiumLabelDto.getMobile());
            //退券状态   1为已退费，2为未退费, 3为退费失败
            returnPremium.setState(ReturnPremiumState.ReturnPremiumState_Two.getCode());
            returnPremium.setUserId(returnPremiumLabelDto.getUserId());
            returnPremium.setUserName(returnPremiumLabelDto.getUserName());
            returnPremium.setUpdateBy(returnPremiumLabelDto.getOperatePerson());
            if (returnPremiumLabelDto.getRemark() != null) {
                returnPremium.setRemark(returnPremiumLabelDto.getRemark());
            }
            //标记退券类型,目前 1:小银券  2：其他类型的券
            returnPremium.setRefundTicketType(RefundTicketType.RefundTicketType_one.getCode());
            logger.info("退费标记对象:"+ JSON.toJSON(returnPremium));
            returnPremiumDao.save(returnPremium);

        }catch (Exception e){
            logger.error("退费标记出错",e);
        }
        return response;
    }

    /**
     * 综合券管理（侬要贷）查询
     * @param integratedVolumeDto
     * @return
     */
    @Override
    public ResponseData findIntegratedVolumeDetails(IntegratedVolumeDto integratedVolumeDto) {
        ResponseData response = ResponseData.success();
        if(integratedVolumeDto == null){
            return response.error("integratedVolumeDto is null~~~");
        }
        try{
            List<IntegratedVolumeInfo> list = new ArrayList<>();
            Integer pageNum = integratedVolumeDto.getPageNum();
            Integer pageSize = integratedVolumeDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 20;
            }
            Integer total = returnPremiumMapper.getIntegratedVolumeDetailsCount(integratedVolumeDto);
            logger.info("综合卷管理（侬要贷）查询总个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<IntegratedVolumeInfo> resultList = returnPremiumMapper.findIntegratedVolumeDetails(integratedVolumeDto);
            logger.info("综合卷管理（侬要贷）查询 resultList is " + resultList);
            if(resultList != null && resultList.size() > 0){
                for (IntegratedVolumeInfo integratedVolume:resultList) {
                    if(integratedVolume.getTicketProvideTime() == null){
                        integratedVolume.setTicketProvideTime("");
                    }
                    if(integratedVolume.getTicketAmount() == null){
                        integratedVolume.setTicketAmount(new BigDecimal(0));
                    }

                    list.add(integratedVolume);
                }
            }
            PageInfo pageInfo = new PageInfo(list);
            pageInfo.setTotal(total);
            response.setData(pageInfo);
        } catch (Exception e) {
            logger.error("综合卷管理（侬要贷）查询出错~~~~", e);
            return response.error("服务器开小差了！");
        }
        return response;
    }

    /**
     * 批量发券
     * @param batchCouponDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData batchCoupons(BatchCouponDto batchCouponDto) {
        ResponseData response = ResponseData.success();
        if(batchCouponDto == null){
            return response.error("batchCouponDto is null");
        }
        if(batchCouponDto.getPremiumIdList().size() == 0){
            return response.error("请至少选中一个发放对象");
        }
        if(batchCouponDto.getTicketAmount().compareTo(new BigDecimal(0)) <= 0){
            return response.error("金额太小，请正确输入金额");
        }
        if(batchCouponDto.getTicketAmount().compareTo(new BigDecimal(1000)) > 0){
            return response.error("金额过大，请谨慎操作");
        }
        try {
            List<BatchUserDto> premiumIdList = batchCouponDto.getPremiumIdList();//被选中要退券的集合
            List<ReturnTicketLogDto> logList = new ArrayList<>();//更新日志表的集合

            //1.将t_account表更新 需要的值给到user服务
            BatchRefundTicketDto batchRefund = new BatchRefundTicketDto();
            batchRefund.setReturnTicketAmount(batchCouponDto.getTicketAmount());//退券金额
            batchRefund.setReturnTicketType(batchCouponDto.getRefundTicketType());//券类型（1:小银券）
            List<BatchRefundTicketDto.BatchObject> batchObjectList = new ArrayList<>();
            for (BatchUserDto batchUser:premiumIdList){//遍历获取选中的手机号
                BatchRefundTicketDto.BatchObject batchObject = new  BatchRefundTicketDto.BatchObject();//内嵌对象
                batchObject.setAccountNumber(batchUser.getMobile());//手机号
                batchObject.setPremiumId(batchUser.getPremiumId());//唯一标识
                batchObjectList.add(batchObject);
            }
            batchRefund.setAccountNumbers(batchObjectList);
            logger.info("更改账户余额入参:" + batchRefund);
            ResponseData responseData = userAccountContract.BatchRefundTicket(batchRefund);
            logger.info("更改账户余额 结果:" + responseData);
            //2.更新t_return_premium表中的订单状态
            if(responseData != null && "0".equals(responseData.getStatus())){
                List<String> list = (List<String>)responseData.getData();
                if(list != null && list.size() > 0){
                    //有更新失败的对象
                    for (String premiumId:list) {
                        for (BatchUserDto batchUser:premiumIdList) {
                            if(premiumId.equals(batchUser.getPremiumId())){
                                batchUser.setTicketAmount(batchCouponDto.getTicketAmount());
                                batchUser.setUpdateBy(batchCouponDto.getUpdateBy());
                                batchUser.setType(batchCouponDto.getRefundTicketType());
                                batchUser.setState(ReturnPremiumState.ReturnPremiumState_Three.getCode());//退费失败
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                batchUser.setTicketProvideTime(format.format(new Date()));
                                ReturnTicketLogDto returnTicketLogDto = new ReturnTicketLogDto();
                                BeanUtils.copyProperties(batchUser,returnTicketLogDto);
                                returnTicketLogDto.setState(1);//表示发券失败
                                logList.add(returnTicketLogDto);
                                //更新标记表
                                logger.info("更新退费标记 对象信息（1）：" + JSON.toJSONString(batchUser));
                                Integer rows = returnPremiumMapper.updatePremiumBypremiumId(batchUser);
                                if(rows == 1){
                                    logger.info("唯一标识（1）：" + batchUser.getPremiumId() + " 更新成功！！");
                                } else {
                                    logger.info("唯一标识（1）：" + batchUser.getPremiumId() + " 更新失败！！");
                                }
                                //3.新增t_return_ticket_log表记录
                                logger.info("退费记录表更新 入参（1）：" + logList);
                                Integer logRows = returnPremiumMapper.insertRetuenTicketLog(logList);
                                logger.info("新增t_return_ticket_log表记录条数（1）：" + logRows);
                            } else {
                                batchUser.setTicketAmount(batchCouponDto.getTicketAmount());
                                batchUser.setUpdateBy(batchCouponDto.getUpdateBy());
                                batchUser.setType(batchCouponDto.getRefundTicketType());
                                batchUser.setState(ReturnPremiumState.ReturnPremiumState_one.getCode());//退费成功
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                batchUser.setTicketProvideTime(format.format(new Date()));
                                ReturnTicketLogDto returnTicketLogDto = new ReturnTicketLogDto();
                                BeanUtils.copyProperties(batchUser,returnTicketLogDto);
                                returnTicketLogDto.setState(0);//表示发券成功
                                logList.add(returnTicketLogDto);
                                logger.info("更新退费标记 对象信息（2）：" + JSON.toJSONString(batchUser));
                                Integer rows = returnPremiumMapper.updatePremiumBypremiumId(batchUser);
                                if(rows == 1){
                                    logger.info("唯一标识（2）：" + batchUser.getPremiumId() + " 更新成功！！");
                                } else {
                                    logger.info("唯一标识（2）：" + batchUser.getPremiumId() + " 更新失败！！");
                                }
                                //3.新增t_return_ticket_log表记录
                                logger.info("退费记录表更新 入参（2）：" + logList);
                                Integer logRows = returnPremiumMapper.insertRetuenTicketLog(logList);
                                logger.info("新增t_return_ticket_log表记录条数（2）：" + logRows);
                            }
                        }
                    }
                }else{
                    //全部更新成功
                    for (BatchUserDto batchUser:premiumIdList) {
                        batchUser.setTicketAmount(batchCouponDto.getTicketAmount());
                        batchUser.setUpdateBy(batchCouponDto.getUpdateBy());
                        batchUser.setType(batchCouponDto.getRefundTicketType());
                        batchUser.setState(ReturnPremiumState.ReturnPremiumState_one.getCode());//已退费
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        batchUser.setTicketProvideTime(format.format(new Date()));
                        ReturnTicketLogDto returnTicketLogDto = new ReturnTicketLogDto();
                        BeanUtils.copyProperties(batchUser,returnTicketLogDto);
                        returnTicketLogDto.setState(0);//表示发券成功
                        logList.add(returnTicketLogDto);
                        logger.info("更新退费标记 对象信息（3）：" + JSON.toJSONString(batchUser));
                        Integer rows = returnPremiumMapper.updatePremiumBypremiumId(batchUser);
                        if(rows == 1){
                            logger.info("唯一标识（3）：" + batchUser.getPremiumId() + " 更新成功！！");
                        } else {
                            logger.info("唯一标识（3）：" + batchUser.getPremiumId() + " 更新失败！！");
                        }
                    }
                    logger.info("退费记录表更新 入参（3）：" + logList);
                    Integer logRows = returnPremiumMapper.insertRetuenTicketLog(logList);
                    logger.info("新增t_return_ticket_log表记录条数（3）：" + logRows);
                }
            } else {
                //全部更新失败的逻辑
                for (BatchUserDto batchUser:premiumIdList) {
                    batchUser.setTicketAmount(batchCouponDto.getTicketAmount());
                    batchUser.setUpdateBy(batchCouponDto.getUpdateBy());
                    batchUser.setType(batchCouponDto.getRefundTicketType());
                    batchUser.setState(ReturnPremiumState.ReturnPremiumState_Three.getCode());//退费失败
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    batchUser.setTicketProvideTime(format.format(new Date()));
                    ReturnTicketLogDto returnTicketLogDto = new ReturnTicketLogDto();
                    BeanUtils.copyProperties(batchUser,returnTicketLogDto);
                    returnTicketLogDto.setState(1);//表示发券失败
                    logList.add(returnTicketLogDto);
                    logger.info("更新退费标记 对象信息（4）：" + JSON.toJSONString(batchUser));
                    Integer rows = returnPremiumMapper.updatePremiumBypremiumId(batchUser);
                    if(rows == 1){
                        logger.info("唯一标识（4）：" + batchUser.getPremiumId() + " 更新成功！！");
                    } else {
                        logger.info("唯一标识（4）：" + batchUser.getPremiumId() + " 更新失败！！");
                    }
                }
                logger.info("退费记录表更新 入参（4）：" + logList);
                Integer logRows = returnPremiumMapper.insertRetuenTicketLog(logList);
                logger.info("新增t_return_ticket_log表记录条数（4）：" + logRows);
            }
        } catch (Exception e) {
            logger.error("批量发券出错~~~~", e);
            return response.error("服务器开小差了！");
        }
        return response;
    }

    /**
     * 修改备注
     * @param remarkDto
     * @return
     */
    @Override
    public ResponseData updateRemark(RemarkDto remarkDto) {
        ResponseData response = ResponseData.success();
        if(remarkDto == null){
            return response.error("remarkDto is null");
        }
        if(remarkDto.getPremiumId() == null){
            return response.error("premiumId is null");
        }
        try {
            Integer rows = returnPremiumMapper.updateRemark(remarkDto);
            if(rows == 1){
                logger.info("唯一标识为：" + remarkDto.getPremiumId() + " 的订单，备注更新成功！");
            } else {
                logger.info("唯一标识为：" + remarkDto.getPremiumId() + " 的订单，备注更新失败！");
                return response.error("唯一标识为：" + remarkDto.getPremiumId() + " 的订单，备注更新失败！");
            }
        } catch (Exception e){
            logger.info("备注更新出错！！",e);
            return response.error("服务器开小差了！");
        }
        return response;
    }
}
