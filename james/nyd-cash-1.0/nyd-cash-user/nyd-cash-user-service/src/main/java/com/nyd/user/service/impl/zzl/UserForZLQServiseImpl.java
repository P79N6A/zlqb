package com.nyd.user.service.impl.zzl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.call.CallRecordService;
import com.nyd.application.model.call.CustInfoQuery;
import com.nyd.application.model.common.ListTool;
import com.nyd.order.model.common.BeanCommonUtils;
import com.nyd.user.api.zzl.UserForZLQServise;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.dao.ContactDao;
import com.nyd.user.entity.Contact;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.CustInfoQueryVO;


@Service("userForZLQServise")
public class UserForZLQServiseImpl implements UserForZLQServise{
	 @Autowired
	 private ContactDao contactDao;
	 @Autowired
	 private UserSqlService userSqlService;
	 
	    private static Logger logger = LoggerFactory.getLogger(UserForZLQServiseImpl.class);

		@Autowired
		private CallRecordService callRecordService;
	
	 @Override
	 public List<Contact> findContactByUserId(String userId) {
		 List<Contact> contact=new ArrayList<Contact>();
        try {
        	System.out.println("参数"+userId);
        	//String sql ="select * from t_user_contact  t where t.user_id='"+userId+"'";
        	String sql ="select * from t_user_contact  t where t.user_id= '%s'";
    		contact=userSqlService.queryT(String.format(sql, userId), Contact.class);
    		System.out.println(JSONObject.toJSONString(contact));
        } catch (Exception e) {
        }
        return contact;
	 }
	 @Override
	 public List<Contact> findByUserId(String userId){
		
		 List<Contact> contact=new ArrayList<Contact>();
        try {
        	contact = contactDao.getUserContacts(userId);;
        } catch (Exception e) {
        }
        return contact;
	 }
	 
		@Override
		public PagedResponse<List<CallRecordVO>> queryCallRecord(CustInfoQueryVO vo) {
			PagedResponse<List<CallRecordVO>> common =  new PagedResponse<List<CallRecordVO>>();
			logger.error(" start" + JSON.toJSONString(vo));
			CustInfoQuery custInfoQuery = new CustInfoQuery();
			custInfoQuery.setMobile(vo.getMobile());
			custInfoQuery.setCustInfoId(vo.getCustInfoId());
			custInfoQuery.setOrdrId(vo.getOrderId());
			custInfoQuery.setCallNo(vo.getCallNo());
			custInfoQuery.setName(vo.getName());
			custInfoQuery.setPageNo(vo.getPageNo());
			custInfoQuery.setPageSize(vo.getPageSize());
			List<CallRecordVO>  retList = new ArrayList<CallRecordVO>(); 
			try {
				com.nyd.application.model.common.PagedResponse<List<com.nyd.application.model.call.CallRecordVO>> pagecommon = callRecordService.getCallRecord(custInfoQuery);
				logger.error("pagecommon:" + JSON.toJSONString(pagecommon));
				//retList = BeanCommonUtils.copyListProperties(pagecommon.getData(), CallRecordVO.class);
				List list = pagecommon.getData();
				retList = new ListTool<CallRecordVO>().parseToObject(list, CallRecordVO.class);
				logger.error("retList:"+ JSON.toJSONString(retList));
				common.setData(retList);
				common.setTotal(pagecommon.getTotal());
				common.setSuccess(true);
				common.setCode("1");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("test 查询通话记录异常" + e.getMessage());
				common.setCode("0");
				common.setSuccess(false);
			}
			return common;
		}
	 
	 
	 

	
}
