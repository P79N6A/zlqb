package com.nyd.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.application.api.QiniuContract;
import com.nyd.user.api.RefundContract;
import com.nyd.user.dao.mapper.RefundAppMapper;
import com.nyd.user.dao.mapper.RefundMapper;
import com.nyd.user.model.RefundAppCountInfo;
import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.RefundOrderInfo;
import com.nyd.user.model.vo.RefundAppVo;
import com.nyd.user.service.RefundAppCountService;
import com.nyd.user.service.RefundOrderService;
import com.nyd.user.service.RefundService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.MongoApi;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

/**
 * 
 * @author zhangdk
 *
 */
@Service("refundContract")
public class RefundServiceImpl implements RefundService,RefundContract {
    private static Logger logger = LoggerFactory.getLogger(RefundServiceImpl.class);
    
	public static String KEY =	"nyd:refunduser";
	
	@Autowired
	RefundMapper refundMapper;
	@Autowired
	RefundAppMapper refundAppMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private RedisTemplate redisTemplate;
	@Autowired
	private UserProperties  userProperties;
	@Autowired
	private RestTemplateApi restTemplateApi;
    
    @Autowired
    private RefundAppCountService refundAppCountService;
    
	 @Autowired
	 QiniuContract qiniuContract;
	 
	 @Autowired
	 RefundOrderService refundOrderService;
	
	@Override
    public void save(RefundInfo refund) throws Exception{
		refund.setRefundNo(generaterCode());
		refund.setRequestStatus(999);
		refundMapper.save(refund);
    }
	@Override
    public void update(RefundInfo refund) throws Exception{
		RefundInfo res = refundMapper.getRefundByRefundNo(refund.getRefundNo());
		//审核通过更新统计注册记录&&生成退款订单流水
		if(refund.getRequestStatus().equals(1001)) {
			RefundOrderInfo refu = new RefundOrderInfo();
			BeanUtils.copyProperties(refu, res);
			RefundInfo  info = refundMapper.getRefundByRefundNo(refund.getRefundNo());
			try {
				//审核通过生成退款订单流水
				refundOrderService.save(refu);
			}catch(Exception e) {
				logger.error("插入借款订单流水异常：" + e.getMessage());
			}
			try {
				//停止代扣
				stopWithhold(info);
			}catch(Exception e) {
				logger.error("stop withhold excption : " + e.getMessage());
			}
			String appList = info.getAppList();
			if(!StringUtils.isBlank(appList)) {
				for(String code:appList.split(";")) {
					RefundAppCountInfo count = new RefundAppCountInfo();
					count.setAppCode(code);
					count.setCountDate(new Date());
					count.setUpdateRegisterCount(1);
					try {
						refundAppCountService.updateCount(count);
					}catch(Exception e) {
						logger.info("更新统计记录异常：" + e.getMessage());
						continue;
					}
				}
			}
			try {
				refundOrderService.sumbitRefundList(refund);
			}catch(Exception e) {
				logger.error("退款订单推送公共服务异常",e);
			}
			
		}
		if(refund.getRequestStatus().equals(1002)) {
			List<RefundAppVo> imge = mongoApi.getRefundImge(refund.getRefundNo());
			if(imge != null && imge.size() > 0) {
				List<RefundAppInfo> appList = imge.get(0).getAppList();
				for(int i =0;i<appList.size();i++) {
					for(RefundAppInfo reason:refund.getAppReasonList()) {
						if(appList.get(i).getAppCode().equals(reason.getAppCode())) {
							appList.get(i).setReason(reason.getReason());
						}
					}
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("refundNo", refund.getRefundNo());
				map.put("appList", appList);
				try {
					mongoApi.updateRefundImge(map);
				}catch(Exception e) {
					logger.error("更新拒绝原因失败：" + e.getMessage());
				}
			}
		}
		refundMapper.update(refund);
	}
	private void stopWithhold(RefundInfo refund) {
		try {
			//停止代扣
			Map<String, Object> param = new HashMap<String, Object>();
			String payOrderNo =(String)redisTemplate.opsForValue().get(KEY+refund.getAccountNumber());
			param.put("payOrderNo", payOrderNo);
			String url = "http://" + userProperties.getCommonPayIp() + ":" + userProperties.getCommonPayPort() + "/common/pay/stopPayStateByNo";
			logger.info("停止代扣传入参数{},url{}",JSON.toJSONString(param),url);	
			ResponseData resp = restTemplateApi.postForObject(url, param,ResponseData.class);
			logger.info("响应信息:" + JSON.toJSONString(resp));
			if("0".equals(resp.getStatus())) {
				logger.info("停止代扣成功"+refund.getAccountNumber());
				redisTemplate.delete(KEY+refund.getAccountNumber());
			}else {
				logger.info("停止代扣失败"+refund.getAccountNumber());
			}
		} catch (Exception e) {
			logger.info("save refunduser error",e);
			throw e;
		}	
	}
	@Override
    public ResponseData<List<RefundInfo>> queryRefund(RefundInfo info) throws Exception{
		ResponseData responseData = ResponseData.success();
		Integer pageNum = info.getPageNum();
        Integer pageSize = info.getPageSize();
        //设置默认分页条件
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Integer total = 0;
        try {
			total = refundMapper.queryRefundTotal(info);
		} catch (Exception e) {
			logger.error("查询退款列表个数异常：" + e.getMessage());
		}
        logger.info("查询总个数 total is " + total);
        if (total == null) {
            total = 0;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<RefundInfo> list = null;
        try {
            list = refundMapper.queryRefund(info);
        } catch (Exception e) {
        	logger.error("查询退款列表失败：" + e.getMessage());
            responseData = ResponseData.error("查询退款列表失败：" + e.getMessage());
            return responseData;
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setTotal(total);
        responseData.setData(pageInfo);
        return responseData;
	}
	@Override
	public ResponseData<RefundInfo> getRefundByRefundNo(String refundNo) throws Exception{
		ResponseData responseData = ResponseData.success();
		RefundInfo res = null;
		try {
			res = refundMapper.getRefundByRefundNo(refundNo);
			if (res!=null) {
				responseData.setData(res);
			}
		} catch (Exception e) {
			logger.error("查询退款详情失败：" + e.getMessage());
			responseData = ResponseData.error("查询退款详情失败！" );
			return responseData;
		}
		return responseData;
	}
	@Override
	public ResponseData<List<RefundInfo>> getRefundByUserId(String userId) throws Exception{
		ResponseData responseData = ResponseData.success();
		List<RefundInfo> res = null;
		try {
			res = refundMapper.getRefundByUserId(userId);
			if (res!=null) {
				responseData.setData(res);
			}
		} catch (Exception e) {
			logger.error("查询退款详情失败：" + e.getMessage());
			responseData = ResponseData.error("查询退款详情失败！" );
			return responseData;
		}
		return responseData;
	}
	@Override
	public ResponseData<List<RefundAppInfo>> getRefundAppListByRefundNo(String refundNo) throws Exception{
		ResponseData responseData = ResponseData.success();
		try {
			List<RefundAppVo> imge = mongoApi.getRefundImge(refundNo);
			if(imge != null && imge.size()>0) {
				List<RefundAppInfo> appList = imge.get(0).getAppList();
				for(RefundAppInfo app : appList) {
					List<String> imgeKeys = app.getImgeList();
					List<String> imgeUrls = new ArrayList<String>();
					for(String imgeKey: imgeKeys) {
						ResponseData res = qiniuContract.download(imgeKey);
						if(res != null && res.getStatus().equals("0")) {
							imgeUrls.add((String)res.getData());
						}
					}
					app.setImgeList(imgeUrls);
					RefundAppInfo info = refundAppMapper.getRefundAppByAppCode(app.getAppCode());
					if(info != null) {
						app.setRefundAppName(info.getRefundAppName());
					}
				}
				responseData.setData(appList);
			}
			
		} catch (Exception e) {
			logger.error("查询退款详情失败：" + e.getMessage());
			responseData = ResponseData.error("查询退款详情失败！" );
			return responseData;
		}
		return responseData;
	}

	@Override
	public ResponseData<List<RefundAppInfo>> getRefundAppListByRefundNoAuth(String refundNo) throws Exception {
		ResponseData responseData = ResponseData.success();
		try {
			List<RefundAppVo> imge = mongoApi.getRefundImge(refundNo);
			if(imge != null && imge.size()>0) {
				List<RefundAppInfo> appList = imge.get(0).getAppList();
				for(RefundAppInfo app : appList) {
					List<String> imgeKeys = app.getImgeList();
					List<Map<String,String>> list = new ArrayList<>();
					for(String imgeKey: imgeKeys) {
						Map<String,String> imgeMap = new HashMap<>();
						ResponseData res = qiniuContract.download(imgeKey);
						if(res != null && res.getStatus().equals("0")) {
							imgeMap.put("key",imgeKey);
							imgeMap.put("url",(String)res.getData());
							list.add(imgeMap);
						}
					}
					app.setImgeMapList(list);
					RefundAppInfo info = refundAppMapper.getRefundAppByAppCode(app.getAppCode());
					if(info != null) {
						app.setRefundAppName(info.getRefundAppName());
					}
					app.setImgeList(null);
				}
				responseData.setData(appList);
			}

		} catch (Exception e) {
			logger.error("重新申请退款查询详情请异常：" , e);
			responseData = ResponseData.error("重新申请退款查询详情异常" );
			return responseData;
		}
		return responseData;
	}

	private String generaterCode() {
		try {
			return idGenerator.generatorId(BizCode.ORDER_NYD).toString();
		} catch (Exception e) {
			logger.error("生成借款订单号失败" + e.getMessage());
		}
		return System.currentTimeMillis()+"";
	}
	@Override
	public ResponseData uploadRefundImge(RefundAppVo vo) throws Exception {
		ResponseData res = ResponseData.success();
		logger.info("图片上传前端传入参数"+JSON.toJSONString(vo));
		if(StringUtils.isBlank(vo.getRefundNo()) || vo.getAppList() == null) {
			logger.error("参数为null");
			return res.error("参数不能为空");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("refundNo", vo.getRefundNo());
		map.put("appList", vo.getAppList());
		try {
			mongoApi.save(map, "attachment");
			RefundInfo refund = refundMapper.getRefundByRefundNo(vo.getRefundNo());
			if(refund != null) {
				refund.setAppName(vo.getAppName());
				refund.setRequestStatus(1000);
				refundMapper.update(refund);
			}
		}catch(Exception e) {
			logger.error("保存图片信息失败：" + e.getMessage());
			return res.error("保存图片信息失败");
		}
		return res.success();
	}
	

}
