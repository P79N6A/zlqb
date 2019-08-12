package com.nyd.user.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.enums.AttachmentType;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.user.api.GetYmtUserDataService;
import com.nyd.user.dao.mapper.AccountResetMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Bank;
import com.nyd.user.entity.Password;
import com.nyd.user.entity.Step;
import com.nyd.user.model.AccountResetInfo;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.YmtUserSaveFlag;
import com.nyd.user.model.dto.YmtUserDto;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.AccountPassWordInfoService;
import com.nyd.user.service.BankInfoService;
import com.nyd.user.service.ContactInfoService;
import com.nyd.user.service.IdentityInfoService;
import com.nyd.user.service.JobInfoService;
import com.nyd.user.service.StepInfoService;
import com.nyd.user.service.UserDetailService;
import com.nyd.user.service.UserService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.Md5Util;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

/**
 * 同步银码头用户数据到侬要贷
 */

@Service(value = "getYmtUserDataService")
class GetYmtUserDataServiceImpl implements GetYmtUserDataService {
	private static Logger logger = LoggerFactory.getLogger(GetYmtUserDataServiceImpl.class);

	@Autowired
	private BankInfoService bankInfoService;

	@Autowired
	private ContactInfoService contactInfoService;

	@Autowired
	private JobInfoService jobInfoService;

	@Autowired
	private StepInfoService stepInfoService;

	@Autowired
	private AccountInfoService accountInfoService;

	@Autowired
	private AccountPassWordInfoService accountPassWordInfoService;

	@Autowired
	private IdentityInfoService identityInfoService;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private UserService userService;

	@Autowired
	private Md5Util md5Util;

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private QiniuContract qiniuContract;
	
	@Autowired
	private AccountResetMapper accountResetMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseData saveYmtUserData(YmtUserDto ymtUserDto) throws Exception {
		logger.info("begin to save account, accountNumber is " + ymtUserDto.getAccountNumber());
		ResponseData responseData = ResponseData.success();
		// 判断该用户是否存在
		if (StringUtils.isNotBlank(ymtUserDto.getAccountNumber()) && StringUtils.isNotBlank(ymtUserDto.getIdNumber())) { // 根据手机号码和身份证去判断是否为新老户
			String accountNumber = ymtUserDto.getAccountNumber(); // 手机号码
			String idNumber = ymtUserDto.getIdNumber(); // 身份证号码
			String userId = idGenerator.generatorId(BizCode.USER).toString(); // 用户userId
			logger.info("用户的相关信息参数：" + "用户id:" + userId + ",身份证号码：" + idNumber + ",手机号码：" + accountNumber);

			List<Account> accountList = null;
			List<UserInfo> userList = null;
			try {
				accountList = accountInfoService.findByAccountNumber(accountNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				userList = userService.findByIdNumber(idNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if ((accountList.size() > 0 && accountList != null) || (userList.size() > 0 && userList != null)) { // 表示该账号已经存在
				try {
					confirmBankInfo(ymtUserDto, accountList.get(0).getUserId());
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
				responseData.setMsg("该用户为老用户");
			} else { // 表示该账号为新账号
				// 保存用户基本信息：
				UserInfo userInfo = new UserInfo();
				userInfo.setUserId(userId);
				userInfo.setRealName(ymtUserDto.getRealName());
				userInfo.setIdNumber(ymtUserDto.getIdNumber());
				if (StringUtils.isNotBlank(ymtUserDto.getCertificateType())) {
					userInfo.setCertificateType(ymtUserDto.getCertificateType());
				} else {
					userInfo.setCertificateType("10");
				}
				String gender = Integer.parseInt(String.valueOf(ymtUserDto.getIdNumber().charAt(16))) % 2 == 0 ? "女"
						: "男";
				userInfo.setGender(gender);
				userInfo.setNation(ymtUserDto.getNation());
				userInfo.setBirth(ymtUserDto.getBirth());
				logger.info("银码头传过来的用户基本信息:" + JSON.toJSONString(userInfo));
				try {
					userService.saveUser(userInfo);
					logger.info("保存银码头传过来的用户基本信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户基本信息fail");
					responseData.setError("保存银码头传过来的用户基本信息fail");
				}

				// 保存用户详细信息：
				UserDetailInfo detailInfo = new UserDetailInfo();
				detailInfo.setUserId(userId);
				detailInfo.setRealName(ymtUserDto.getRealName());
				detailInfo.setIdNumber(ymtUserDto.getIdNumber());
				detailInfo.setIdAddress(ymtUserDto.getIdAddress());
				detailInfo.setSignOrg(ymtUserDto.getSignOrg());
				detailInfo.setEffectTime(ymtUserDto.getEffectTime());
				detailInfo.setLivingAddress(ymtUserDto.getLivingAddress());
				detailInfo.setLivingProvince(ymtUserDto.getLivingProvince());
				detailInfo.setLivingCity(ymtUserDto.getLivingCity());
				detailInfo.setLivingDistrict(ymtUserDto.getLivingDistrict());
				detailInfo.setMaritalStatus(ymtUserDto.getMaritalStatus());
				detailInfo.setHighestDegree(ymtUserDto.getHighestDegree());
				detailInfo.setFaceRecognition(ymtUserDto.getFaceRecognition());
				detailInfo.setFaceRecognitionSimilarity(ymtUserDto.getFaceRecognitionSimilarity());
				logger.info("银码头传过来的用户详细信息:" + JSON.toJSONString(detailInfo));
				try {
					userDetailService.saveUserDetail(detailInfo);
					logger.info("保存银码头传过来的用户详细信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户详细信息fail");
					responseData.setError("保存银码头传过来的用户详细信息fail");
				}

				// 保存账号信息：
				Account account = new Account();
				account.setUserId(userId);
				account.setAccountNumber(ymtUserDto.getAccountNumber());
				account.setIBankUserId(ymtUserDto.getIBankUserId()); // 关联银马头用户id
				account.setAccountType(10); // 账号类型

				if (StringUtils.isNotBlank(ymtUserDto.getLimitCredit().toString())) {
					account.setLimitCredit(ymtUserDto.getLimitCredit());
				} else {
					account.setLimitCredit(new BigDecimal(0));
				}

				if (StringUtils.isNotBlank(ymtUserDto.getUsableCredit().toString())) {
					account.setUsableCredit(ymtUserDto.getUsableCredit());
				} else {
					account.setUsableCredit(new BigDecimal(0));
				}

				if (StringUtils.isNotBlank(ymtUserDto.getUsedCredit().toString())) {
					account.setUsedCredit(ymtUserDto.getUsedCredit());
				} else {
					account.setUsedCredit(new BigDecimal(0));
				}

				if (StringUtils.isNotBlank(ymtUserDto.getProductCode())) {
					account.setProductCode(ymtUserDto.getProductCode());
				} else {
					account.setProductCode("cash");
				}
				if (StringUtils.isNotBlank(ymtUserDto.getSource())) {
					account.setSource(ymtUserDto.getSource());
				} else {
					account.setSource("nyd");
				}

				// 对于新增的用户渠道channel字段处理：
				account.setChannel(1); // 1为银码头

				if (StringUtils.isNotBlank(ymtUserDto.getAccount_type().toString())) {
					account.setAccountType(ymtUserDto.getAccount_type());
				} else {
					account.setAccountType(10);
				}
				logger.info("银码头传过来的用户账号信息：" + JSON.toJSONString(account));
				try {
					accountInfoService.saveYmtAccount(account);
					// 保存账户信息至reidis中
					accountInfoService.saveAccountInRedis(account, null);
					logger.info("保存银码头传过来的用户账号信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户账号信息fail:" + e);
					responseData.setError("保存银码头传过来的用户账号信息fail");
				}

				// 更新信息完整度
				Step step = new Step();
				step.setUserId(userInfo.getUserId());
				step.setIdentityFlag(UserConsts.FILL_FLAG);// 身份证信息完整度
				try {
					stepInfoService.saveStep(step);
					logger.info("保存身份证信息完整度success");
				} catch (Exception e) {
					logger.error("保存身份证信息完整度fail");
					responseData.setError("保存身份证信息完整度fail");
				}

				// 保存联系人信息
				ContactInfos contactInfos = new ContactInfos();
				contactInfos.setUserId(userId);
				contactInfos.setAccountNumber(accountNumber);
				contactInfos.setDirectContactMobile(ymtUserDto.getDirectContactMobile());
				contactInfos.setMajorContactMobile(ymtUserDto.getMajorContactMobile());
				contactInfos.setDirectContactName(ymtUserDto.getDirectContactName());
				contactInfos.setMajorContactName(ymtUserDto.getMajorContactName());
				contactInfos.setDirectContactRelation(ymtUserDto.getDirectContactRelation());
				contactInfos.setMajorContactRelation(ymtUserDto.getMajorContactRelation());
				logger.info("银码头传过来的用户联系人信息：" + JSON.toJSONString(contactInfos));
				try {
					contactInfoService.saveContactInfo(contactInfos);
					logger.info("保存银码头传过来的用户联系人信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户联系人信息fail:" + e);
					responseData.setError("保存银码头传过来的用户联系人信息fail");
				}

				// 保存用户工作信息
				JobInfo jobInfo = new JobInfo();
				jobInfo.setUserId(userId);
				jobInfo.setCompany(ymtUserDto.getCompany());
				jobInfo.setTelephone(ymtUserDto.getTelephone());
				jobInfo.setTelDistrictNo(ymtUserDto.getTelDistrictNo());
				jobInfo.setProfession(ymtUserDto.getProfession());
				jobInfo.setTelExtNo(ymtUserDto.getTelExtNo());
				jobInfo.setIndustry(ymtUserDto.getIndustry());
				jobInfo.setCompanyAddress(ymtUserDto.getCompanyAddress());
				jobInfo.setCompanyCity(ymtUserDto.getCompanyCity());
				jobInfo.setCompanyProvince(ymtUserDto.getCompanyProvince());
				jobInfo.setCompanyDistrict(ymtUserDto.getCompanyDistrict());
				jobInfo.setSalary(ymtUserDto.getSalary());
				logger.info("银码头传过来的用户工作信息：" + JSON.toJSONString(jobInfo));
				try {
					jobInfoService.saveJobInfo(jobInfo);
					logger.info("保存银码头传过来的用户工作信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户工作信息fail:" + e);
					responseData.setError("保存银码头传过来的用户工作信息fail");
				}

				// 保存银行卡信息
				Bank bank = new Bank();
				bank.setUserId(userId);
				bank.setAccountName(ymtUserDto.getAccountName());
				if (StringUtils.isNotBlank(ymtUserDto.getAccountType())) {
					bank.setAccountType(ymtUserDto.getAccountType());
				} else {
					bank.setAccountType("");
				}
				bank.setBankAccount(ymtUserDto.getBankAccount());
				bank.setBankName(ymtUserDto.getBankName());
				bank.setReservedPhone(ymtUserDto.getReservedPhone());
				logger.info("银码头传过来的用户银行卡信息：" + JSON.toJSONString(bank));
				try {
					bankInfoService.saveBank(bank);
					logger.info("保存银码头传过来的用户银行卡信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户银行卡信息fail:" + e);
					responseData.setError("保存银码头传过来的用户银行卡信息fail");
				}

				// 保存新用户密码
				Password password = new Password();
				password.setPasswordType(1);
				password.setAccountNumber(accountNumber);
				password.setPassword(md5Util.getSecondMD5(ymtUserDto.getPassword()));
				logger.info("银码头传过来的密码参数：" + JSON.toJSONString(password));
				try {
					accountPassWordInfoService.saveAccountPassword(password);
					logger.info("保存银码头传过来的密码信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的密码信息fail：" + e);
					responseData.setError("保存银码头传过来的密码信息fail");
				}

				// 用户信息填写情况
				// 公信宝认证时间、淘宝认证时间、手机认证时间默认为null,有些参数的值在做其他保存的时候已经存起来了
				Step step1 = new Step();
				step1.setUserId(userId);
//                step1.setGxbFlag(ymtUserDto.getGxbFlag());

				if (StringUtils.isNotBlank(ymtUserDto.getTbFlag())) {
					step1.setTbFlag(ymtUserDto.getTbFlag());
				} else {
					step1.setTbFlag("1");
				}

				if (ymtUserDto.getTbWeight() != null) {
					step1.setTbWeight(ymtUserDto.getTbWeight());
				} else {
					step1.setTbWeight(1);
				}

				if (StringUtils.isNotBlank(ymtUserDto.getOnlineBankFlag())) {
					step1.setOnlineBankFlag(ymtUserDto.getOnlineBankFlag());
				} else {
					step1.setOnlineBankFlag("1");
				}

				if (ymtUserDto.getOnlineBankWeight() != null) {
					step1.setOnlineBankWeight(ymtUserDto.getOnlineBankWeight());
				} else {
					step1.setOnlineBankWeight(1);
				}

				if (StringUtils.isNotBlank(ymtUserDto.getMobileFlag())) {
					step1.setMobileFlag(ymtUserDto.getMobileFlag());
				} else {
					step1.setMobileFlag("1");
				}
				// 侬要贷风控前置需要运营商，以及手机埋点等数据，所以需要侬要贷这边重新认证
				step1.setMobileFlag("0");
				if (ymtUserDto.getMobileWeight() != null) {
					step1.setMobileWeight(ymtUserDto.getMobileWeight());
				} else {
					step1.setMobileWeight(1);
				}

				if (StringUtils.isNotBlank(ymtUserDto.getZmxyFlag())) {
					step1.setZmxyFlag(ymtUserDto.getZmxyFlag());
				} else {
					step1.setZmxyFlag("1");
				}

				if (ymtUserDto.getZmxyWeight() != null) {
					step1.setZmxyWeight(ymtUserDto.getZmxyWeight());
				} else {
					step1.setZmxyWeight(1);
				}

				if (StringUtils.isNotBlank(ymtUserDto.getGxbFlag())) {
					step1.setGxbFlag(ymtUserDto.getGxbFlag());
				} else {
					step1.setGxbFlag("1");
				}

				if (StringUtils.isNotBlank(ymtUserDto.getAuthFlag())) {
					step1.setAuthFlag(ymtUserDto.getAuthFlag());
				} else {
					step1.setAuthFlag("1");
				}
				step1.setAuthFlag("0");
				logger.info("用户：" + ymtUserDto.getRealName() + "银码头传过来的用户信息填写情况参数：" + JSON.toJSONString(step1));
				try {
//                    stepInfoService.saveStep(step1);
					stepInfoService.updateStep(step1);
					logger.info("保存银码头传过来的用户信息填写情况参数success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户信息填写情况参数fail：" + e);
					responseData.setError("保存银码头传过来的用户信息填写情况参数fail");
				}

				// 照片保存：
				try {
					saveAttachment(userId, ymtUserDto.getIdCardFrontPhoto(), ymtUserDto.getIdCardBackPhoto());
					logger.info("照片保存成功");
				} catch (Exception e) {
					logger.error("save photo error! userId = " + userId, e);
				}

			}

		} else {
			logger.info("参数缺失");
			responseData.setMsg("参数缺失");
		}

		return responseData;
	}

	/**
	 * 确认银行卡信息是否存在，如不存在查询划扣绑卡接口，查询到绑卡信息则保存
	 * 
	 * @param userDto
	 * @return
	 */
	private ResponseData confirmBankInfo(YmtUserDto userDto, String userId) throws Exception {
		ResponseData<List<BankInfo>> bankInfos = bankInfoService.getBankInfosByBankAccout(userDto.getBankAccount());
		if ("0".equals(bankInfos.getStatus())) {
			if (bankInfos.getData() != null && bankInfos.getData().size() > 0) {
				return ResponseData.success();
			} else {
				BankInfo bank = new BankInfo();
				bank.setUserId(userId);
				bank.setAccountName(userDto.getAccountName());
				if (StringUtils.isNotBlank(userDto.getAccountType())) {
					bank.setAccountType(userDto.getAccountType());
				} else {
					bank.setAccountType("");
				}
				bank.setBankAccount(userDto.getBankAccount());
				bank.setBankName(userDto.getBankName());
				bank.setReservedPhone(userDto.getReservedPhone());
				logger.info("更新t_user_bank表记录：" + JSON.toJSONString(bank));
				bankInfoService.saveBankInfoNoJudge(bank);
			}
		} else {
			logger.info("查询银行卡信息失败" + bankInfos.getMsg());
			return ResponseData.error("查询银行卡信息失败");
		}
		return ResponseData.success();
	}

	/**
	 * 保存身份证正反面照片
	 */
	private void saveAttachment(String userId, String frontfile, String backfile) {
		new Thread(() -> {
			logger.info("begin to save idCard front photo, userId is " + userId);
			// 身份证正面照
			AttachmentModel frontModel = new AttachmentModel();
			frontModel.setUserId(userId);
			frontModel.setFile(frontfile);
			frontModel.setType(AttachmentType.IDCARD_FRONT_PHOTO.getType());
			frontModel.setFileName(AttachmentType.IDCARD_FRONT_PHOTO.getDescription());
			qiniuContract.base64Upload(frontModel);
			logger.info("begin to save idCard back photo, userId is " + userId);
			// 身份证反面照
			AttachmentModel backModel = new AttachmentModel();
			backModel.setUserId(userId);
			backModel.setFile(backfile);
			backModel.setType(AttachmentType.IDCARD_BACK_PHOTO.getType());
			backModel.setFileName(AttachmentType.IDCARD_BACK_PHOTO.getDescription());
			qiniuContract.base64Upload(backModel);
			logger.info("save idCard photo success");
		}).start();
	}

	@Override
	public ResponseData saveYmtUserDataAsStep(YmtUserDto ymtUserDto) throws Exception {
		logger.info("begin to save user info, accountNumber is " + ymtUserDto.getAccountNumber());
		ResponseData responseData = ResponseData.success();
		if (StringUtils.isBlank(ymtUserDto.getAccountNumber()) && StringUtils.isBlank(ymtUserDto.getIdNumber())) {
			logger.info("参数缺失: ");
			responseData.setMsg("参数缺失");
			return responseData;
		}
		logger.info("用户的相关信息参数： " + ",身份证号码： " + ymtUserDto.getIdNumber() + ",手机号码： " + ymtUserDto.getAccountNumber());
		YmtUserSaveFlag flag = getUserSaveFlag(ymtUserDto);
		return  saveUserInfo(ymtUserDto,flag);
	}

	private ResponseData saveUserInfo(YmtUserDto ymtUserDto,YmtUserSaveFlag flag) throws Exception {
		ResponseData responseData = ResponseData.success();
		List<Account> accountList = null;
		List<UserInfo> userList = null;
		try {
			accountList = accountInfoService.findByAccountNumber(ymtUserDto.getAccountNumber());
		} catch (Exception e) {
			logger.error("查询账户信息异常：" + e.getMessage());
			return responseData.error("查询账户信息异常");
		}
		try {
			userList = userService.findByIdNumber(ymtUserDto.getIdNumber());
		} catch (Exception e) {
			logger.error("查询客户信息异常：" + e.getMessage());
			return responseData.error("查询账户信息异常");
		}
		String userId = idGenerator.generatorId(BizCode.USER).toString(); // 用户userId
		if(userList != null && userList.size() > 0) {
			userId = userList.get(0).getUserId();
		}
		if(flag.isSaveAccount()) {
			saveAccount(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveUser()) {
			saveUser(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveAccountPassword()) {
			savePassword(ymtUserDto,responseData);
		}
		if(flag.isSaveUserBank()) {
			saveUserBank(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveUserContract()) {
			saveUsercontract(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveUserDetail()) {
			saveUserDetail(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveUserJob()) {
			saveUserJob(ymtUserDto,userId,responseData);
		}
		if(flag.isResetAccountNumber()) {
			resetAccountNumber(ymtUserDto,userId,responseData);
		}
		if(flag.isSaveUserStep()) {
			//保存客户数据标志
			saveUserStep(ymtUserDto,userId,responseData);
		}
		if(flag.isUpdateUserStep()) {
			updateUserStep(ymtUserDto,userId,responseData);
		}
		// 照片保存：
		try {
			saveAttachment(userId, ymtUserDto.getIdCardFrontPhoto(), ymtUserDto.getIdCardBackPhoto());
			logger.info("照片保存成功");
		} catch (Exception e) {
			logger.error("save photo error! userId = " + userId, e);
		}
		return responseData;
	}

	/**
	 * 重置手机号
	 * @param ymtUserDto
	 * @param userId
	 * @param responseData
	 * @throws Exception
	 */
	private void resetAccountNumber(YmtUserDto ymtUserDto, String userId, ResponseData responseData) throws Exception{
		List<Account> accountList = null;
		List<UserInfo> userList = null;
		try {
			accountList = accountInfoService.findByAccountNumber(ymtUserDto.getAccountNumber());
		} catch (Exception e) {
			logger.error("查询账户信息异常：" + e.getMessage());
			throw e;
		}
		try {
			userList = userService.findByIdNumber(ymtUserDto.getIdNumber());
		} catch (Exception e) {
			logger.error("查询客户信息异常：" + e.getMessage());
			throw e;
		}
		List<Account> list = accountInfoService.findByUserId(userId);
		if(list == null || list.size() == 0) {
			throw new Exception("账户信息错误");
		}
		AccountResetInfo reset = new AccountResetInfo();
		reset.setIbankUserId(ymtUserDto.getIBankUserId());
		reset.setUserId(userId);
		reset.setNewAccountNumber(ymtUserDto.getAccountNumber());
		reset.setOldAccountNumber(list.get(0).getAccountNumber());
		accountResetMapper.save(reset);
		if(list.get(0).getChannel().equals(1)) {
			updateAccountForReset(ymtUserDto,userId,responseData);
			updatePasswordForReset(ymtUserDto,userId,responseData);
			updateUserDetailForReset(ymtUserDto,userId,responseData);
		}
		saveUserJob(ymtUserDto,userId,responseData);
		saveUsercontract(ymtUserDto,userId,responseData);
		saveUserBank(ymtUserDto,userId,responseData);
		saveUserDetail(ymtUserDto,userId,responseData);
	}

	private void updateUserDetailForReset(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存用户详细信息：
		UserDetailInfo detailInfo = new UserDetailInfo();
		detailInfo.setUserId(userId);
		detailInfo.setRealName(ymtUserDto.getRealName());
		detailInfo.setIdNumber(ymtUserDto.getIdNumber());
		detailInfo.setIdAddress(ymtUserDto.getIdAddress());
		detailInfo.setSignOrg(ymtUserDto.getSignOrg());
		detailInfo.setEffectTime(ymtUserDto.getEffectTime());
		detailInfo.setLivingAddress(ymtUserDto.getLivingAddress());
		detailInfo.setLivingProvince(ymtUserDto.getLivingProvince());
		detailInfo.setLivingCity(ymtUserDto.getLivingCity());
		detailInfo.setLivingDistrict(ymtUserDto.getLivingDistrict());
		detailInfo.setMaritalStatus(ymtUserDto.getMaritalStatus());
		detailInfo.setHighestDegree(ymtUserDto.getHighestDegree());
		detailInfo.setFaceRecognition(ymtUserDto.getFaceRecognition());
		detailInfo.setFaceRecognitionSimilarity(ymtUserDto.getFaceRecognitionSimilarity());
		logger.info("银码头传过来的用户详细信息:" + JSON.toJSONString(detailInfo));
		try {
			userDetailService.updateUserDetailByUserId(detailInfo);
			logger.info("保存银码头传过来的用户详细信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户详细信息fail");
			responseData.setError("保存银码头传过来的用户详细信息fail");
		}
		
	}

	private void savePasswordForReset(YmtUserDto ymtUserDto, String userId, ResponseData responseData) throws Exception{
		// 保存新用户密码
		Password password = new Password();
		password.setPasswordType(1);
		password.setAccountNumber(ymtUserDto.getAccountNumber());
		password.setPassword(md5Util.getSecondMD5(ymtUserDto.getPassword()));
		logger.info("银码头传过来的密码参数：" + JSON.toJSONString(password));
		try {
			accountPassWordInfoService.saveAccountPassword(password);
			logger.info("保存银码头传过来的密码信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的密码信息fail：" + e);
			responseData.setError("保存银码头传过来的密码信息fail");
		}
	}
	private void updatePasswordForReset(YmtUserDto ymtUserDto, String userId, ResponseData responseData) throws Exception{
		// 保存新用户密码
		Password password = new Password();
		password.setPasswordType(1);
		password.setAccountNumber(ymtUserDto.getAccountNumber());
		password.setPassword(md5Util.getSecondMD5(ymtUserDto.getPassword()));
		logger.info("银码头传过来的密码参数：" + JSON.toJSONString(password));
		try {
			accountPassWordInfoService.updateAccountPassword(password);
			logger.info("保存银码头传过来的密码信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的密码信息fail：" + e);
			responseData.setError("保存银码头传过来的密码信息fail");
		}
	}

	private void saveUserStep(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 用户信息填写情况
		// 公信宝认证时间、淘宝认证时间、手机认证时间默认为null,有些参数的值在做其他保存的时候已经存起来了
		Step step1 = new Step();
		step1.setUserId(userId);
//		        step1.setGxbFlag(ymtUserDto.getGxbFlag());

		if (StringUtils.isNotBlank(ymtUserDto.getTbFlag())) {
			step1.setTbFlag(ymtUserDto.getTbFlag());
		} else {
			step1.setTbFlag("1");
		}

		if (ymtUserDto.getTbWeight() != null) {
			step1.setTbWeight(ymtUserDto.getTbWeight());
		} else {
			step1.setTbWeight(1);
		}

		if (StringUtils.isNotBlank(ymtUserDto.getOnlineBankFlag())) {
			step1.setOnlineBankFlag(ymtUserDto.getOnlineBankFlag());
		} else {
			step1.setOnlineBankFlag("1");
		}

		if (ymtUserDto.getOnlineBankWeight() != null) {
			step1.setOnlineBankWeight(ymtUserDto.getOnlineBankWeight());
		} else {
			step1.setOnlineBankWeight(1);
		}

		if (StringUtils.isNotBlank(ymtUserDto.getMobileFlag())) {
			step1.setMobileFlag(ymtUserDto.getMobileFlag());
		} else {
			step1.setMobileFlag("1");
		}
		// 侬要贷风控前置需要运营商，以及手机埋点等数据，所以需要侬要贷这边重新认证
		step1.setMobileFlag("0");
		if (ymtUserDto.getMobileWeight() != null) {
			step1.setMobileWeight(ymtUserDto.getMobileWeight());
		} else {
			step1.setMobileWeight(1);
		}

		if (StringUtils.isNotBlank(ymtUserDto.getZmxyFlag())) {
			step1.setZmxyFlag(ymtUserDto.getZmxyFlag());
		} else {
			step1.setZmxyFlag("1");
		}

		if (ymtUserDto.getZmxyWeight() != null) {
			step1.setZmxyWeight(ymtUserDto.getZmxyWeight());
		} else {
			step1.setZmxyWeight(1);
		}

		if (StringUtils.isNotBlank(ymtUserDto.getGxbFlag())) {
			step1.setGxbFlag(ymtUserDto.getGxbFlag());
		} else {
			step1.setGxbFlag("1");
		}

		if (StringUtils.isNotBlank(ymtUserDto.getAuthFlag())) {
			step1.setAuthFlag(ymtUserDto.getAuthFlag());
		} else {
			step1.setAuthFlag("1");
		}
		step1.setAuthFlag("0");
		logger.info("用户：" + ymtUserDto.getRealName() + "银码头传过来的用户信息填写情况参数：" + JSON.toJSONString(step1));
		try {
		    stepInfoService.saveStep(step1);
			//stepInfoService.updateStep(step1);
			logger.info("保存银码头传过来的用户信息填写情况参数success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户信息填写情况参数fail：" + e);
			responseData.setError("保存银码头传过来的用户信息填写情况参数fail");
		}
	}
	private void updateUserStep(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 用户信息填写情况
		// 公信宝认证时间、淘宝认证时间、手机认证时间默认为null,有些参数的值在做其他保存的时候已经存起来了
		Step step1 = new Step();
		step1.setUserId(userId);
//		        step1.setGxbFlag(ymtUserDto.getGxbFlag());
		
		if (StringUtils.isNotBlank(ymtUserDto.getTbFlag())) {
			step1.setTbFlag(ymtUserDto.getTbFlag());
		} else {
			step1.setTbFlag("1");
		}
		
		if (ymtUserDto.getTbWeight() != null) {
			step1.setTbWeight(ymtUserDto.getTbWeight());
		} else {
			step1.setTbWeight(1);
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getOnlineBankFlag())) {
			step1.setOnlineBankFlag(ymtUserDto.getOnlineBankFlag());
		} else {
			step1.setOnlineBankFlag("1");
		}
		
		if (ymtUserDto.getOnlineBankWeight() != null) {
			step1.setOnlineBankWeight(ymtUserDto.getOnlineBankWeight());
		} else {
			step1.setOnlineBankWeight(1);
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getMobileFlag())) {
			step1.setMobileFlag(ymtUserDto.getMobileFlag());
		} else {
			step1.setMobileFlag("1");
		}
		// 侬要贷风控前置需要运营商，以及手机埋点等数据，所以需要侬要贷这边重新认证
		step1.setMobileFlag("0");
		if (ymtUserDto.getMobileWeight() != null) {
			step1.setMobileWeight(ymtUserDto.getMobileWeight());
		} else {
			step1.setMobileWeight(1);
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getZmxyFlag())) {
			step1.setZmxyFlag(ymtUserDto.getZmxyFlag());
		} else {
			step1.setZmxyFlag("1");
		}
		
		if (ymtUserDto.getZmxyWeight() != null) {
			step1.setZmxyWeight(ymtUserDto.getZmxyWeight());
		} else {
			step1.setZmxyWeight(1);
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getGxbFlag())) {
			step1.setGxbFlag(ymtUserDto.getGxbFlag());
		} else {
			step1.setGxbFlag("1");
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getAuthFlag())) {
			step1.setAuthFlag(ymtUserDto.getAuthFlag());
		} else {
			step1.setAuthFlag("1");
		}
		step1.setAuthFlag("0");
		logger.info("用户：" + ymtUserDto.getRealName() + "银码头传过来的用户信息填写情况参数：" + JSON.toJSONString(step1));
		try {
			//stepInfoService.saveStep(step1);
			stepInfoService.updateStep(step1);
			logger.info("保存银码头传过来的用户信息填写情况参数success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户信息填写情况参数fail：" + e);
			responseData.setError("保存银码头传过来的用户信息填写情况参数fail");
		}
	}

	private void saveUserJob(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存用户工作信息
		JobInfo jobInfo = new JobInfo();
		jobInfo.setUserId(userId);
		jobInfo.setCompany(ymtUserDto.getCompany());
		jobInfo.setTelephone(ymtUserDto.getTelephone());
		jobInfo.setTelDistrictNo(ymtUserDto.getTelDistrictNo());
		jobInfo.setProfession(ymtUserDto.getProfession());
		jobInfo.setTelExtNo(ymtUserDto.getTelExtNo());
		jobInfo.setIndustry(ymtUserDto.getIndustry());
		jobInfo.setCompanyAddress(ymtUserDto.getCompanyAddress());
		jobInfo.setCompanyCity(ymtUserDto.getCompanyCity());
		jobInfo.setCompanyProvince(ymtUserDto.getCompanyProvince());
		jobInfo.setCompanyDistrict(ymtUserDto.getCompanyDistrict());
		jobInfo.setSalary(ymtUserDto.getSalary());
		logger.info("银码头传过来的用户工作信息：" + JSON.toJSONString(jobInfo));
		try {
			jobInfoService.saveJobInfo(jobInfo);
			logger.info("保存银码头传过来的用户工作信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户工作信息fail:" + e);
			responseData.setError("保存银码头传过来的用户工作信息fail");
		}
	}

	private void saveUserDetail(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存用户详细信息：
		UserDetailInfo detailInfo = new UserDetailInfo();
		detailInfo.setUserId(userId);
		detailInfo.setRealName(ymtUserDto.getRealName());
		detailInfo.setIdNumber(ymtUserDto.getIdNumber());
		detailInfo.setIdAddress(ymtUserDto.getIdAddress());
		detailInfo.setSignOrg(ymtUserDto.getSignOrg());
		detailInfo.setEffectTime(ymtUserDto.getEffectTime());
		detailInfo.setLivingAddress(ymtUserDto.getLivingAddress());
		detailInfo.setLivingProvince(ymtUserDto.getLivingProvince());
		detailInfo.setLivingCity(ymtUserDto.getLivingCity());
		detailInfo.setLivingDistrict(ymtUserDto.getLivingDistrict());
		detailInfo.setMaritalStatus(ymtUserDto.getMaritalStatus());
		detailInfo.setHighestDegree(ymtUserDto.getHighestDegree());
		detailInfo.setFaceRecognition(ymtUserDto.getFaceRecognition());
		detailInfo.setFaceRecognitionSimilarity(ymtUserDto.getFaceRecognitionSimilarity());
		logger.info("银码头传过来的用户详细信息:" + JSON.toJSONString(detailInfo));
		try {
			userDetailService.saveUserDetail(detailInfo);
			logger.info("保存银码头传过来的用户详细信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户详细信息fail");
			responseData.setError("保存银码头传过来的用户详细信息fail");
		}
	}

	private void saveUsercontract(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存联系人信息
		ContactInfos contactInfos = new ContactInfos();
		contactInfos.setUserId(userId);
		contactInfos.setAccountNumber(ymtUserDto.getAccountNumber());
		contactInfos.setDirectContactMobile(ymtUserDto.getDirectContactMobile());
		contactInfos.setMajorContactMobile(ymtUserDto.getMajorContactMobile());
		contactInfos.setDirectContactName(ymtUserDto.getDirectContactName());
		contactInfos.setMajorContactName(ymtUserDto.getMajorContactName());
		contactInfos.setDirectContactRelation(ymtUserDto.getDirectContactRelation());
		contactInfos.setMajorContactRelation(ymtUserDto.getMajorContactRelation());
		logger.info("银码头传过来的用户联系人信息：" + JSON.toJSONString(contactInfos));
		try {
			contactInfoService.saveContactInfo(contactInfos);
			logger.info("保存银码头传过来的用户联系人信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户联系人信息fail:" + e);
			responseData.setError("保存银码头传过来的用户联系人信息fail");
		}
		
	}

	private void saveUserBank(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		
		ResponseData<List<BankInfo>> bankInfos = bankInfoService.getBankInfosByBankAccout(ymtUserDto.getBankAccount());
		Bank bank = new Bank();
		if ("0".equals(bankInfos.getStatus())) {
			if (bankInfos.getData() != null && bankInfos.getData().size() > 0) {
				bank.setBankAccount(ymtUserDto.getBankAccount());
				bank.setReservedPhone(ymtUserDto.getReservedPhone());
				try {
					bankInfoService.updateBank(bank);
					logger.info("保存银码头传过来的用户银行卡信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户银行卡信息fail:" + e);
					responseData.setError("保存银码头传过来的用户银行卡信息fail");
				}
			} else {
				bank.setUserId(userId);
				bank.setAccountName(ymtUserDto.getAccountName());
				if (StringUtils.isNotBlank(ymtUserDto.getAccountType())) {
					bank.setAccountType(ymtUserDto.getAccountType());
				} else {
					bank.setAccountType("");
				}
				bank.setBankAccount(ymtUserDto.getBankAccount());
				bank.setBankName(ymtUserDto.getBankName());
				bank.setReservedPhone(ymtUserDto.getReservedPhone());
				logger.info("银码头传过来的用户银行卡信息：" + JSON.toJSONString(bank));
				try {
					bankInfoService.saveBank(bank);
					logger.info("保存银码头传过来的用户银行卡信息success");
				} catch (Exception e) {
					logger.error("保存银码头传过来的用户银行卡信息fail:" + e);
					responseData.setError("保存银码头传过来的用户银行卡信息fail");
				}
			}
		} else {
			logger.info("查询银行卡信息失败" + bankInfos.getMsg());
			responseData.error("查询银行卡信息失败");
		}
	}

	private void savePassword(YmtUserDto ymtUserDto, ResponseData responseData) throws Exception{
		// 保存新用户密码
		Password password = new Password();
		password.setPasswordType(1);
		password.setAccountNumber(ymtUserDto.getAccountNumber());
		password.setPassword(md5Util.getSecondMD5(ymtUserDto.getPassword()));
		logger.info("银码头传过来的密码参数：" + JSON.toJSONString(password));
		try {
			accountPassWordInfoService.saveAccountPassword(password);
			logger.info("保存银码头传过来的密码信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的密码信息fail：" + e);
			responseData.setError("保存银码头传过来的密码信息fail");
		}
		
	}

	private void saveAccount(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存账号信息：
		Account account = new Account();
		account.setUserId(userId);
		account.setAccountNumber(ymtUserDto.getAccountNumber());
		account.setIBankUserId(ymtUserDto.getIBankUserId()); // 关联银马头用户id
		account.setAccountType(10); // 账号类型

		if (StringUtils.isNotBlank(ymtUserDto.getLimitCredit().toString())) {
			account.setLimitCredit(ymtUserDto.getLimitCredit());
		} else {
			account.setLimitCredit(new BigDecimal(0));
		}

		if (StringUtils.isNotBlank(ymtUserDto.getUsableCredit().toString())) {
			account.setUsableCredit(ymtUserDto.getUsableCredit());
		} else {
			account.setUsableCredit(new BigDecimal(0));
		}

		if (StringUtils.isNotBlank(ymtUserDto.getUsedCredit().toString())) {
			account.setUsedCredit(ymtUserDto.getUsedCredit());
		} else {
			account.setUsedCredit(new BigDecimal(0));
		}

		if (StringUtils.isNotBlank(ymtUserDto.getProductCode())) {
			account.setProductCode(ymtUserDto.getProductCode());
		} else {
			account.setProductCode("cash");
		}
		if (StringUtils.isNotBlank(ymtUserDto.getSource())) {
			account.setSource(ymtUserDto.getSource());
		} else {
			account.setSource("nyd");
		}

		// 对于新增的用户渠道channel字段处理：
		account.setChannel(1); // 1为银码头

		if (StringUtils.isNotBlank(ymtUserDto.getAccount_type().toString())) {
			account.setAccountType(ymtUserDto.getAccount_type());
		} else {
			account.setAccountType(10);
		}
		logger.info("银码头传过来的用户账号信息：" + JSON.toJSONString(account));
		try {
			accountInfoService.saveYmtAccount(account);
			// 保存账户信息至reidis中
			accountInfoService.saveAccountInRedis(account, null);
			logger.info("保存银码头传过来的用户账号信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户账号信息fail:" + e);
			responseData.setError("保存银码头传过来的用户账号信息fail");
		}
	}
	private void updateAccountForReset(YmtUserDto ymtUserDto, String userId, ResponseData responseData) {
		// 保存账号信息：
		Account account = new Account();
		account.setUserId(userId);
		account.setAccountNumber(ymtUserDto.getAccountNumber());
		account.setIBankUserId(ymtUserDto.getIBankUserId()); // 关联银马头用户id
		account.setAccountType(10); // 账号类型
		
		if (StringUtils.isNotBlank(ymtUserDto.getLimitCredit().toString())) {
			account.setLimitCredit(ymtUserDto.getLimitCredit());
		} else {
			account.setLimitCredit(new BigDecimal(0));
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getUsableCredit().toString())) {
			account.setUsableCredit(ymtUserDto.getUsableCredit());
		} else {
			account.setUsableCredit(new BigDecimal(0));
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getUsedCredit().toString())) {
			account.setUsedCredit(ymtUserDto.getUsedCredit());
		} else {
			account.setUsedCredit(new BigDecimal(0));
		}
		
		if (StringUtils.isNotBlank(ymtUserDto.getProductCode())) {
			account.setProductCode(ymtUserDto.getProductCode());
		} else {
			account.setProductCode("cash");
		}
		if (StringUtils.isNotBlank(ymtUserDto.getSource())) {
			account.setSource(ymtUserDto.getSource());
		} else {
			account.setSource("nyd");
		}
		
		// 对于新增的用户渠道channel字段处理：
		account.setChannel(1); // 1为银码头
		
		if (StringUtils.isNotBlank(ymtUserDto.getAccount_type().toString())) {
			account.setAccountType(ymtUserDto.getAccount_type());
		} else {
			account.setAccountType(10);
		}
		logger.info("银码头传过来的用户账号信息：" + JSON.toJSONString(account));
		try {
			accountInfoService.updateAccountByUserId(account);
			// 保存账户信息至reidis中
			accountInfoService.saveAccountInRedis(account, null);
			logger.info("保存银码头传过来的用户账号信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户账号信息fail:" + e);
			responseData.setError("保存银码头传过来的用户账号信息fail");
		}
	}

	private void saveUser(YmtUserDto ymtUserDto, String userId,ResponseData responseData) {
		// 保存用户基本信息：
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setRealName(ymtUserDto.getRealName());
		userInfo.setIdNumber(ymtUserDto.getIdNumber());
		if (StringUtils.isNotBlank(ymtUserDto.getCertificateType())) {
			userInfo.setCertificateType(ymtUserDto.getCertificateType());
		} else {
			userInfo.setCertificateType("10");
		}
		String gender = Integer.parseInt(String.valueOf(ymtUserDto.getIdNumber().charAt(16))) % 2 == 0 ? "女"
				: "男";
		userInfo.setGender(gender);
		userInfo.setNation(ymtUserDto.getNation());
		userInfo.setBirth(ymtUserDto.getBirth());
		logger.info("银码头传过来的用户基本信息:" + JSON.toJSONString(userInfo));
		try {
			userService.saveUser(userInfo);
			logger.info("保存银码头传过来的用户基本信息success");
		} catch (Exception e) {
			logger.error("保存银码头传过来的用户基本信息fail");
			responseData.setError("保存银码头传过来的用户基本信息fail");
		}
		
	}

	/**
	 * 获取更新用户信息标识
	 * @param ymtUserDto
	 * @return
	 * @throws Exception
	 */
	private YmtUserSaveFlag getUserSaveFlag(YmtUserDto ymtUserDto) throws Exception{
		YmtUserSaveFlag flag = new YmtUserSaveFlag();
		// 判断该用户是否存在
		logger.info("用户的相关信息参数：" + ",身份证号码：" + ymtUserDto.getIdNumber() + ",手机号码：" + ymtUserDto.getAccountNumber());
		List<Account> accountList = null;
		List<UserInfo> userList = null;
		try {
			accountList = accountInfoService.findByAccountNumber(ymtUserDto.getAccountNumber());
		} catch (Exception e) {
			logger.error("查询账户信息异常：" + e.getMessage());
			throw e;
		}
		try {
			userList = userService.findByIdNumber(ymtUserDto.getIdNumber());
		} catch (Exception e) {
			logger.error("查询客户信息异常：" + e.getMessage());
			throw e;
		}
		if ((accountList == null || accountList.size() == 0) && (userList == null || userList.size() == 0)) {
			flag = new YmtUserSaveFlag(true,false);
		}else if((accountList != null && accountList.size() > 0) && (userList != null && userList.size() > 0)) {
			flag.setSaveUserDetail(true);
			flag.setSaveUserBank(true);
			flag.setSaveUserContract(true);
			flag.setSaveUserJob(true);
		}else if((accountList != null && accountList.size() > 0) && (userList == null || userList.size() == 0)){
			flag.setUpdateAccount(true);
			flag.setSaveUser(true);
			flag.setSaveUserDetail(true);
			flag.setSaveUserBank(true);
			flag.setSaveUserContract(true);
			flag.setSaveUserJob(true);
		}else if((accountList == null || accountList.size() == 0) && (userList != null && userList.size() > 0)) {
			/*if(StringUtils.isBlank(userList.get(0).getUserId())) {
				flag.setUpdateUser(true);
				flag.setSaveAccount(true);
				flag.setSaveAccountPassword(true);
				flag.setSaveUserDetail(true);
				flag.setSaveUserBank(true);
				flag.setSaveUserContract(true);
				flag.setSaveUserJob(true);
				return flag;
			}
			flag.setUpdateAccount(true);
			flag.setUpdateUser(true);
			flag.setUpdateAccountPassword(true);
			flag.setSaveUserDetail(true);
			flag.setSaveUserBank(true);
			flag.setSaveUserContract(true);
			flag.setSaveUserJob(true);*/
			flag.setResetAccountNumber(true);
		}
		return flag;
	}

}
