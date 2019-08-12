package com.nyd.user.service.impl.zzl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.common.ArithUtil;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.dao.UserDetailDao;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.vo.ImageUrlVo;
import com.nyd.user.model.vo.OrderCustInfoVO;
import com.tasfe.framework.support.model.ResponseData;

@Service("userForZQServise")
public class UserForZQServiseImpl implements UserForZQServise{
	
    private static Logger logger = LoggerFactory.getLogger(UserForZQServiseImpl.class);

    private static final String URL="http://a.ugiygk.cn/";
	
	@Autowired
	private OrderContract orderContract;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private UserDetailDao userDetailDao;
	
	@Autowired
	private UserJobContract userJobContract;
	
	@Autowired
	private UserSqlService userSqlService;
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
	public CommonResponse<OrderCustInfoVO> getCustInfoByOrderNo(String orderNo) {
		logger.error("请求订单详情-客户信息,请求参数:orderNo:"+orderNo);
		CommonResponse<OrderCustInfoVO> common = new CommonResponse<OrderCustInfoVO>();
		OrderCustInfoVO resultVo = new OrderCustInfoVO();
		try {
			ResponseData<OrderInfo> orderInfoResp = orderContract.getOrderByOrderNo(orderNo);
			OrderInfo orderInfo = orderInfoResp.getData();
			String userId = orderInfo.getUserId();
			List<UserInfo> userInfos = userDao.getUsersByUserId(userId);
			if(ChkUtil.isEmpty(userInfos) || userInfos.size()<=0){
				// TODO Auto-generated catch block
			}else {
				UserInfo userInfo = userInfos.get(0);
				resultVo.setCustName(userInfo.getRealName());
				// TODO Auto-generated catch block 手机号 运营商
				resultVo.setMobile(userInfo.getAccountNumber());
				resultVo.setCustIc(userInfo.getIdNumber());
			}
			List<AccountDto> accounts = accountDao.getAccountByuserId(userId);
			if(!ChkUtil.isEmpty(accounts) && accounts.size()>0){
				AccountDto account = accounts.get(0);
				resultVo.setMobile(account.getAccountNumber());
			}
			
			//查询客户详情
			List<UserDetailInfo> userDetails = userDetailDao.getUserDetailsByUserId(userId);
			if(!ChkUtil.isEmpty(userDetails) && userDetails.size()>0){
				UserDetailInfo userDetailInfo = userDetails.get(0);
				String pro = ChkUtil.isEmpty(userDetailInfo.getLivingProvince())?"":userDetailInfo.getLivingProvince();
				String city =ChkUtil.isEmpty( userDetailInfo.getLivingCity())?"": userDetailInfo.getLivingCity();
				String dis =ChkUtil.isEmpty(userDetailInfo.getLivingDistrict())?"":userDetailInfo.getLivingDistrict();
				String address =ChkUtil.isEmpty(userDetailInfo.getLivingAddress())?"":userDetailInfo.getLivingAddress() ;
				resultVo.setHomeAddress(pro+city+dis+address);			//居住地址
				resultVo.setEducation(ChkUtil.isEmpty(userDetailInfo.getHighestDegree())?"":userDetailInfo.getHighestDegree());	//学历
				resultVo.setMarriage(ChkUtil.isEmpty(userDetailInfo.getMaritalStatus())?"":userDetailInfo.getMaritalStatus());	//婚姻状态
			}
			
			//查询工作信息
			ResponseData<JobInfo> jobInfoResp = userJobContract.getJobInfo(userId);
			if(!ChkUtil.isEmpty(jobInfoResp.getData())){
				JobInfo jobInfo = jobInfoResp.getData();
				String pro = ChkUtil.isEmpty(jobInfo.getCompanyProvince())?"":jobInfo.getCompanyProvince();
				String city =ChkUtil.isEmpty( jobInfo.getCompanyCity())?"": jobInfo.getCompanyCity();
				String dis =ChkUtil.isEmpty(jobInfo.getCompanyDistrict())?"":jobInfo.getCompanyDistrict();
				String address =ChkUtil.isEmpty(jobInfo.getCompanyAddress())?"":jobInfo.getCompanyAddress() ;
				resultVo.setCompanyName(ChkUtil.isEmpty(jobInfo.getCompany())?"":jobInfo.getCompany());
				resultVo.setCompanyAddress(pro+city+dis+address);
				resultVo.setJobType(ChkUtil.isEmpty(jobInfo.getIndustry())?"":jobInfo.getIndustry());
				resultVo.setJob(ChkUtil.isEmpty(jobInfo.getProfession())?"":jobInfo.getProfession());
				resultVo.setMonthlyIncome(ChkUtil.isEmpty(jobInfo.getSalary())?"":jobInfo.getSalary());
			}
			common.setCode("1");
			common.setSuccess(true);
			common.setData(resultVo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单详情-客户信息出现异常,请求参数:orderNo:"+orderNo);
			logger.error(e.getMessage());
			common.setCode("0");
			common.setSuccess(false);
		}
		return common;
	}

	@Override
	public BankInfo getAccountNo(String userId){
		String sql="select user_id as userId, bank_account as bankAccount, soure, channel_code as channelCode,"
				+ "bank_name as bankName,account_name as accountName,account_ic as accountIc "
				+ "from t_user_bank where user_id = '"+userId+"'";
		JSONObject jsonObject = userSqlService.queryOne(sql);
		BankInfo bankInfo = JSONObject.parseObject(JSON.toJSONString(jsonObject), BankInfo.class);
		return bankInfo;
	}
	
	@Override
	public JSONObject getAccountInfo(String userId) {
		String sql="select account_name as accountName,reserved_phone as accountMobile,account_ic AS accountIc,bank_account as cardNumber,"
				+ "protocol_no as protocolNo,hlb_user_id as hlbUserId,user_id as userId,bank_code as bankCode from t_user_bank where soure = 2 "
				+ "and user_id = '"+userId+"'";
		JSONObject jsonObject = userSqlService.queryOne(sql);
		//查询用户手机号
		String userMobile = userSqlService.queryOne("select account_number as mobile from t_account where user_id = '"+userId+"'").getString("mobile");
		jsonObject.put("userMobile", userMobile);
		if(ChkUtil.isEmpty(jsonObject)){
			return null;
		}else {
			return jsonObject;
		}
	}


	/**
	 * 正反面照片查询
	 */
	@Override
	public CommonResponse<ImageUrlVo> queryImgUrl(ImageUrlVo vo) {
		CommonResponse<ImageUrlVo> common = new CommonResponse<ImageUrlVo>();
		logger.info("身份证正反面查询,请求参数:ImageUrlVo:"+JSONObject.toJSONString(vo));
		try {
			//正面
			String frontUrl = queryUrl("1",vo.getUserId());
			//反面
			String backUrl = queryUrl("2",vo.getUserId());
			//活体
			String livingUrl = queryUrl("3",vo.getUserId());
			vo.setFrontUrl(frontUrl);
			vo.setBackUrl(backUrl);
			vo.setLivingUrl(livingUrl);
			common.setData(vo);
			common.setCode("1");
			common.setMsg("操作成功");
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("身份证正反面查询异常,请求参数:ImageUrlVo:"+JSONObject.toJSONString(vo));
			logger.error(e.getMessage());
			common.setCode("0");
			common.setSuccess(false);
		}
		return common;
	}
	
	//查询MongoDB
	public String queryUrl(String type,String userId){
		String url = "";
		try {
			Query query = Query.query(Criteria.where("type").is(type)
					.and("userId").is(userId)
					);
			query.with(new Sort(Direction.DESC, "create_time"));
			Map<String,Object> map = mongoTemplate.findOne(query, Map.class, "attachment");
			if(!ChkUtil.isEmpty(map) && map.containsKey("file") 
					&& !ChkUtil.isEmpty(map.get("file"))){
				url=URL+map.get("file").toString();
			}
		} catch (Exception e) {
			logger.error("身份证正反面或活体查询mongodb异常,请求参数type:{},userId:{}",type,userId);
			logger.error(e.getMessage());
		}
		return url;
	}

	@Override
	public String queryAssessmentAmount(){
		String sql = "select real_fee as assessmentAmont from t_pay_fee limit 1";
		JSONObject json = userSqlService.queryOne(sql);
		String assessmentAmont = ChkUtil.isEmpty(json.getBigDecimal("assessmentAmont"))?"":ArithUtil.formatDouble(json.getBigDecimal("assessmentAmont").doubleValue());
		return assessmentAmont;
	}
	
	 //查询身份证信息
	 @Override
	 public JSONObject findUserDetailByUserId(String userId) {
		 JSONObject result = new JSONObject();
       try {
       	System.out.println("参数"+userId);
			String userDetailSql = "select * from t_user_detail where user_id = '"+userId+"'" +"order by create_time desc limit 1";
			result = userSqlService.queryOne(userDetailSql);
       } catch (Exception e) {
       }
       return result;
	 }
}
