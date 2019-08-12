package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.CustomerRefundDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.RefundMapper;
import com.nyd.admin.entity.RefundAuditLog;
import com.nyd.admin.model.dto.CustomerRefundDto;
import com.nyd.admin.model.dto.RefundApplyDto;
import com.nyd.admin.service.CustomerRefundService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.user.api.RefundApplyApi;
import com.nyd.user.model.RefundApplyInfo;
import com.nyd.user.model.enums.RefundApplyCode;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/11/2 18:35
 */
@Service
public class CustomerRefundServiceImpl implements CustomerRefundService {

    private static Logger logger = LoggerFactory.getLogger(CustomerRefundServiceImpl.class);

    @Autowired
    RefundMapper refundMapper;

    @Autowired
    private RefundApplyApi refundApplyApi;
    @Autowired
    private CustomerRefundDao customerRefundDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData customerOperational(CustomerRefundDto customerRefundDto) {
        ResponseData responseData = ResponseData.success();
        try {
            if (customerRefundDto == null || customerRefundDto.getRequestNo() == null) {
                return responseData.error("参数错误！");
            }
            if (customerRefundDto.getRefundAmount() == null) {
                customerRefundDto.setRefundAmount(new BigDecimal(0));
            }
            if (customerRefundDto.getRemark() == null) {
                customerRefundDto.setRemark("");
            }
            if (customerRefundDto.getFlag() == 1 && customerRefundDto.getAuditFlag() == 1) {
                if (customerRefundDto.getRefundAmount().compareTo(new BigDecimal(0)) == -1) {
                    return responseData.error("输入的金额有误，请重新输入！");
                }
            }
            //表示客服操作
            if (customerRefundDto.getFlag() == 1) {
                if (customerRefundDto.getAuditFlag() == 1) { //客服审核通过
                    RefundApplyInfo refundApplyInfo = new RefundApplyInfo();
                    refundApplyInfo.setRequestNo(customerRefundDto.getRequestNo());
                    refundApplyInfo.setRefundAmount(customerRefundDto.getRefundAmount());
                    refundApplyInfo.setUpdateBy(customerRefundDto.getOperatorPerson());
                    refundApplyInfo.setStatus(30);//表示客服审核通过，也就是财务待审
                    customerRefundDto.setStatus(30);
                    refundApplyInfo.setMessage(customerRefundDto.getRemark());
                    try {
                        responseData = refundApplyApi.update(refundApplyInfo);
                    } catch (Exception e) {
                        logger.info("审核操作调用user服务接口出错！");
                    }
                    logger.info("更改审核状态操作结果：" + JSON.toJSONString(responseData));
                    if (responseData != null && "0".equals(responseData.getStatus())) {
                        saveRefundAuditLog(customerRefundDto);
                    }
                } else if (customerRefundDto.getAuditFlag() == 2) { //拒绝
                    RefundApplyInfo refundApplyInfo = new RefundApplyInfo();
                    refundApplyInfo.setRequestNo(customerRefundDto.getRequestNo());
                    refundApplyInfo.setUpdateBy(customerRefundDto.getOperatorPerson());
                    refundApplyInfo.setStatus(20);//表示客服审核拒绝
                    customerRefundDto.setStatus(20);
                    refundApplyInfo.setMessage(customerRefundDto.getRemark());
                    try {
                        responseData = refundApplyApi.update(refundApplyInfo);
                    } catch (Exception e) {
                        logger.info("审核操作调用user服务接口出错！");
                    }
                    logger.info("更改审核状态操作结果：" + JSON.toJSONString(responseData));
                    if (responseData != null && "0".equals(responseData.getStatus())) {
                        saveRefundAuditLog(customerRefundDto);
                    }
                }
            }
            //表示财务操作
            if (customerRefundDto.getFlag() == 2) {
                if (customerRefundDto.getAuditFlag() == 1){ //财务通过
                    RefundApplyInfo refundApplyInfo = new RefundApplyInfo();
                    refundApplyInfo.setRequestNo(customerRefundDto.getRequestNo());
                    refundApplyInfo.setUpdateBy(customerRefundDto.getOperatorPerson());
                    refundApplyInfo.setStatus(50);//表示财务审核通过
                    customerRefundDto.setStatus(50);
                    try {
                        responseData = refundApplyApi.update(refundApplyInfo);
                    } catch (Exception e) {
                        logger.info("审核操作调用user服务接口出错！");
                    }
                    logger.info("更改审核状态操作结果：" + JSON.toJSONString(responseData));
                    if (responseData != null && "0".equals(responseData.getStatus())) {
                        saveRefundAuditLog(customerRefundDto);
                    }
                } else if (customerRefundDto.getAuditFlag() == 2) { //拒绝
                    RefundApplyInfo refundApplyInfo = new RefundApplyInfo();
                    refundApplyInfo.setRequestNo(customerRefundDto.getRequestNo());
                    refundApplyInfo.setUpdateBy(customerRefundDto.getOperatorPerson());
                    refundApplyInfo.setStatus(1000);//表示财务审核拒绝
                    customerRefundDto.setStatus(1000);
                    refundApplyInfo.setMessage(customerRefundDto.getRemark());
                    try {
                        responseData = refundApplyApi.update(refundApplyInfo);
                    } catch (Exception e) {
                        logger.info("审核操作调用user服务接口出错");
                    }
                    logger.info("更改审核状态操作结果：" + JSON.toJSONString(responseData));
                    if (responseData != null && "0".equals(responseData.getStatus())) {
                        saveRefundAuditLog(customerRefundDto);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("操作出错啦！",e);
            return responseData.error("服务器开小差，请稍后重试！");
        }
        return responseData;
    }

    /**
     * 日志保存
     * @param customerRefundDto
     */
    private void saveRefundAuditLog(CustomerRefundDto customerRefundDto) {
        RefundAuditLog refundAuditLog = new RefundAuditLog();
        BeanUtils.copyProperties(customerRefundDto,refundAuditLog);
        refundAuditLog.setCreateTime(new Date());
        refundAuditLog.setUpdateTime(new Date());
        refundAuditLog.setUpdateBy("sys");
        logger.info("日志数据为" + refundAuditLog);
        try {
            customerRefundDao.save(refundAuditLog);
        } catch (Exception e) {
            logger.info("日志插入出错");
        }
    }

    @Override
    public ResponseData getRefundList(RefundApplyDto refundApplyInfo) {
        ResponseData responseData = ResponseData.success();
        refundApplyInfo.setTypeCode(RefundApplyCode.REFUND.getCode());
        try {
            Integer pageNum = refundApplyInfo.getPageNum();
            Integer pageSize = refundApplyInfo.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 5;
            }
            //进行查询个数
            Integer total = refundMapper.findCount(refundApplyInfo);
            logger.info("授信查询总个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            //查询详情
            PageHelper.startPage(pageNum, pageSize);
            List<RefundApplyDto> creditDetailResult = refundMapper.getRefundApplyList(refundApplyInfo);
            PageInfo pageInfo = new PageInfo(creditDetailResult);
            pageInfo.setTotal(total);
            return responseData.setData(pageInfo);
        }catch(Exception e) {
            logger.error("查询列表异常");
            return ResponseData.error("查询退款记录异常！");
        }
    }

	@Override
	public ResponseData getRefundDetail(RefundApplyDto refundApplyInfo) {
		if(refundApplyInfo.getRequestNo() == null) {
			return ResponseData.error("请求参数异常！");
		}
		
		List<RefundApplyDto> list = null;
		try {
			list = refundMapper.findRefundDetails(refundApplyInfo);
		}catch(Exception ex) {
			logger.error("查询列表异常");
			return ResponseData.error("查询退款记录异常！");
		}
		return ResponseData.success(list);
		/*ResponseData<List<RefundApplyInfo>> infoList = refundApplyApi.getRefundApplyList(refundApplyInfo);
		if(infoList == null || infoList.getData() == null || infoList.getData().size() == 0) {
			return ResponseData.error("没有查询到记录");
		}
		RefundApplyInfo info = infoList.getData().get(0);*/
	}

    @Override
    public ResponseData financeQueryRefundList(RefundApplyDto refundApplyDto) {
        ResponseData responseData = ResponseData.success();
        try {
            if (refundApplyDto == null) {
                return responseData.error("参数错误！");
            }
            Integer pageNum = refundApplyDto.getPageNum();
            Integer pageSize = refundApplyDto.getPageSize();
            //设置默认分页条件
            if (pageNum == null || pageNum == 0) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 5;
            }
            Integer total = refundMapper.financeQueryRefundCount(refundApplyDto);
            logger.info("客服查询总个数 total is " + total);
            if (total == null) {
                total = 0;
            }
            PageHelper.startPage(pageNum, pageSize);
            List<RefundApplyDto> list = refundMapper.financeQueryRefundDetails(refundApplyDto);
            PageInfo pageInfo = new PageInfo(list);
            pageInfo.setTotal(total);
            responseData.setData(pageInfo);
        } catch (Exception e) {
            logger.error("财务查询列表出错！");
            return responseData.error("服务器开小差，请稍后重试！");
        }
        return responseData;
    }
}
