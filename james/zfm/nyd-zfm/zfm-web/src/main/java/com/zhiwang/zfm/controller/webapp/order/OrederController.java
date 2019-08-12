package com.zhiwang.zfm.controller.webapp.order;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.vo.DeductInfoVO;
import com.nyd.order.model.vo.OrderProductInfoVO;
import com.nyd.order.model.vo.OrderlistVo;
import com.nyd.order.model.vo.SaveRefunRecordVo;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.enums.ContratNameEnum;
import com.nyd.user.model.vo.OrderCustInfoVO;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.nyd.zeus.model.PaymentRiskFlowVo;
import com.nyd.zeus.model.PaymentRiskRecordExtendVo;
import com.nyd.zeus.model.PaymentRiskRequestChangjie;
import com.nyd.zeus.model.PaymentRiskRequestCommon;
import com.nyd.zeus.model.PaymentRiskRequestXunlian;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/order")
@Api(description="订单")
public class OrederController {

	
    private static Logger LOGGER = LoggerFactory.getLogger(OrederController.class);

	
	@Autowired
	private OrderForZQServise orderForZQServise;
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@Autowired
	private PaymentRiskRecordService paymentRiskRecordService;
	
	
	@PostMapping("/getOrderList")
	@ApiOperation(value = "获取订单列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<OrderlistVo>> getOrderList(OrderlistVo vo) {
		PagedResponse<List<OrderlistVo>> pageResponse = new PagedResponse<List<OrderlistVo>>();
		pageResponse = orderForZQServise.getOrderList(vo);
		return pageResponse;
	}
	
	
	@PostMapping("/getOrderProduct")
	@ApiOperation(value = "根据订单编号获取产品信息")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<OrderProductInfoVO> getOrderProduct(String orderNo) {
		CommonResponse<OrderProductInfoVO> common = new CommonResponse<OrderProductInfoVO>();
		common = orderForZQServise.getOrderProduct(orderNo);
		OrderProductInfoVO vo = common.getData();
		String aa = userForZQServise.queryAssessmentAmount();
		vo.setAssessmentAmount(aa);
		common.setData(vo);
		return common;
	}
	
	@PostMapping("/getCustInfo")
	@ApiOperation(value = "根据订单编号获取客户信息")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<OrderCustInfoVO> getCustInfoByOrderNo(String orderNo) {
		CommonResponse<OrderCustInfoVO> common = new CommonResponse<OrderCustInfoVO>();
		com.nyd.user.model.common.CommonResponse<OrderCustInfoVO> result = userForZQServise.getCustInfoByOrderNo(orderNo);
		common.setCode(result.getCode());
		common.setSuccess(result.isSuccess());
		common.setData(result.getData());
		return common;
	}

	
	@PostMapping("/getDeductInfo")
	@ApiOperation(value = "获取扣款信息")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<DeductInfoVO> getDeductInfo(String orderNo) {
		PagedResponse<DeductInfoVO> pageResponse = new PagedResponse<DeductInfoVO>();
		pageResponse = orderForZQServise.getDeductInfo(orderNo);
		return pageResponse;
	}
	
	@PostMapping("/getRiskCost")
	@ApiOperation(value = "获取订单的风控扣款金额")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> getRiskCost(String orderNo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			JSONObject result = paymentRiskRecordService.queryOrderCostMoney(orderNo).getData();
			common.setData(result);
			common.setMsg("操作成功!");
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/payRisk")
	@ApiOperation(value = "风控扣款接口")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse payRisk(String bankNo,String idNo,double money,String orderNo,String channelCode,String name,String protocolId) {
		CommonResponse common = new CommonResponse();
		try {
			PaymentRiskRequestCommon request = new PaymentRiskRequestCommon();
			request.setChannelCode(channelCode);
			request.setMoney(new BigDecimal(money));
			request.setOrderNo(orderNo);
			request.setRiskTime(new Date());
			
			// 畅捷
			if(channelCode.equals("changjie")) {
				PaymentRiskRequestChangjie cj = new PaymentRiskRequestChangjie();
				cj.setBankNo(bankNo);
				cj.setIdNo(idNo);
				request.setChannelJson(JSONObject.toJSONString(cj));
			}
			// xunlian
			if(channelCode.equals("xunlian")) {
				PaymentRiskRequestXunlian xl = new PaymentRiskRequestXunlian();
				xl.setAccount(bankNo);
				xl.setName(name);
				xl.setProtocolId(protocolId);
				request.setChannelJson(JSONObject.toJSONString(xl));
			}	
			
			JSONObject result= JSONObject.parseObject(JSONObject.toJSONString(paymentRiskRecordService.activeRepayment(request)));
			common.setMsg("操作成功!");
			common.setData(result);
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/payRiskExtend")
	@ApiOperation(value = "风控扣款接口")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse payRiskExtend(@RequestBody PaymentRiskRecordExtendVo vo) {
		CommonResponse common = new CommonResponse();
		try {
			JSONObject result= JSONObject.parseObject(JSONObject.toJSONString(paymentRiskRecordService.saveRepaymentExtend(vo)));
			common.setMsg("操作成功!");
			common.setData(result);
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/updateRisk")
	@ApiOperation(value = "风控扣款更新接口")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse updateRisk(String orderNo,int riskStatus) {
		CommonResponse common = new CommonResponse();
		try {
			PaymentRiskRequestCommon request = new PaymentRiskRequestCommon();
			request.setOrderNo(orderNo);
			request.setRiskStatus(riskStatus);
			request.setRiskTime(new Date());
			JSONObject result= JSONObject.parseObject(JSONObject.toJSONString(paymentRiskRecordService.updatePaymentRisk(request)));
			common.setMsg("操作成功!");
			common.setData(result);
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/riskAllCost")
	@ApiOperation(value = "风控扣款总扣款金额")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse riskAllCost() {
		CommonResponse common = new CommonResponse();
		try {
			JSONObject result= JSONObject.parseObject(JSONObject.toJSONString(paymentRiskRecordService.allCostMoney()));
			common.setMsg("操作成功!");
			common.setData(result);
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/riskSuccessFlow")
	@ApiOperation(value = "风控扣款流水")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<JSONObject> riskSuccessFlow(String orderNo) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			JSONObject resultJson = new JSONObject();
			List<PaymentRiskFlowVo> result= paymentRiskRecordService.successFlow(orderNo).getData();
			JSONObject money = paymentRiskRecordService.queryOrderCostMoney(orderNo).getData();
			resultJson.put("money", new DecimalFormat("0.00").format(money.getDouble("costMoney")));
			resultJson.put("list", result);
			common.setMsg("操作成功!");
			common.setData(resultJson);
			common.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			common.setMsg("操作失败!");
			common.setSuccess(false);
		}
		return common;
	}
	@PostMapping("/saveRefundRecord")
	@ApiOperation(value = "新增退款记录")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<Object> saveRefundRecord(SaveRefunRecordVo vo) {
		CommonResponse<Object> common = new CommonResponse<Object>();
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		vo.setSysUserId(userVO.getId());
		vo.setSysUserName(userVO.getLoginName());
		common = orderForZQServise.saveRefundRecord(vo);
		return common;
	}
	
	
	@GetMapping("/viewContract")
	public void viewContract(HttpServletRequest reqeust,HttpServletResponse response,String url,String code) {
		try {
			
			//输出字符转换编码
			response.setContentType("text/html;charset=UTF-8");
			//在线预览
			String contratName = ContratNameEnum.fromDesc(code);
			downLoad(url,contratName,response,true);
		} catch (Exception e) {
			LOGGER.error("合同在线查看异常，参数 :url{}", new Object[]{ url });
			LOGGER.error(e.getMessage(), e);
//			throw new BusinessException();
		}
	}
	

	private void downLoad(String filePath,String fileName, HttpServletResponse response, boolean isOnLine) throws Exception {
		
		URL url = new URL(filePath);
		URLConnection conn = url.openConnection();
		// 合同中心token
//		JSONObject resultJson = BusiCtrlUtil.contractCenterToken();
//		conn.setRequestProperty("Authorization", resultJson.getString("data"));
	    InputStream inStream = conn.getInputStream();
        BufferedInputStream br = new BufferedInputStream(inStream);
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        
        if (isOnLine) { // 在线打开方式
        		URL u = new URL(filePath); 
            response.setContentType(u.openConnection().getContentType());
            response.setContentType("application/pdf");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "inline; filename=" +new String(fileName.getBytes(),"ISO8859-1")+".pdf");
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/octet-stream");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO8859-1")+".pdf");
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
        out.write(buf, 0, len);
        br.close();
        out.close();
    }
}
