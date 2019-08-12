package com.zhiwang.zfm.controller.webapp.ordercheck;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.zzl.OrderForZLQServise;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderCheckVo;
import com.nyd.order.model.OrderRecordHisVo;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.vo.ExamReq;
import com.nyd.user.api.zzl.UserForZLQServise;
import com.nyd.user.entity.Contact;
import com.nyd.user.model.common.CommonResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;
import com.zhiwang.zfm.service.api.sys.SqlService;
import com.nyd.user.model.ordercheck.ContactsInfoVo;
import com.nyd.user.model.ordercheck.UserNoParam;
import com.nyd.user.model.vo.sms.SmsRecordingVo;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.api.zzl.ZeusForZLQServise;
import com.nyd.zeus.model.helibao.PaymentVo;
import com.nyd.zeus.model.helibao.vo.pay.req.BankCardbindVo;
import com.nyd.zeus.model.helibao.vo.pay.resp.BankCardbindResponseVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order")
@Api(description = "订单审核")
public class OrderCheckController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCheckController.class);
	
	@Autowired
    private OrderForZLQServise orderForZLQServise;
	@Autowired
    private UserForZLQServise userForZLQServise;
	@Autowired
	private ZeusForZLQServise zeusForZLQServise;
	
	@Autowired
	private ZeusForWHServise zeusForWHServise;
	
	@Autowired
	private SqlService sqlService;

    @SuppressWarnings("unchecked")
	@PostMapping("/orderCheckList")
	@ApiOperation(value = "订单审批列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public com.nyd.order.model.common.PagedResponse<List<OrderCheckVo>> orderList(@RequestBody OrderCheckQuery orderCheckQuery) throws IOException {
		// 设置返回类型
    	UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		com.nyd.order.model.common.PagedResponse<List<com.nyd.order.model.OrderCheckVo>> result = new PagedResponse<>();
		com.nyd.order.model.common.PagedResponse<List<com.nyd.order.model.OrderCheckVo>> res = new PagedResponse<>();
		try {
			if(StringUtils.isNotBlank(orderCheckQuery.getBeginTime())){
				orderCheckQuery.setBeginTime(orderCheckQuery.getBeginTime()+" 00:00:00");
			}
			if(StringUtils.isNotBlank(orderCheckQuery.getEndTime())) {
				orderCheckQuery.setEndTime(orderCheckQuery.getEndTime()+" 23:59:59");
			}
			String sql = "select role_id from sys_user_role where user_id = '%s'";
			//String roleSql = "select su.id from sys_user su INNER JOIN (select USER_ID from sys_user_role sur where sur.ROLE_ID in ('1','5')) rol on su.ID=rol.USER_ID";
			List<JSONObject> userList = sqlService.query(String.format(sql, userVO.getId()));
			String loanId = userVO.getId();
			Boolean flag = false;
			for(JSONObject j:userList){
				String roleUserId = j.getString("role_id");
				if("1".equals(roleUserId) || "4".equals(roleUserId)){
					flag = true;
					break;
				}
			}
			result = orderForZLQServise.findByParam(orderCheckQuery);
			if(flag){
			    return result;
			}else{
				List<com.nyd.order.model.OrderCheckVo> resList = new ArrayList<OrderCheckVo>();
				List<com.nyd.order.model.OrderCheckVo> respList = result.getData();
				if(null != respList && respList.size()>0){
					for(com.nyd.order.model.OrderCheckVo vo:respList){
						if(loanId.equals(vo.getAssignId())){
							resList.add(vo);
						}
					}
				}
				res.setData(resList);
				res.setTotal(result.getTotal());
				res.setSuccess(true);
				return res;
			}
			
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(e.getMessage(), e);
			//logger.error("查询用户异常,请求参数:{}",orderCheckVo);
			res=(com.nyd.order.model.common.PagedResponse<List<com.nyd.order.model.OrderCheckVo>>) com.nyd.order.model.common.PagedResponse.error(StatusConstants.SYS_ERROR_MSG);
			res.setSuccess(false);
			res.setCode("0");
			res.setMsg("失败");
		}
		return res;
	}
	
    //联系人信息
    @SuppressWarnings("unchecked")
   	@PostMapping("/contactsInfo")
   	@ApiOperation(value = "联系人信息")
   	@Produces(value = MediaType.APPLICATION_JSON)
   	public CommonResponse<List<ContactsInfoVo>> contactsInfoList(@RequestBody UserNoParam userNoParam) throws IOException {
   		// 设置返回类型
    	CommonResponse<List<ContactsInfoVo>> result = new CommonResponse<List<ContactsInfoVo>>();
   		try {
   			//List<Contact>  list= userForZLQServise.findByUserId(userNoParam.getUserid());
   			List<ContactsInfoVo> respList = new ArrayList<ContactsInfoVo>();
   			
        	List<Contact> Contact= userForZLQServise.findContactByUserId(userNoParam.getUserId());
        	if(Contact!=null &&Contact.size()>0) {
        		
        		for(int i=0;i<Contact.size();i++) {
        			ContactsInfoVo resp=new ContactsInfoVo();
        			resp.setUserid(Contact.get(i).getUserId());
                	resp.setContactPhone(Contact.get(i).getMobile());
                	resp.setContactName(Contact.get(i).getName());
                	resp.setContactRelation(Contact.get(i).getRelationship());
                	if("direct".equals(Contact.get(i).getType())) {
                		resp.setContactRelation(Contact.get(i).getRelationship()+"(直系联系人)");
                	}
                	if("major".equals(Contact.get(i).getType())) {
                		resp.setContactRelation(Contact.get(i).getRelationship()+"(重要联系人)");
                	}
                	respList.add(resp);
        		}
        		
        	}
        	
        	result.setData(respList);
        	result.setSuccess(true);
        	result.setCode("1");
        	result.setMsg("成功");
        	
    	
   		} catch (Exception e) {
   			e.printStackTrace();
   			result=(CommonResponse<List<ContactsInfoVo>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
   			result.setSuccess(false);
        	result.setCode("0");
        	result.setMsg("失败");
   		}
   		return result;
   	}
    
    //历史申请记录
    @SuppressWarnings("unchecked")
   	@PostMapping("/orderRecordHis")
   	@ApiOperation(value = "历史申请记录")
   	@Produces(value = MediaType.APPLICATION_JSON)

  public CommonResponse<List<OrderRecordHisVo>> contactsInfo(@RequestBody com.nyd.order.model.UserNoParam userParam) throws Throwable {
    	CommonResponse<List<OrderRecordHisVo>> responseData = new CommonResponse<List<OrderRecordHisVo>>();
    	
    	try {
        	List<OrderRecordHisVo> recordHis=orderForZLQServise.findHisByUserId(userParam);
        	if(recordHis!=null && recordHis.size()>0) {
        		for(int i=0;i<recordHis.size();i++) {
        			if(recordHis.get(i).getApprovingOfficer()==null ||recordHis.get(i).getApprovingOfficer().equals("")) {
        				recordHis.get(i).setApprovingOfficer("--");
        			}
        			if(recordHis.get(i).getApplicationOpinion()==null ||recordHis.get(i).getApplicationOpinion().equals("")) {
        				recordHis.get(i).setApplicationOpinion("--");
        			}
        			recordHis.get(i).setApplyResult(OrderStatus.getDescription(Integer.parseInt(recordHis.get(i).getApplyResult())));
        			com.nyd.zeus.model.OrderRecordHisVo  orderRecors= zeusForZLQServise.findHisByOrder(recordHis.get(i).getOrderNo());
        			
        			if(orderRecors!=null ) {
        				String orderStatus = orderRecors.getApplyResult();
        				if(StringUtils.isNotBlank(orderStatus)){
        					int status = Integer.parseInt(orderStatus);
        					recordHis.get(i).setApplyResult(OrderStatus.getDescription(status));
        				}
        				
        				recordHis.get(i).setOverdueDays(orderRecors.getOverdueDays());
            			recordHis.get(i).setEndTime(orderRecors.getEndTime());
            			recordHis.get(i).setActualRepayTime(orderRecors.getActualRepayTime());
        			}else {
        				recordHis.get(i).setOverdueDays("--");
            			recordHis.get(i).setEndTime("--");
            			recordHis.get(i).setActualRepayTime("--");
        			}
        			
            	}
        	}
        	responseData.setCode("1");
        	responseData.setData(recordHis);
        	responseData.setMsg("历史申请记录查询成功！");
        	responseData.setSuccess(true);
   		} catch (Exception e) {
   			e.printStackTrace();
   			responseData=(CommonResponse<List<OrderRecordHisVo>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
   			responseData.setCode("0");
   			responseData.setMsg("失败");
   			responseData.setSuccess(false);
   		}
     return responseData;
 }
    
    @ApiOperation(value = "审核信息")
	@RequestMapping(value = "/examineInfo", method = RequestMethod.POST)
    public com.nyd.order.model.common.CommonResponse examineInfo(@RequestBody ExamReq req) throws Throwable{
    	LOGGER.info("审核信息请求开始入参：" + JSON.toJSONString(req));
    	
    	UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
    	req.setLoanUserId(userVO.getId());
    	req.setLoanUserName(userVO.getLoginName());
    	com.nyd.order.model.common.CommonResponse comm = orderForZLQServise.checkApproval(req);
        return comm;
    }
    
    @ApiOperation(value = "产品信息")
  	@RequestMapping(value = "/queryProduct", method = RequestMethod.POST)
      public com.nyd.order.model.common.CommonResponse<List<ExamReq>> queryProduct(@RequestBody ExamReq req) throws Throwable{
      	LOGGER.info("产品信息开始入参：" + JSON.toJSONString(req));
      	
      	return orderForZLQServise.queryProduct(req);
      
      }
	
    
    @PostMapping("/manageRepayment")
	@ApiOperation(value = "用户管理系统代扣")
	@Produces(value = MediaType.APPLICATION_JSON)
	public com.nyd.zeus.model.common.CommonResponse manageRepayment(@ModelAttribute PaymentVo paymentVo){
		return zeusForWHServise.manageRepayment(paymentVo);
	}
    
}
