package com.zhiwang.zfm.controller.webapp.refund;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.zzl.OrderForGYTServise;
import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.refund.request.RefundListRequest;
import com.nyd.order.model.refund.vo.RefundFlowVo;
import com.nyd.order.model.refund.vo.RefundListVo;
import com.nyd.order.model.refund.vo.RefundRatioVo;
import com.nyd.order.model.refund.vo.SumAmountVo;
import com.nyd.order.model.vo.ConfirmRefunVo;
import com.nyd.user.api.zzl.UserForZQServise;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.config.shiro.ShiroUtils;

@RestController
@RequestMapping("/refund")
@Api(description = "退款处理")
public class RefundController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RefundController.class);
	
	@Autowired
	private OrderForGYTServise orderForGYTServise;
	
	@Autowired
	private OrderForZQServise orderForZQServise;
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@ApiOperation(value = "退款列表查询")
	@RequestMapping(value = "/refundList", method = RequestMethod.POST)
    public PagedResponse<List<RefundListVo>> refundList(@RequestBody RefundListRequest request) throws Throwable{
    	LOGGER.info("退款列表查询请求开始入参：" + JSON.toJSONString(request));
    	PagedResponse<List<RefundListVo>> response = orderForGYTServise.refundList(request);
        return response;
    }
	
	@ApiOperation(value = "退款比例")
	@RequestMapping(value = "/refundRatio", method = RequestMethod.POST)
    public CommonResponse<RefundRatioVo> refundRatio() throws Throwable{
    	LOGGER.info("退款处理--退款比列查询开始");
    	CommonResponse<RefundRatioVo> response = orderForGYTServise.refundRatio();
        return response;
    }
	
	@ApiOperation(value = "退款处理记录")
	@RequestMapping(value = "/refundListRecord", method = RequestMethod.POST)
    public CommonResponse<List<RefundFlowVo>> refundListRecord(String orderNo) throws Throwable{
    	LOGGER.info("退款列表查询请求开始入参：" + JSON.toJSONString(orderNo));
    	CommonResponse<List<RefundFlowVo>> response = orderForGYTServise.queryRefundFlow(orderNo);
        return response;
    }
	
	@ApiOperation(value = "确认退款")
	@RequestMapping(value = "/refundMoney", method = RequestMethod.POST)
    public CommonResponse<Object> refundMoney(ConfirmRefunVo vo)  throws Throwable{
    	LOGGER.info("确认退款请求开始入参：" + JSON.toJSONString(vo));
    	CommonResponse<Object> response = new CommonResponse<Object>();
    	try {
    		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
    		vo.setSysUserId(userVO.getId());
    		vo.setSysUserName(userVO.getLoginName());
			if(ChkUtil.isEmpty(vo.getUserId()) || ChkUtil.isEmpty(vo.getRefundId())
					|| ChkUtil.isEmpty(vo.getIsResult())){
				response.setData(null);
				response.setCode("0");
				response.setMsg("必填参数缺失！");
				response.setSuccess(false);
				return response;
			}
        	response = orderForZQServise.refundMoney(vo);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("确认退款系统错误：{}",e,e.getMessage());
			LOGGER.error("确认退款系统错误入参：{}",JSON.toJSONString(vo));
		}
        return response;
    }
	
	@ApiOperation(value = "退款处理记录金额合计查询")
	@RequestMapping(value = "/sumAmount", method = RequestMethod.POST)
    public CommonResponse<SumAmountVo> sumAmount(String orderNo) throws Throwable{
    	LOGGER.info("退款处理记录金额合计查询开始入参，orderNo：" + orderNo);
    	CommonResponse<SumAmountVo> response = orderForGYTServise.sumAmount(orderNo);
        return response;
    }
}
