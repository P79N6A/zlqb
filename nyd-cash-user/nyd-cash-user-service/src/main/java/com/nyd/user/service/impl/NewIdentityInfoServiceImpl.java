package com.nyd.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.feature.rinse.api.FeatureRinse;
import com.creativearts.feature.rinse.consts.RinseDataType;
import com.creativearts.feature.rinse.dto.RinseMessageDto;
import com.creativearts.limit.api.AssessService;
import com.creativearts.limit.query.dto.AssessDto;
import com.creativearts.limit.query.dto.UserLimitDto;
import com.google.gson.Gson;
import com.nyd.activity.api.CashRedBagService;
import com.nyd.activity.model.ActivityCashDto;
import com.nyd.activity.model.ActivityFeeInfo;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.application.model.mongo.FilePDFInfo;
import com.nyd.dsp.api.JoContract;
import com.nyd.dsp.model.request.IdNumberMobileNameModel;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.zzl.OrderForOrderPayBackServise;
import com.nyd.order.model.JudgeInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.ContactDao;
import com.nyd.user.dao.JobDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.dao.UserDetailDao;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Contact;
import com.nyd.user.entity.Step;
import com.nyd.user.entity.User;
import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.IdentityInfo;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.NewStepInfo;
import com.nyd.user.model.PersonInfo;
import com.nyd.user.model.StepInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.enums.AttachmentType;
import com.nyd.user.model.request.NewAttachmentModel;
import com.nyd.user.service.NewIdentityInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.mq.UserToApplicationProducer;
import com.nyd.user.service.util.CommonUtil;
import com.nyd.user.service.util.DateUtil;
import com.nyd.user.service.util.MongoApi;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.model.BillInfo;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

@Service(value="newIdentityInfoService")
public class NewIdentityInfoServiceImpl implements NewIdentityInfoService{
	
	public static  Logger logger =LoggerFactory.getLogger(NewIdentityInfoServiceImpl.class);
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserDetailDao userDetailDao;
	
	@Autowired
	private StepDao stepDao;
	
	@Autowired
	private MongoApi mongoApi;
	
	@Autowired
	private  AgreeMentContract agreeMentContract;
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	private UserToApplicationProducer userToApplicationProducer;
		
	@Autowired(required = false)
	private FeatureRinse featureRinse;

    @Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired(required = false)
	private AssessService assessService;
	
	@Autowired
	private UserProperties userProperties;
	
	@Autowired
	private OrderContract orderContract;
	
	@Autowired(required = false)
    private JoContract joContract;
	
	@Autowired
	private CashRedBagService cashRedBagService;

    @Autowired
    RestTemplateApi restTemplateApi;
    
    @Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
    
    @Autowired
	private OrderForOrderPayBackServise orderForOrderPayBackServise;
    
	@Override
	@Transactional(rollbackFor=Exception.class)
	public ResponseData saveFaceIdentity(IdentityInfo identityInfo) throws Exception {
		if(StringUtils.isBlank(identityInfo.getAccountNumber())) {
			logger.info("面部识别 accountNumber is null");
			return ResponseData.error("accountNumber is null");
		}	
		if(StringUtils.isBlank(identityInfo.getFileJson())) {
			logger.info("面部识别 fileJson 图片信息 is null"+identityInfo.getAccountNumber());
			return ResponseData.error("fileJson is null");
		}
		 ResponseData responseData = ResponseData.success();
		 String accountNumber = identityInfo.getAccountNumber();
		 Account account = new Account();
         account.setAccountNumber(accountNumber);
         account.setFaceFlag(UserConsts.FILL_FLAG);
         try {
        	 accountDao.updateAccountByAccountNumber(account);   
         }catch(Exception e) {
        	 logger.error("update account error"+accountNumber,e);
        	 throw e;
         }       
         //照片保存及相应参数
         try {
        	 saveAttachmentNewByAccountNumber(accountNumber, identityInfo.getFileJson(), identityInfo.getJsonString(), identityInfo.getDelta());
         } catch (Exception e) {      	
             logger.error("save photo error! accountNumber = " + accountNumber, e);
             return  ResponseData.error(UserConsts.DB_ERROR_MSG);
         }           
		 return responseData;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public ResponseData saveIdentityInfo(IdentityInfo identityInfo) throws Exception{
		ResponseData responseData = ResponseData.success();
		if(formatAge(identityInfo)) {
			return ResponseData.error("依据法律法规，未成年人禁止借款");
		}
		if(StringUtils.isBlank(identityInfo.getFileJson())) {
			logger.info("身份证认证 fileJson 图片信息 is null"+identityInfo.getAccountNumber());
			return ResponseData.error("fileJson is null");
		}
		//第三方对接 暂时不用
//		if(!judgeJo(identityInfo)){
//	           return ResponseData.error(UserConsts.IDCARD_THREE_ERROR);
//	     }
		//先根据身份证号查询，如果有就是老户，没有时新户
        List<UserInfo> userList = null;
        try {
            logger.info("begin to get userInfo, idNumber is " + identityInfo.getIdNumber());
            userList = userDao.getUsersByIdNumber(identityInfo.getIdNumber());
        } catch (Exception e) {
        	logger.error("get userInfo error! userId = "+identityInfo.getUserId(),e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            return responseData;
        }
        if (userList != null && userList.size()>0) {
            logger.info("idNumber is already exist");
            //已存在用户
            responseData = ResponseData.error(UserConsts.IDCARD_ALREADY_REGISTER);
            return responseData;
        }else {//新户		
		 //使用大户的ID服务
        String userId = idGenerator.generatorId(BizCode.USER).toString();     
		 //计算性别
        String gender = Integer.parseInt(String.valueOf(identityInfo.getIdNumber().charAt(16))) % 2 == 0 ? "女" : "男";
        identityInfo.setGender(gender);
        identityInfo.setUserId(userId);
        //用户信息赋值
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(identityInfo,userInfo);
        //用户详细信息赋值
        UserDetailInfo detailInfo = new UserDetailInfo();
        BeanUtils.copyProperties(identityInfo,detailInfo);
        try {
            userDao.save(userInfo);
            userDetailDao.save(detailInfo);
        } catch (Exception e) {
            logger.error("save userInfo error! userId = "+userId,e); 
            throw e;
        }
        Account account = new Account();
        account.setAccountNumber(identityInfo.getAccountNumber());
        account.setUserId(userId);
        try {
            accountDao.updateAccountByAccountNumber(account);
            logger.info("save accountInfo success");
        } catch (Exception e) {
       	    logger.error("account bind user error! accountNumber = "+identityInfo.getAccountNumber(),e);
       	    throw e;
        }
        try {
            logger.info("beigin to getNewAttachmentModel,mobile is "+identityInfo.getAccountNumber());
            List<NewAttachmentModel> modelList =mongoApi.getNewAttachmentModel(identityInfo.getAccountNumber());
            for(NewAttachmentModel  newAttachmentModel :modelList ) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("_id", newAttachmentModel.get_id());
                map.put("userId", userId);
                map.put("accountNumber", newAttachmentModel.getAccountNumber());
                map.put("type", newAttachmentModel.getType());
                map.put("file", newAttachmentModel.getFile());
                map.put("fileName", newAttachmentModel.getFileName());
                mongoApi.upsert(map, "attachment");
            }
            logger.info("mongo update attachment success! mobile is "+identityInfo.getAccountNumber());
            } catch (Exception e) {
                logger.error("mongo update has exception! mobile is "+identityInfo.getAccountNumber(),e);
        }
            //照片保存及相应参数
        try {
            saveAttachmentNew(userId, identityInfo.getAccountNumber(),identityInfo.getFileJson(), identityInfo.getJsonString(), identityInfo.getDelta());
        } catch (Exception e) {
            logger.error("save photo error! userId = " + userId, e);
            return ResponseData.error(UserConsts.DB_ERROR_MSG);
        } 
        try {
            //更新信息完整度
       	    logger.info("begin to save stepInfo");
            Step step =new Step();
            step.setUserId(userId);
            step.setIdentityFlag(UserConsts.FILL_FLAG);
            stepDao.saveStep(step);
            logger.info("update stepInfo success");
        } catch (Exception e) {
       	    logger.error("save userInfo success，but save stepInfo failed ! userId= "+userId, e);
       	    return  ResponseData.error(UserConsts.DB_ERROR_MSG);
        }      
           
        //签署注册及隐私协议
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",userId);
            jsonObject.put("userName",identityInfo.getRealName());
            agreeMentContract.signRegisterAgreeMent(jsonObject);
        } catch (Exception e) {
            logger.error("signRegisterAgreeMent has error! userId is "+userId,e);
            return responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        BaseInfo baseInfo = new BaseInfo();
        List<AccountDto> accountList= accountDao.getAccountByuserId(userId);
        if(accountList != null && accountList.size() >0) {
        	AccountDto accountDto =accountList.get(0);
        	baseInfo.setAccountNumber(accountDto.getAccountNumber());       	
        }     
        baseInfo.setUserId(userId);
        responseData.setData(baseInfo);
		return responseData;
       }
	}
	
	private boolean formatAge(IdentityInfo identityInfo) {
		String idNumber = identityInfo.getIdNumber();
		if(StringUtils.isNotBlank(idNumber)) {
			String birthDay = idNumber.substring(6, 14);
			Date date1 = DateUtils.addYears(new Date(), -18);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dat = sdf.format(date1);
			if(birthDay.compareTo(dat) >= 0) {
				return true;
			}
		}
		return false;
	}
	/**
     * 批量保存图片信息
     *
     * @param userId
     * @param fileJson
     */
    private void saveAttachmentNew(String userId,String accountNumber, String fileJson, String jsonString, String delta) {
        Gson gson = new Gson();
        Map<String, String> fileMap = gson.fromJson(fileJson, Map.class);
        for (String key : fileMap.keySet()) {
            new Thread(() -> {
                logger.info("begin to save photo, userId is " + userId);
                //身份证正面照
                ThreadLocal<Map> attachmentThreadLocal = new ThreadLocal<Map>() {
                    @Override
                    protected Map initialValue() {
                        NewAttachmentModel attachmentModel = new NewAttachmentModel();
                        attachmentModel.setUserId(userId);
                        attachmentModel.setAccountNumber(accountNumber);
                        attachmentModel.setFile(fileMap.get(key));
                        attachmentModel.setType(AttachmentType.valueOf(key).getType());
                        attachmentModel.setFileName(AttachmentType.valueOf(key).getDescription());
                        Map<String, Object> map = CommonUtil.transBeantoMap(attachmentModel);
                        return map;
                    }
                };
                mongoApi.save(attachmentThreadLocal.get(), "attachment");
                logger.info("end to save photo, userId is " + userId);
            }).start();
        }
        if (!StringUtils.isBlank(delta) || !StringUtils.isBlank(jsonString)) {
            //存储与业务不相关的前端数据
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("userId", userId);
            extraMap.put("jsonString", jsonString);
            extraMap.put("delta", delta);
            extraMap.put("type", AttachmentType.EXTRA.getType());
            mongoApi.save(extraMap, "attachment");
        }
    }

	@Override
	public ResponseData savePersonInfo(PersonInfo personInfo) {
		ResponseData responseData = ResponseData.success();
		String userId = personInfo.getUserId();
		JobInfo jobInfo  = new JobInfo();
		BeanUtils.copyProperties(personInfo,jobInfo);

		//保存工作信息
		if(jobInfo !=null && UserConsts.FILL_FLAG.equals(jobInfo.getJobFlag())) {
			jobInfo.setUserId(userId);			
			Step step =new Step();
			step.setUserId(userId);
	        step.setJobFlag(UserConsts.FILL_FLAG);
			try {
				jobDao.save(jobInfo);
				stepDao.updateStep(step);
			} catch (Exception e) {
				logger.error("save jobInfo error! userId = "+personInfo.getUserId(),e);  
				return  ResponseData.error(UserConsts.DB_ERROR_MSG);
		     }
		 }
		ContactInfos contactInfo = new ContactInfos();
		BeanUtils.copyProperties(personInfo,contactInfo);
		//保存联系人信息
		if(contactInfo != null && UserConsts.FILL_FLAG.equals(contactInfo.getContactFlag())) {
			 List<Contact> contactList = getContact(contactInfo);
			 try {
	  	    	 Step step =new Step();
	  			 step.setUserId(userId);
	  	         step.setContactFlag(UserConsts.FILL_FLAG);
	             contactDao.saveList(contactList);
	             stepDao.updateStep(step);
	             logger.info("save contactInfo success");

	             //lee 调用第三方的接口；
                 Map<String, Object> param = new HashMap<String, Object>();
                 param.put("type", "moxie_carrier_report");
                 param.put("userId", userId);
                 logger.info("保存联系人，调接口 moxie_carrier_report： " + JSON.toJSONString(param));
                 ResponseData resp = restTemplateApi.postForObject(userProperties.getQueryFeatureRinseUserReRun(), param, ResponseData.class);
                 logger.info("保存联系人，调接口 moxie_carrier_report 结果：{} ", JSON.toJSONString(resp));

                 Map<String, Object> param2 = new HashMap<String, Object>();
                 param2.put("type", "moxie_carrier_originalInfo");
                 param2.put("userId", userId);
                 logger.info("保存联系人，调接口 moxie_carrier_originalInfo： " + JSON.toJSONString(param2));
                 ResponseData resp2 = restTemplateApi.postForObject(userProperties.getQueryFeatureRinseUserReRun(), param2, ResponseData.class);
                 logger.info("保存联系人，调接口 moxie_carrier_originalInfo 结果：{} ", JSON.toJSONString(resp));

             } catch (Exception e) {
	              logger.error("save contactInfo error! userId = "+contactInfo.getUserId(),e);
	              return ResponseData.error(UserConsts.DB_ERROR_MSG);
	          }
		}			      	 	   
	    UserDetailInfo detailInfo = new UserDetailInfo();
        BeanUtils.copyProperties(personInfo,detailInfo);
        //更新用户详细信息
        String personFlag = null;
		 if(detailInfo !=null && UserConsts.FILL_FLAG.equals(personInfo.getPersonFlag())) {			
			 try {
				 personFlag =personInfo.getPersonFlag();
				 userDetailDao.update(detailInfo);				
			 }catch(Exception e) {
				  logger.error("update userDetail error! userId = "+personInfo.getUserId(),e);  
			 } 
		 }	  		 	 
	    List<Step> stepList =new ArrayList<Step>();
		try {
			stepList = stepDao.selectByUserId(personInfo.getUserId());
		} catch (Exception e) {
			logger.info("select setp error"+personInfo.getUserId());
			return ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		if(stepList != null && stepList.size() >0) {
			Step step =stepList.get(0);
			if(UserConsts.FILL_FLAG.equals(step.getJobFlag()) && UserConsts.FILL_FLAG.equals(step.getContactFlag()) && UserConsts.FILL_FLAG.equals(personFlag)) {
				 getAssess(step.getUserId() ,step);//推送风控	
				 Step stepPersonInfo =new Step();
				 stepPersonInfo.setUserId(userId);
				 stepPersonInfo.setPreAuditFlag(step.getPreAuditFlag());
				 stepPersonInfo.setPreAuditLevel(step.getPreAuditFlag());
				 try {
					 userDetailDao.update(detailInfo);
					 stepDao.updateStep(stepPersonInfo);
					 insertActivityLog(personInfo);
				 }catch(Exception e) {
					  logger.error("update userDetail error! userId = "+personInfo.getUserId(),e); 
					  return ResponseData.error(UserConsts.DB_ERROR_MSG);
				 } 
		  }		
		}	
		 BaseInfo baseInfo = new BaseInfo();
	     baseInfo.setUserId(userId);
	     responseData.setData(baseInfo);
		 return responseData;
	}
	
	/**
     * 组装联系人信息
     * @param contactInfo
     * @return
     */
    private List<Contact> getContact(ContactInfos contactInfo){
        List<Contact> contactList = new ArrayList<Contact>();
        Contact directContact = new Contact();
        directContact.setUserId(contactInfo.getUserId());
        directContact.setType("direct");
        directContact.setName(contactInfo.getDirectContactName());
        directContact.setMobile(contactInfo.getDirectContactMobile());
        directContact.setRelationship(contactInfo.getDirectContactRelation());
        Contact majorContact = new Contact();
        majorContact.setUserId(contactInfo.getUserId());
        majorContact.setType("major");
        majorContact.setName(contactInfo.getMajorContactName());
        majorContact.setMobile(contactInfo.getMajorContactMobile());
        majorContact.setRelationship(contactInfo.getMajorContactRelation());
        contactList.add(directContact);
        contactList.add(majorContact);
        return contactList;
    }

    @Override
	public ResponseData saveMobileFlag(StepInfo stepInfo) {  	
        ResponseData responseData = ResponseData.success();
        if(stepInfo == null || StringUtils.isBlank(stepInfo.getUserId()) || StringUtils.isBlank(stepInfo.getMobileFlag())) {     	
        	logger.info("运营认证参数传入不完整");
        	return  ResponseData.error("参数不完整");
        }
        Date identifMobileTime = null;//上一次运营商认证时间
        try {
			List<Step> stepList = stepDao.selectByUserId(stepInfo.getUserId());
			if(stepList != null && stepList.size() >0) {
				identifMobileTime =stepList.get(0).getMobileTime();
			}
		} catch (Exception e1) {
			logger.info("select setp error"+stepInfo.getUserId());
		}
        int authEffectiveTime = Integer.valueOf(userProperties.getAuthEffectiveTime()); 
        Step step = new Step();
        //运营商过期认证
        BeanUtils.copyProperties(stepInfo, step);  
        if(DateUtil.getDays(identifMobileTime, new Date()) > authEffectiveTime && UserConsts.FILL_FLAG.equals(stepInfo.getMobileFlag())) {
        	logger.info("运营商认证过期，重新认证"+step.getUserId());
        	getAssess(step.getUserId() ,step);//推送风控	
        	updateOperatorInfo(step);
        	return responseData;
        }
        //用户首次运营商认证                         
        if (UserConsts.FILL_FLAG.equals(stepInfo.getMobileFlag())) {
        	 //通知风控
            if (noticeFeature(stepInfo.getUserId())) {
                logger.info("notify to feature success! userId is "+stepInfo.getUserId());
            }          
            updateOperatorInfo(step); 
        }                   
       
        return responseData;
	}
    
    
    //update t_user_step
    private void updateOperatorInfo(Step step) {
    	step.setMobileFlag(UserConsts.FILL_FLAG);
        step.setMobileTime(new Date());
        try {
            stepDao.updateStep(step);
            logger.info("update stepInfo success");
        } catch (Exception e) {
        	logger.error("update stepInfo error! userId = " + step.getUserId(), e);         
        }
    }
    
    
    /**
     * 批量保存图片信息
     *
     * @param fileJson
     */
    private void saveAttachmentNewByAccountNumber(String accountNumber, String fileJson, String jsonString, String delta) {
        Gson gson = new Gson();
        Map<String, String> fileMap = gson.fromJson(fileJson, Map.class);
        for (String key : fileMap.keySet()) {
            new Thread(() -> {
                logger.info("begin to save photo, accountNumber is " + accountNumber);          
                ThreadLocal<Map> attachmentThreadLocal = new ThreadLocal<Map>() {
                    @Override
                    protected Map initialValue() { 
                        NewAttachmentModel attachmentModel = new NewAttachmentModel();
                        attachmentModel.setAccountNumber(accountNumber);
                        attachmentModel.setFile(fileMap.get(key));
                        attachmentModel.setType(AttachmentType.valueOf(key).getType());
                        attachmentModel.setFileName(AttachmentType.valueOf(key).getDescription());
                        Map<String, Object> map = CommonUtil.transBeantoMap(attachmentModel);
                        return map;
                    }
                };
                mongoApi.save(attachmentThreadLocal.get(), "attachment");
                logger.info("end to save photo, accountNumber is " + accountNumber);
            }).start();
        }
        if (!StringUtils.isBlank(delta) || !StringUtils.isBlank(jsonString)) {
            //存储与业务不相关的前端数据
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("accountNumber", accountNumber);
            extraMap.put("jsonString", jsonString);
            extraMap.put("delta", delta);
            extraMap.put("type", AttachmentType.EXTRA.getType());
            mongoApi.save(extraMap, "attachment");
        }
    }
    
  //认证完成，调接口、记录是否认证、评级信息
    public void getAssess(String userId, Step step) {
        try {
            if (step==null||"0".equals(step.getContactFlag())||"0".equals(step.getJobFlag())||"0".equals(step.getMobileFlag())||"0".equals(step.getIdentityFlag())) {
                logger.info("资料未填写完成不发送风控前置！userId is "+userId);
                return;
            }
            UserLimitDto userLimitDto = new UserLimitDto();
            userLimitDto.setProductType("nyd");
            userLimitDto.setUserId(userId);
           /* userLimitDto.setAppName(appName);
            if("wwj".equals(appName)) {
            	userLimitDto.setAppName("wwj");
            }else if(appName == null || appName == "" ){
            	userLimitDto.setAppName("nyd");
            }*/
            ResponseData<AssessDto> assessDtoResponseData = assessService.getassess(userLimitDto);
            logger.info("assessService response is " + JSON.toJSONString(assessDtoResponseData));
            if (assessDtoResponseData != null && "0".equals(assessDtoResponseData.getStatus())) {
                AssessDto assessDto = assessDtoResponseData.getData();
                if (!"".equals(assessDto.getAssessScore()) && assessDto.getAssessScore() != null) {
//                当有分的时候才说明风控前置走完，mq发消息至application生成PDF文档
                    try {
                        FilePDFInfo filePDFInfo = new FilePDFInfo();
                        filePDFInfo.setUserId(userLimitDto.getUserId());
                        //qiniuContractNyd.saveFilePDFInfo(userLimitDto.getUserId());
                        userToApplicationProducer.sendMsg(filePDFInfo);
                        step.setPreAuditFlag("1");
                    } catch (Exception e) {
                        logger.error("send msg to application auditpdf has exception!", e);
                    }
                }
                step.setPreAuditLevel(assessDto.getAssessLevel());
            }
        } catch (Exception e) {
            logger.error(" assessService getAssess error! userId = " + userId, e);
        }
    }
    
    
    //通知风控
    private boolean noticeFeature(String userId) {
        boolean flag = false;
        try {
            RinseMessageDto carrierOriginal = new RinseMessageDto();
            carrierOriginal.setType(RinseDataType.CarrierOriginal);
            carrierOriginal.setUserId(userId);
            featureRinse.resetGrayData(carrierOriginal);
            RinseMessageDto carrierReport = new RinseMessageDto();
            carrierReport.setType(RinseDataType.CarrierReport);
            carrierReport.setUserId(userId);
            featureRinse.resetGrayData(carrierReport);
            flag = true;
        } catch (Exception e) {
            logger.error("resetGrayData has exception! userId is "+userId,e);
        }
        return flag;
    }

	@Override
    @Transactional(rollbackFor=Exception.class)
	public ResponseData getStepInfo(String accountNumber,String appName) {
		 ResponseData responseData = ResponseData.success();
		 List<AccountDto> accountList = new ArrayList<AccountDto>();
		 try {
			accountList = accountDao.findByAccountNumber(accountNumber);
		 } catch (Exception e) {
			 logger.info("get account error"+accountNumber);
			 return ResponseData.error(UserConsts.DB_ERROR_MSG);
		 }
		 String userId =null;
		 String faceFlag=null;
		 if(accountList != null && accountList.size() > 0) {
			 AccountDto account = accountList.get(0);
			 userId =account.getUserId();
			 faceFlag=account.getFaceFlag();
		 }
		 NewStepInfo stepInfo = new NewStepInfo();
		 //如userId为空，则是个新用户
		 if(StringUtils.isBlank(userId)) {			
			 stepInfo.setFaceFlag(faceFlag);			 
			 stepInfo.setIdentityFlag(UserConsts.UNFILL_FLAG);
			 stepInfo.setMobileFlag(UserConsts.UNFILL_FLAG);
			 stepInfo.setPersonInfoFlag(UserConsts.UNFILL_FLAG);
			 stepInfo.setJobFlag(UserConsts.UNFILL_FLAG);
			 stepInfo.setContactFlag(UserConsts.UNFILL_FLAG);
			 //绑卡标识;
			 stepInfo.setBindCardFlag(UserConsts.UNFILL_FLAG);
			 responseData.setData(stepInfo);
			 return responseData;
		}		 	  
		List<UserInfo> userList = new ArrayList<UserInfo>(); 
		try {
			 userList =userDao.getUsersByUserId(userId);
		} catch (Exception e1) {		
			 logger.info("get user error"+e1);
			 return ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		if(userList != null && userList.size() >0) {
			UserInfo userInfo =userList.get(0);
			stepInfo.setIdNumber(userInfo.getIdNumber());
			stepInfo.setUserName(userInfo.getRealName());
		}	
		List<UserDetailInfo> detailList = new ArrayList<UserDetailInfo>();
		String highestDegree = null;//最高学历
		String maritalStatus = null;//婚姻
		String livingAddress = null;//地址
		try {
			detailList = userDetailDao.getUserDetailsByUserId(userId);
			if(detailList != null && detailList.size() > 0) {
				highestDegree =detailList.get(0).getHighestDegree();
				maritalStatus =detailList.get(0).getMaritalStatus();
				livingAddress =detailList.get(0).getLivingAddress();
			}
		} catch (Exception e1) {
			logger.info("select userDetail error"+userId);
		}			
        List<Step> stepList = null;
        try {
            stepList = stepDao.getStepByUserId(userId);
        } catch (Exception e2) {
            logger.error("get stepInfo error! userId = " + userId, e2);
            return ResponseData.error(UserConsts.DB_ERROR_MSG);
        }          
        if (stepList != null && stepList.size() > 0) {
            Step step = stepList.get(0);
            if(UserConsts.FILL_FLAG.equals(step.getJobFlag())) {
            	step.setJobFlag(UserConsts.FILL_FLAG);
            }
            //绑定银行卡的判断
            if(UserConsts.FILL_FLAG.equals(step.getBankFlag())) {
                stepInfo.setBindCardFlag(UserConsts.FILL_FLAG);
            }else{
                stepInfo.setBindCardFlag(UserConsts.UNFILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(step.getMobileFlag())) {
                stepInfo.setMobileFlag(UserConsts.FILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(step.getContactFlag())) {
            	step.setContactFlag(UserConsts.FILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(faceFlag)) {
            	stepInfo.setFaceFlag(UserConsts.FILL_FLAG);
            }else {
            	stepInfo.setFaceFlag(UserConsts.UNFILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(step.getIdentityFlag())) {
            	step.setIdentityFlag(UserConsts.FILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(step.getJobFlag()) && UserConsts.FILL_FLAG.equals(step.getContactFlag()) 
            	&&	StringUtils.isNotBlank(highestDegree) && StringUtils.isNotBlank(maritalStatus) && StringUtils.isNotBlank(livingAddress)) {
            	stepInfo.setPersonInfoFlag(UserConsts.FILL_FLAG);
            }else {
            	stepInfo.setPersonInfoFlag(UserConsts.UNFILL_FLAG);
            }
            if(UserConsts.FILL_FLAG.equals(step.getMobileFlag())) {
            	step.setMobileFlag(UserConsts.FILL_FLAG);
            }
            int authEffectiveTime = Integer.valueOf(userProperties.getAuthEffectiveTime());          
            if (UserConsts.FILL_FLAG.equals(step.getMobileFlag()) && DateUtil.getDays(step.getMobileTime(), new Date()) > authEffectiveTime) {
                step.setMobileFlag(UserConsts.OPERATOR_OVERDUE);//运营商认证过期设置为2
                stepInfo.setWhetherScore("1");//完整度是否满足借款
                stepInfo.setWhetherScoreMsg(UserConsts.AUTH_EXPIRED_MSG);
                Step stepOperator = new Step();
                stepOperator.setUserId(userId);
                try {
					stepDao.updateStep(stepOperator);
				} catch (Exception e) {
					logger.info("update step error"+userId);
				}
            } 
            BeanUtils.copyProperties(step, stepInfo);         
            //判断是否可以借款
            try {
                ResponseData<JudgeInfo> judgeResponse = orderContract.judgeBorrowNew(userId,appName);
                if ("0".equals(judgeResponse.getStatus())) {
                    JudgeInfo judgeInfo = judgeResponse.getData();
                    stepInfo.setWhetherLoan(judgeInfo.getWhetherLoan());
                    stepInfo.setWhetherLoanMsg(judgeInfo.getWhetherLoanMsg());
                    stepInfo.setOrderFlag(judgeInfo.getOrderFlag());
                }
            } catch (Exception e3) {
                logger.error("judge whether Borrow error! userId = " + userId, e3);
                //0可以借款，1不可借款
                stepInfo.setWhetherLoan("1");
                stepInfo.setWhetherLoanMsg(UserConsts.DB_ERROR_MSG);
            }
            //是否有过借款
            ResponseData<List<OrderInfo>> orderData= orderContract.getOrdersByUserId(userId);
            if("0".equals(orderData.getStatus())) {
            	if(orderData.getData()!=null && orderData.getData().size() >0) {
            		stepInfo.setFirstLoanFlag("1");
            	}else {
            		stepInfo.setFirstLoanFlag("0");
            	}         	
            }
            
            try {
            	ResponseData<OrderInfo> orderResp = orderForOrderPayBackServise.checkOrderSuccess(userId);
            	if("0".equals(orderResp.getStatus())){
            		BillInfo bill = zeusForOrderPayBackServise.queryBillByOrderNo(orderResp.getData().getOrderNo());
            		if(!ChkUtil.isEmpty(bill))
            			stepInfo.setBillStatus(bill.getBillStatus());
            	}
			} catch (Exception e) {
				logger.info("get bill error" + userId);
			}
        }           
        responseData.setData(stepInfo);
		return responseData;
	}

	@Override
	public ResponseData getCashList(BaseInfo baseInfo) {
		ResponseData responseData = ResponseData.success();
		ActivityCashDto dto = new ActivityCashDto();
		try {
			dto =cashRedBagService.getCashList(baseInfo.getUserId());
		}catch(Exception e) {
			logger.info("调用activity 服务 error"+baseInfo.getUserId());
			responseData =ResponseData.error();
		}		
		responseData.setData(dto);
		return responseData;
	}
	
	
	private void  insertActivityLog(PersonInfo personInfo) {
		ActivityFeeInfo activityFee = new ActivityFeeInfo();					 		
		activityFee.setAccountNumber(personInfo.getAccountNumber());
		activityFee.setUserId(personInfo.getUserId());
		activityFee.setActivityType("newUserAward");//新用户完整资料奖励
		activityFee.setActivityMoney("38");//38元现金红包
		activityFee.setActivityLimitTime(null);
		activityFee.setUseFlag("0");
		activityFee.setMarks("新用户完善资料奖励");
		activityFee.setAppName("nyd");
		activityFee.setDeleteFlag("0");
		try {
			cashRedBagService.insertActivityLog(activityFee);
		}catch(Exception e) {
			logger.info("save t_activity_log tabel error"+e);
		}
	}
	
	 /**
     * 三要素验证
     * @param identityInfo
     * @return boolean
     */
    private boolean judgeJo(IdentityInfo identityInfo){
        logger.info("begin to judgeJo, accountNumber is " + identityInfo.getAccountNumber());
        if (!"ON".equals(userProperties.getJoVerifyFalg())){
            return true;
        }
        boolean judge = false;
        IdNumberMobileNameModel model = new IdNumberMobileNameModel();
        model.setAppId(identityInfo.getAccountNumber());
        model.setName(identityInfo.getRealName());
        model.setIdNumber(identityInfo.getIdNumber());
        model.setMobile(identityInfo.getAccountNumber());
        ResponseData<String> response = joContract.verify(model);
        logger.info("judgeJo response is "+ JSONObject.toJSONString(response));
        if("0".equals(response.getStatus())) {
            String flag = response.getData();
            if("0".equals(flag)){//集奥 0 代表一致
                judge = true;
            }
        }
        logger.info("judgeJo result is " + judge);
        return judge;
    }


}
