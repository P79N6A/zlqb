package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.das.query.api.model.ReportType;
import com.creativearts.feature.rinse.api.FeatureRinse;
import com.creativearts.feature.rinse.consts.RinseDataType;
import com.creativearts.feature.rinse.dto.RinseMessageDto;
import com.creativearts.limit.api.AssessService;
import com.creativearts.limit.query.dto.AssessDto;
import com.creativearts.limit.query.dto.UserLimitDto;
import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.mongo.FilePDFInfo;
import com.nyd.member.api.MemberContract;
import com.nyd.member.model.MemberModel;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.JudgeInfo;
import com.nyd.user.api.UserStepContract;
import com.nyd.user.dao.StepDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.dao.mapper.AccountMapper;
import com.nyd.user.dao.mapper.UserStepMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Step;
import com.nyd.user.model.StepInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.enums.StepScore;
import com.nyd.user.service.StepInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.mq.UserToApplicationProducer;
import com.nyd.user.service.util.DateUtil;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 17/11/4.
 */
@Service(value = "userStepContractNyd")
public class StepInfoServiceImpl implements StepInfoService, UserStepContract {
    private static Logger LOGGER = LoggerFactory.getLogger(StepInfoServiceImpl.class);

    @Autowired
    private StepDao stepDao;
    @Autowired(required = false)
    private OrderContract orderContract;
    @Autowired
    private UserProperties userProperties;
    @Autowired(required = false)
    private AssessService assessService;
    @Autowired
    private UserToApplicationProducer userToApplicationProducer;
    @Autowired(required = false)
    private QiniuContract qiniuContractNyd;
    @Autowired(required = false)
    private com.creativearts.das.query.api.QiniuContract qiniuContract;
    @Autowired(required = false)
    private MemberContract memberContract;
    @Autowired
    private UserDao userDao;
    @Autowired(required = false)
    private FeatureRinse featureRinse;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserStepMapper userStepMapper;

    /**
     * 更新资料完整度
     * 资料完整度达标后进行用户认证资料的保存并进行获取用户评估报告
     *
     * @param stepInfo
     * @return
     */
    @Override
    public ResponseData updateStepInfo(StepInfo stepInfo) {
        LOGGER.info("begin to update stepInfo, userId is " + stepInfo.getUserId());
        ResponseData responseData = ResponseData.success();
        try {
            Step step = new Step();
            BeanUtils.copyProperties(stepInfo, step);
            if (UserConsts.FILL_FLAG.equals(stepInfo.getZmxyFlag())) {
                step.setZmxyFlag(UserConsts.FILL_FLAG);
            }
            if (UserConsts.FILL_FLAG.equals(stepInfo.getMobileFlag())) {
                //通知风控
                if (noticeFeature(stepInfo.getUserId())) {
                    LOGGER.info("notify to feature success! userId is "+stepInfo.getUserId());
                }
                getAssess(stepInfo.getUserId(), step);
                step.setMobileFlag(UserConsts.FILL_FLAG);
                step.setMobileTime(new Date());
            }
            if (UserConsts.FILL_FLAG.equals(stepInfo.getTbFlag())) {
                step.setTbFlag(UserConsts.FILL_FLAG);
                step.setTbTime(new Date());
            }
            if (UserConsts.FILL_FLAG.equals(stepInfo.getOnlineBankFlag())) {
                step.setOnlineBankFlag(UserConsts.FILL_FLAG);
            }
            if (UserConsts.FILL_FLAG.equals(stepInfo.getGxbFlag())) {
                step.setGxbFlag(UserConsts.FILL_FLAG);
                step.setGxbTime(new Date());
            }
            try {
                stepDao.updateStep(step);
                LOGGER.info("update stepInfo success");
            } catch (Exception e) {
                responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
                LOGGER.error("update stepInfo error! userId = " + stepInfo.getUserId(), e);
            }
        } catch (BeansException e) {
            LOGGER.error("updateStepInfo has exception! request param is " + stepInfo.toString(), e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }


    //认证完成，调接口、记录是否认证、评级信息
    public void getAssess(String userId, Step step) {
        try {
            if (step==null||"0".equals(step.getContactFlag())||"0".equals(step.getJobFlag())||"0".equals(step.getMobileFlag())||"0".equals(step.getIdentityFlag())) {
                LOGGER.info("资料未填写完成不发送风控前置！userId is "+userId);
                return;
            }
            UserLimitDto userLimitDto = new UserLimitDto();
            userLimitDto.setProductType("nyd");
            userLimitDto.setUserId(userId);
            /*userLimitDto.setAppName(appName);
            if("wwj".equals(appName)) {
            	userLimitDto.setAppName("wwj");
            }else if(appName == null || appName == "" ){
            	userLimitDto.setAppName("nyd");
            }*/
            ResponseData<AssessDto> assessDtoResponseData = assessService.getassess(userLimitDto);
            LOGGER.info("assessService response is " + JSON.toJSONString(assessDtoResponseData));
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
                        LOGGER.error("send msg to application auditpdf has exception!", e);
                    }
                }
                step.setPreAuditLevel(assessDto.getAssessLevel());
            }
        } catch (Exception e) {
            LOGGER.error(" assessService getAssess error! userId = " + userId, e);
        }
    }


    //认证完成，调接口、记录是否认证、评级信息
    public void getAssessForAssessTask(String userId, Step step) {
        try {
            UserLimitDto userLimitDto = new UserLimitDto();
            userLimitDto.setProductType("nyd");
            userLimitDto.setUserId(userId);
            ResponseData<AssessDto> assessDtoResponseData = assessService.getassess(userLimitDto);
            LOGGER.info("assessService response is " + JSON.toJSONString(assessDtoResponseData));
            if (assessDtoResponseData != null && "0".equals(assessDtoResponseData.getStatus())) {
                AssessDto assessDto = assessDtoResponseData.getData();
                if (!"".equals(assessDto.getAssessScore()) && assessDto.getAssessScore() != null) {
//                当有分的时候才说明风控前置走完，mq发消息至application生成PDF文档
                    try {
                        FilePDFInfo filePDFInfo = new FilePDFInfo();
                        filePDFInfo.setUserId(userLimitDto.getUserId());
                        qiniuContractNyd.saveFilePDFInfo(userLimitDto.getUserId());
                        //userToApplicationProducer.sendMsg(filePDFInfo);
                        step.setPreAuditFlag("1");
                    } catch (Exception e) {
                        LOGGER.error("send msg to application auditpdf has exception!", e);
                    }
                }
                step.setPreAuditLevel(assessDto.getAssessLevel());
            }
        } catch (Exception e) {
            LOGGER.error(" assessService getAssess error! userId = " + userId, e);
        }
    }

    /**
     * 获取资料完整度信息
     * 判断资料提供是否完整
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData<StepInfo> getStepScore(String userId,String appName) {
        LOGGER.info("begin to get stepScore, userId is " + userId+" ,appName is "+appName);
        ResponseData responseData = ResponseData.success();
        if (userId == null) {
            return responseData;
        }
        List<Step> stepList = null;
        try {
            stepList = stepDao.getStepByUserId(userId);
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get stepInfo error! userId = " + userId, e);
            return responseData;
        }
        StepInfo stepInfo = new StepInfo();
        if (stepList != null && stepList.size() > 0) {
            Step step = stepList.get(0);

            int authEffectiveTime = Integer.valueOf(userProperties.getAuthEffectiveTime());
            int expiredCount = 0;
            if (UserConsts.FILL_FLAG.equals(step.getMobileFlag()) && DateUtil.getDays(step.getMobileTime(), new Date()) > authEffectiveTime) {
                step.setMobileFlag(UserConsts.UNFILL_FLAG);
                expiredCount++;
            }
            if (UserConsts.FILL_FLAG.equals(step.getTbFlag()) && DateUtil.getDays(step.getTbTime(), new Date()) > authEffectiveTime) {
                step.setTbFlag(UserConsts.UNFILL_FLAG);
            }
            if (UserConsts.FILL_FLAG.equals(step.getGxbFlag()) && DateUtil.getDays(step.getGxbTime(), new Date()) > authEffectiveTime) {
                step.setGxbFlag(UserConsts.UNFILL_FLAG);
            }

            //计算分数
            Integer stepScore = calculateScore(step);
            BeanUtils.copyProperties(step, stepInfo);
            stepInfo.setAuthFlag(UserConsts.UNFILL_FLAG);
            //运营商认证，authFalg才赋值1
            if ( UserConsts.FILL_FLAG.equals(step.getMobileFlag())) {
                stepInfo.setAuthFlag(UserConsts.FILL_FLAG);
            }
            stepInfo.setStepScore(String.valueOf(stepScore));
            if (stepScore >= Integer.parseInt(userProperties.getMinScore())) {
                //0可以借款，1不可借款
                stepInfo.setWhetherScore("0");
            } else {
                stepInfo.setWhetherScore("1");
            }
//            //判断还款计划表是否在规定时间内
//            boolean overdueAllow=false;
//            ResponseData<BillInfo> overdueRes=billContract.getBillInfoByUid(userId);
//            if (StringUtils.equals(overdueRes.getStatus(),"0")){
//                BillInfo billInfo=overdueRes.getData();
//                if (null!=billInfo && null!=billInfo.getPromiseRepaymentDate() && null!=billInfo.getActualSettleDate()){
//                    int diffDays= DateUtil.getDays(billInfo.getPromiseRepaymentDate(),billInfo.getActualSettleDate());
//                    if (diffDays>0 && diffDays<Integer.valueOf(userProperties.getOrderOverdueTime())){
//                        overdueAllow=true;
//                    }
//                }
//            }
//            //是否在逾期允许范围内
//            if (overdueAllow){
//                stepInfo.setWhetherScore("2");
//                stepInfo.setWhetherScoreMsg(UserConsts.AUTH_EXPIRED_MSG);
//            }
            //信用认证过期大于0次
            if (expiredCount > 0) {
                stepInfo.setWhetherScore("1");
                stepInfo.setWhetherScoreMsg(UserConsts.AUTH_EXPIRED_MSG);
            }

            //判断是否可借款
            try {
                ResponseData<JudgeInfo> judgeResponse = orderContract.judgeBorrowNew(userId,appName);
                if ("0".equals(judgeResponse.getStatus())) {
                    JudgeInfo judgeInfo = judgeResponse.getData();
                    stepInfo.setWhetherLoan(judgeInfo.getWhetherLoan());
                    stepInfo.setWhetherLoanMsg(judgeInfo.getWhetherLoanMsg());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("judge whether Borrow error! userId = " + userId, e);
                //0可以借款，1不可借款
                stepInfo.setWhetherLoan("1");
                stepInfo.setWhetherLoanMsg(UserConsts.DB_ERROR_MSG);
            }

            try {
                List<UserInfo> list = userDao.getUsersByUserId(userId);
                if (list!=null&&list.size()>0) {
                    UserInfo userInfo = list.get(0);
                    if (userInfo!=null) {
                        stepInfo.setUserName(userInfo.getRealName());
                        stepInfo.setIdNumber(userInfo.getIdNumber());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("getUsersByUserId has exception ! userId is "+userId,e);
            }
        } else {
            LOGGER.info("get stepInfo is null from db");
            stepInfo.setWhetherScore("1");
        }
        responseData.setData(stepInfo);
        LOGGER.info("get stepInfo is " + JSON.toJSONString(stepInfo));
        return responseData;
    }


    /**
     * 资料提交完成从机器人页面跳转至展示评估分和评估报告按钮
     * 获取评估分以及PDF路径
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData getStepScoreInfo(String userId) {
        LOGGER.info("begin to getStepScoreInfo, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        if (userId == null || "".equals(userId)) {
            responseData = ResponseData.error(UserConsts.PARAM_ERROR);
            return responseData;
        }
        String preAuditLevel = null;
        Step step = new Step();
        UserLimitDto userLimitDto = new UserLimitDto();
        userLimitDto.setProductType("nyd");
        userLimitDto.setUserId(userId);
        String assessLevel = null;
        String assessScore = null;
        //dubbo服务获取当前客户等级
        try {
            ResponseData<AssessDto> assessDtoResponseData = assessService.queryUserLastAssess(userLimitDto);
            if ("0".equals(assessDtoResponseData.getStatus())) {
                AssessDto assessDto = assessDtoResponseData.getData();
                assessLevel = assessDto.getAssessLevel();
                assessScore = assessDto.getAssessScore();
            }
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error(" assessService getAssess error! userId = " + userId, e);
        }
        String url = null;
        Map map = new HashMap();
        //获取客户PDF路径
        try {
            ResponseData qiniuRes = qiniuContract.downloadFilePDFInfo(userId, ReportType.FQZ);
            if (qiniuRes!=null&&"0".equals(qiniuRes.getStatus())) {
                url = String.valueOf(qiniuRes.getData());
                if(StringUtils.isBlank(url)||"null".equalsIgnoreCase(url)){
                    ResponseData qiniuContractResponseData = qiniuContractNyd.downloadFilePDFInfo(userId);
                    if (qiniuContractResponseData != null && "0".equals(qiniuContractResponseData.getStatus())) {
                        url = String.valueOf(qiniuContractResponseData.getData());
                    }
                }
            } else {
                ResponseData qiniuContractResponseData = qiniuContractNyd.downloadFilePDFInfo(userId);
                if (qiniuContractResponseData != null && "0".equals(qiniuContractResponseData.getStatus())) {
                    url = String.valueOf(qiniuContractResponseData.getData());
                }
            }
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("qiniuContractNyd get pdf error! userId = " + userId, e);
            return responseData;
        }
        //获取客户当前信息
        String consumeFlag = null;
        try {
            List<Step> stepList = stepDao.getStepByUserId(userId);
            if (stepList != null && stepList.size() > 0) {
                preAuditLevel = stepList.get(0).getPreAuditLevel();
                if (assessLevel != null && !assessLevel.equals(preAuditLevel)) {
                    try {
                        step.setPreAuditLevel(assessLevel);
                        step.setUserId(userId);
                        stepDao.updateStep(step);
                    } catch (Exception e) {
                        responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
                        LOGGER.error("update stepPreAuditLevel error! userId = " + userId, e);
                        return responseData;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("get userStepInfo error! userId = " + userId, e);
        }
        map.put("preAuditLevel", assessLevel);
        map.put("assessScore", assessScore);
        map.put("url", url);
        LOGGER.info("get  level  is " + assessLevel + "and  score is " + assessScore);
        responseData.setData(map);
        return responseData;
    }


    /**
     * 计算资料完整度分数
     *
     * @param step
     * @return
     */
    private int calculateScore(Step step) {
        int stepScore = 0;
        if (step == null) {
            return stepScore;
        }
        //身份证
        if (UserConsts.FILL_FLAG.equals(step.getIdentityFlag())) {
            stepScore += StepScore.IDENTITY_WEIGHT.getScore();
        }
        //工作信息
        if (UserConsts.FILL_FLAG.equals(step.getJobFlag())) {
            stepScore += StepScore.JOB_WEIGHT.getScore();
        }
        //联系人
        if (UserConsts.FILL_FLAG.equals(step.getContactFlag())) {
            stepScore += StepScore.CONTACT_WEIGHT.getScore();
        }
        //淘宝
        if (UserConsts.FILL_FLAG.equals(step.getTbFlag())) {
            stepScore += StepScore.TAOBAO_WEIGHT.getScore();
        }
        //运营商
        if (UserConsts.FILL_FLAG.equals(step.getMobileFlag())) {
            stepScore += StepScore.MOBILE_WEIGHT.getScore();
        }
        //公信宝
        if (UserConsts.FILL_FLAG.equals(step.getGxbFlag())) {
            stepScore += StepScore.GXB_WEIGHT.getScore();
        }
        return stepScore;
    }


    /**
     * 判断用户报告是否生成完毕,告知前端机器人页面可以跳转
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData<StepInfo> getUserStep(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<Step> stepList = stepDao.getStepByUserId(userId);
            if (stepList != null && stepList.size() > 0) {
                Step step = stepList.get(0);
                if ("0".equals(step.getPreAuditFlag())) {
                    LOGGER.info("begin to getAssess!!!");
                    getAssess(userId, step);
                    try {
                        stepDao.updateStep(step);
                        LOGGER.info("update stepInfo success");
                    } catch (Exception e) {
                        responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
                        LOGGER.error("update stepInfo error! userId = " + userId, e);
                    }
                }
                StepInfo stepInfo = new StepInfo();
                BeanUtils.copyProperties(step, stepInfo);
                responseData.setData(stepInfo);
                return responseData;
            }
        } catch (Exception e) {
            LOGGER.error("getStepByUserId has exception! ", e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            return responseData;
        }
        return responseData;
    }

    /**
     * 为老用户处理报告
     * @param userId
     * @return
     */


    @Override
    public ResponseData<StepInfo> doAssessTask(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<Step> stepList = stepDao.getStepByUserId(userId);
            if (stepList != null && stepList.size() > 0) {
                Step step = stepList.get(0);
                if ("0".equals(step.getPreAuditFlag())) {
                    LOGGER.info("begin to getAssess!!!");
                    getAssessForAssessTask(userId, step);
                    try {
                        stepDao.updateStep(step);
                        LOGGER.info("update stepInfo success");
                    } catch (Exception e) {
                        responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
                        LOGGER.error("update stepInfo error! userId = " + userId, e);
                    }
                }
                StepInfo stepInfo = new StepInfo();
                BeanUtils.copyProperties(step, stepInfo);
                responseData.setData(stepInfo);
                return responseData;
            }
        } catch (Exception e) {
            LOGGER.error("getStepByUserId has exception! ", e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            return responseData;
        }
        return responseData;
    }


    @Override
    public ResponseData getAssessReport(String userId) {
        LOGGER.info("begin to getAssessReport, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        String url = null;
        String ifAssess = null;
        String expireTime = null;
        Map map = new HashMap();
        try {
            ResponseData<MemberModel> memberResponse = memberContract.getMember(userId);
            if ("0".equals(memberResponse.getStatus()) && memberResponse.getData() != null) {
                MemberModel model = memberResponse.getData();
                if (new Date().before(model.getExpireTime())) {
                    ifAssess = "1";
                } else {
                    ifAssess = "2";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                expireTime = sdf.format(model.getExpireTime());
                map.put("expireTime", expireTime);
            } else {
                ifAssess = "0";
            }
        } catch (Exception e) {
            LOGGER.error("getStepByUserId has exception! ", e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            return responseData;
        }
        //获取客户PDF路径
        try {
            ResponseData qiniuRes = qiniuContract.downloadFilePDFInfo(userId, ReportType.FQZ);
            if (qiniuRes!=null&&"0".equals(qiniuRes.getStatus())) {
                url = String.valueOf(qiniuRes.getData());
                if (StringUtils.isBlank(url)||"null".equalsIgnoreCase(url)) {
                    ResponseData qiniuContractResponseData = qiniuContractNyd.downloadFilePDFInfo(userId);
                    if (qiniuContractResponseData != null && "0".equals(qiniuContractResponseData.getStatus())) {
                        url = String.valueOf(qiniuContractResponseData.getData());
                    }
                }
            } else {
                ResponseData qiniuContractResponseData = qiniuContractNyd.downloadFilePDFInfo(userId);
                if (qiniuContractResponseData != null && "0".equals(qiniuContractResponseData.getStatus())) {
                    url = String.valueOf(qiniuContractResponseData.getData());
                }
            }
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("qiniuContractNyd get pdf error! userId = " + userId, e);
            return responseData;
        }
        map.put("url", url);
        map.put("ifAssess", ifAssess);
        responseData.setData(map);
        LOGGER.info("getAssessReport  seccuss, userId is " + userId);
        return responseData;
    }

    @Override
    public ResponseData<Step> findByUserId(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<Step> list = stepDao.selectByUserId(userId);
            if (list != null && list.size() > 0 ){
                Step step = list.get(0);
                responseData.setData(step);
            }

        }catch (Exception e){
            LOGGER.error("根据userId查找资料完整度出错",e);
            ResponseData.error("服务器开小差");
        }
        return responseData;
    }


    @Override
    public void saveStep(Step step) throws Exception {
        stepDao.saveStep(step);
    }

    @Override
    public void updateStep(Step step1) throws Exception {
        stepDao.updateStep(step1);
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
            LOGGER.error("resetGrayData has exception! userId is "+userId,e);
        }
        return flag;
    }

    /**
     * 
     * 修改风控审核状态
     * @throws Exception 
     * @see com.nyd.user.api.UserStepContract#(java.lang.String)
     */
	@Override
	public void updateUserStep(StepInfo step1) throws Exception {
		Step step = new Step();
		step.setUserId(step1.getUserId());
		step.setPreAuditFlag(step1.getPreAuditFlag());
		step.setPreAuditLevel(step1.getPreAuditLevel());
		stepDao.updateStep(step);
	}

    @Override
    public void sendMsgToUserToApplication(String userId) {
        try {
            FilePDFInfo filePDFInfo = new FilePDFInfo();
            filePDFInfo.setUserId(userId);
            userToApplicationProducer.sendMsg(filePDFInfo);
        } catch (Exception e) {
            LOGGER.error("send msg to application auditpdf has exception!", e);
        }
    }
    
    @Override
   	public Map<String,Object> updateFaceFlagInfo(int pageSize,long startId) {
    	LOGGER.info("update faceFlag param startId"+startId);
       	Map<String,Object> map  = new HashMap<String,Object>();
   		Map<String,Object> paramMap = new HashMap<String,Object>();
   		paramMap.put("pageSize", pageSize);
   		paramMap.put("startId", startId);
   		List<Account> accountList =null;
   		try {
   			accountList = accountMapper.selectFaceFlagInfo(paramMap);
   		} catch (Exception e) {
   			LOGGER.info("查询表t_account表异常"+e);
   		}
   		 if(accountList == null || accountList.size() == 0) {
   			LOGGER.info("没有查询到faceFlag为0的数据");		
   			 map.put("flag", false);
   			 map.put("startId", 0L);
   			 return map;
   			
   		}
   		startId = accountList.get(accountList.size()-1).getId();
   		List<String> list = new ArrayList<>();		
   		for(Account account:accountList) {
   			if(StringUtils.isNotBlank(account.getUserId())) {
   				list.add(account.getUserId());
   			}
   			
   		}
   		if(list == null || list.size() == 0) {
   			LOGGER.info("userId为null");
   		     map.put("flag", true);
			 map.put("startId", startId);
			 return map;
   		}
   		List<Step> stepList = userStepMapper.userStepFindByUserId(list);
   		if(stepList == null || stepList.size() == 0) {
   			LOGGER.info("没有查询到Step表的数据");		
   			 map.put("flag", true);
   			 map.put("startId", startId);
   			 return map;
   			
   		}
   		for(Step step:stepList) {
   			for(Account account:accountList){
   				if(step.getUserId().equals(account.getUserId()) && "1".equals(step.getIdentityFlag())) {
   					Account updateAccount = new Account();
   					updateAccount.setUserId(account.getUserId());
   					updateAccount.setFaceFlag("1");
   					accountMapper.updateByUserId(updateAccount);
   					LOGGER.info("更新userid"+account.getUserId());
   				}
   			}
   		}
   		map.put("flag", true);
   		map.put("startId", startId);
   		return map;
   	}   
    
}
