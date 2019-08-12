package com.nyd.settlement.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.BillOrderMapper;
import com.nyd.settlement.entity.refund.QueryRefundEntity;
import com.nyd.settlement.entity.refund.Refund;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.dto.refund.AddRefundDto;
import com.nyd.settlement.model.dto.refund.DoRefundDto;
import com.nyd.settlement.model.po.refund.AlreadyRefundOrderPo;
import com.nyd.settlement.model.po.refund.RefundOrderPo;
import com.nyd.settlement.model.vo.refund.RefundOrderDetailVo;
import com.nyd.settlement.service.ReFundOperationService;
import com.nyd.settlement.service.ReFundService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.AlreadyRefundOrderStruct;
import com.nyd.settlement.service.struct.RefundOrderStruct;
import com.nyd.settlement.service.utils.CommonUtil;
import com.nyd.settlement.service.utils.ValidateUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hwei on 2018/1/15.
 */
@Service
public class ReFundServiceImpl implements ReFundService{
    private static Logger LOGGER = LoggerFactory.getLogger(ReFundServiceImpl.class);
    @Autowired
    BillOrderMapper billOrderMapper;
    @Autowired
    ReFundOperationService reFundOperationService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MYSQL)
    @Override
    public ResponseData queryNoReFund(QueryDto queryDto) {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!varifyQueryDto(queryDto)){
            return ResponseData.error("参数异常！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //查询需要退款记录
        try {
            QueryRefundEntity queryRefundEntity = new QueryRefundEntity();
            if (queryDto!=null) {
                if (StringUtils.isNotBlank(queryDto.getStartDate())) {
                    queryRefundEntity.setStartDate(sdf.parse(queryDto.getStartDate()+" 00:00:00"));
                }
                if (StringUtils.isNotBlank(queryDto.getEndDate())) {
                    queryRefundEntity.setEndDate(sdf.parse(queryDto.getEndDate()+" 23:59:59"));
                }
                if (StringUtils.isNotBlank(queryDto.getMobile())) {
                    queryRefundEntity.setMobile(queryDto.getMobile());
                }
                if (StringUtils.isNotBlank(queryDto.getOrderNo())) {
                    queryRefundEntity.setOrderNo(queryDto.getOrderNo());
                }
            }
            PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize(), queryDto.getOrderBy());
            List<RefundOrderPo> refundOrderPoList = billOrderMapper.getReFundOrderNo(queryRefundEntity);
            responseData.setData(RefundOrderStruct.INSTANCE.poPage2VoPage(new PageInfo(refundOrderPoList)));
            return responseData;
        } catch (ParseException e) {
            LOGGER.error("queryNoReFund has exception",e);
            return ResponseData.error("服务器开小差了！");
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public ResponseData queryNoReFundDetail(QueryDto queryDto) {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!varifyQueryDtoDetail(queryDto)) {
            return ResponseData.error("参数不能为空");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<RefundOrderDetailVo> refundOrderDetailVoList = billOrderMapper.getRefundDetail(queryDto);
            List<Map<String,Object>> list = new ArrayList<>();
            if (refundOrderDetailVoList!=null&&refundOrderDetailVoList.size()>0) {
                for (RefundOrderDetailVo refundOrderDetailVo:refundOrderDetailVoList) {
                    Map<String, Object> map = CommonUtil.transBeantoMap(refundOrderDetailVo);
                    map.put("orderNo",queryDto.getOrderNo());
                    map.put("realName",queryDto.getRealName());
                    map.put("mobile",queryDto.getMobile());
                    if(refundOrderDetailVo.getPromiseRepaymentDate()!=null) {
                        map.put("promiseRepaymentDate",sdf.format(refundOrderDetailVo.getPromiseRepaymentDate()));
                    }
                    if (refundOrderDetailVo.getActualSettleDate()!=null) {
                        map.put("actualSettleDate",sdf.format(refundOrderDetailVo.getActualSettleDate()));
                    }
                    list.add(map);
                }
            }
            responseData.setData(list);
        } catch (Exception e) {
            LOGGER.error("getRefundDetail has exception",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ORDER)
    @Override
    public ResponseData addReFund(AddRefundDto addRefundDto) {
        ResponseData responseData = ResponseData.success();
        try {
            //参数校验
            ValidateUtil.process(addRefundDto);
        } catch (Exception e) {
            LOGGER.error("参数不能为空！",e);
            return ResponseData.error("参数不能为空");
        }
        try {
            if (addRefundDto.getRefundAmount().compareTo(new BigDecimal(0))!=1){
                return ResponseData.error("退款金额不能为负数");
            }
            //检查订单，手机号是否存在
            Map<String,String> param = new HashMap<>();
            param.put("orderNo",addRefundDto.getOrderNo());
            param.put("mobile",addRefundDto.getMobile());
            String verifyOrder = billOrderMapper.queryOrderDetailByOrderNoAndMobile(param);
            if (StringUtils.isBlank(verifyOrder)) {
                return ResponseData.error("订单号或手机号不存在");
            }
            //保存退款记录
            Refund refund = new Refund();
            BeanUtils.copyProperties(addRefundDto,refund);
            refund.setRefundStatus(1);
            if (addRefundDto.getRefundType()==1) { //退会员费
                reFundOperationService.saveRefund(refund);
            } else {  //其他的做记录
                reFundOperationService.saveRefund(refund);
            }
            return responseData;
        } catch (Exception e) {
            LOGGER.error("保存退款信息异常",e);
            return ResponseData.error("服务器开小差了");
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ORDER)
    @Override
    public ResponseData doReFund(DoRefundDto doRefundDto) {
        ResponseData responseData = ResponseData.success();
        try {
            //参数校验
            ValidateUtil.process(doRefundDto);
        } catch (Exception e) {
            LOGGER.error("参数不能为空！",e);
            return ResponseData.error("参数不能为空");
        }
        try {
            if (doRefundDto.getRefundAmount().compareTo(new BigDecimal(0))!=1){
                return ResponseData.error("退款金额不能为负数");
            }
            //检查订单，手机号是否存在
            Map<String,String> param = new HashMap<>();
            param.put("orderNo",doRefundDto.getOrderNo());
            param.put("mobile",doRefundDto.getMobile());
            String verifyOrder = billOrderMapper.queryOrderDetailByOrderNoAndMobile(param);
            if (StringUtils.isBlank(verifyOrder)) {
                return ResponseData.error("订单号或手机号不存在");
            }
            //保存退款记录
            Refund refund = new Refund();
            BeanUtils.copyProperties(doRefundDto,refund);
            refund.setRefundStatus(1);
            if (doRefundDto.getRefundType()==1) { //退会员费
                return ResponseData.error("退款类型错误");
            } else {  //其他的做记录
//                reFundOperationService.saveRefund(refund);
//                //核销
//                try {
//                    reFundOperationService.updateBillRefundAmount(doRefundDto);
//                } catch (Exception e) {
//                    LOGGER.error("核销异常",e);
//                }
                operatDoRefund(refund,doRefundDto);
            }
            return responseData;
        } catch (Exception e) {
            LOGGER.error("退款异常",e);
            return ResponseData.error("服务器开小差了");
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MYSQL)
    @Override
    public ResponseData queryAlreadyReFund(QueryDto queryDto) {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!varifyQueryDto(queryDto)){
            return ResponseData.error("参数异常！");
        }
        //查询已退款记录
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            QueryRefundEntity queryRefundEntity = new QueryRefundEntity();
            if (queryDto!=null) {
                if (StringUtils.isNotBlank(queryDto.getStartDate())) {
                    queryRefundEntity.setStartDate(sdf.parse(queryDto.getStartDate()+" 00:00:00"));
                }
                if (StringUtils.isNotBlank(queryDto.getEndDate())) {
                    queryRefundEntity.setEndDate(sdf.parse(queryDto.getEndDate()+" 23:59:59"));
                }
                if (StringUtils.isNotBlank(queryDto.getMobile())) {
                    queryRefundEntity.setMobile(queryDto.getMobile());
                }
                if (StringUtils.isNotBlank(queryDto.getOrderNo())) {
                    queryRefundEntity.setOrderNo(queryDto.getOrderNo());
                }
            }
            PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize(), queryDto.getOrderBy());
            List<AlreadyRefundOrderPo> alreadyRefundOrderPoList = billOrderMapper.queryAlreadyReFund(queryRefundEntity);
            if (alreadyRefundOrderPoList!=null&&alreadyRefundOrderPoList.size()>0) {
                for (AlreadyRefundOrderPo alreadyRefundOrderPo: alreadyRefundOrderPoList) {
                    if (StringUtils.isBlank(alreadyRefundOrderPo.getOfflineRepayChannel())) {
                        alreadyRefundOrderPo.setRepayTime(alreadyRefundOrderPo.getActualSettleDate());
                    } else {
                        alreadyRefundOrderPo.setRepayTime(alreadyRefundOrderPo.getCreateTime());
                    }
                }
            }
            responseData.setData(AlreadyRefundOrderStruct.INSTANCE.poPage2VoPage(new PageInfo<>(alreadyRefundOrderPoList)));
        } catch (ParseException e) {
            LOGGER.error("queryAlreadyReFund has exception",e);
            return ResponseData.error("服务器开小差了！");
        }
        return responseData;
    }

    private boolean varifyQueryDto(QueryDto queryDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (queryDto==null) {
            return true;
        }
        if (queryDto!=null) {
            if (StringUtils.isNotBlank(queryDto.getMobile())) {
                String pattern = "^1[\\d]{10}";
                if (!Pattern.matches(pattern,queryDto.getMobile())) { //手机号不符合格式要求
                    return false;
                }
            }
            if (StringUtils.isNotBlank(queryDto.getStartDate())) {
                try {
                    sdf.parse(queryDto.getStartDate());
                } catch (ParseException e) {
                    LOGGER.error("日期格式化错误",e);
                    return false;
                }
            }
            if (StringUtils.isNotBlank(queryDto.getEndDate())) {
                try {
                    sdf.parse(queryDto.getEndDate());
                } catch (ParseException e) {
                    LOGGER.error("日期格式化错误",e);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean varifyQueryDtoDetail(QueryDto queryDto) {
        if (queryDto==null) {
            return false;
        }
        if (queryDto!=null) {
            if (StringUtils.isBlank(queryDto.getRealName())) {
                return false;
            }
            if (StringUtils.isBlank(queryDto.getMobile())) {
                return false;
            }
            if (StringUtils.isBlank(queryDto.getOrderNo())) {
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackFor=Exception.class)
    void operatDoRefund( Refund refund,DoRefundDto doRefundDto) throws Exception{
        try {
            reFundOperationService.saveRefund(refund);
        } catch (Exception e) {
            LOGGER.error("保存退款记录异常",e);
            throw e;
        }
        //核销
        try {
            reFundOperationService.updateBillRefundAmount(doRefundDto);
        } catch (Exception e) {
            LOGGER.error("退款核销异常",e);
            throw e;
        }
    }

}
