package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.api.SendMsgRecordContract;
import com.nyd.admin.model.Info.SendMsgRecordInfo;
import com.nyd.admin.model.UnLoginMsgLogInfo;
import com.nyd.admin.model.UserSourceInfo;
import com.nyd.admin.model.Vo.MsgSendResultVo;
import com.nyd.admin.service.AccountManagerService;
import com.nyd.admin.service.SendMsgRecordService;
import com.nyd.admin.service.UnLoginMsgLogManagerService;
import com.nyd.admin.service.UserSourceManagerService;
import com.nyd.admin.service.utils.AdminProperties;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("sendMsgRecordContract")
public class SendMsgRecordContractImpl implements SendMsgRecordContract {
    private static final Logger logger = LoggerFactory.getLogger(SendMsgRecordContractImpl.class);

    private static final Integer LOGIN_SMS_TYPE_ONE = 29;
    private static final Integer LOGIN_SMS_TYPE_TWO = 31;

    @Autowired
    private ISendSmsService iSendSmsService;

    @Autowired
    private AdminProperties adminProperties;

    @Autowired
    private SendMsgRecordService sendMsgRecordService;

    @Autowired
    private UserSourceManagerService userSourceManagerService;
    @Autowired
    private AccountManagerService accountManagerService;
    @Autowired
    private UnLoginMsgLogManagerService unLoginMsgLogManagerService;

    @Override
    public ResponseData<MsgSendResultVo> sendMsg() {
        logger.info("短信跑批开始");
        ResponseData responseData = ResponseData.success();
        if ("OFF".equalsIgnoreCase(adminProperties.getNydSendMsgSwitch())){
            logger.info("发送短信开关关闭");
            responseData = ResponseData.error("短信开关已经关闭");
            return responseData;
        }
        //待发短信数量：
        int totalCount = 0;
        //发成功的数量：
        int successCount = 0;
        //发失败的数量：
        int failCount = 0;
        //短信发送失败的手机集合
        List<String> failPhones = new ArrayList<>();

        try {
            //找出所有未发的短信
            List<SendMsgRecordInfo> list = sendMsgRecordService.findByStatus();
            if (list != null && list.size() > 0){
                totalCount=list.size();
                logger.info("待发短息数量:"+totalCount);
                for (SendMsgRecordInfo sendMsgRecordInfo : list){
                    //加密手机号
                    String encryptMobile = sendMsgRecordInfo.getMobile();
                    //手机号(解密后的)
                    String mobile = new String(new BASE64Decoder().decodeBuffer(encryptMobile),"UTF-8");
                    //app名称
                    String appName = "";
                    if (StringUtils.isNotBlank(sendMsgRecordInfo.getAppName())){
                         appName = sendMsgRecordInfo.getAppName();
                    }else {
                         appName = "nyd";
                    }

                    /**
                     * 调用发送短信的接口
                     */
                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setSmsType(25);
                    smsRequest.setCellphone(mobile);
                    smsRequest.setAppName(appName);
                    ResponseData data = iSendSmsService.sendSingleSms(smsRequest);
                    logger.info("调用发送短信的接口结果:"+JSON.toJSON(data));
                    SendMsgRecordInfo  sMsgRecordInfo  = null;
                    if ("0".equals(data.getStatus())){
                        sMsgRecordInfo  = new SendMsgRecordInfo();
                        sMsgRecordInfo.setMobile(encryptMobile);
                        sMsgRecordInfo.setSendSmsTime(new Date());
                        sMsgRecordInfo.setStatus(2);
                        successCount++;
                    }else {
                        sMsgRecordInfo  = new SendMsgRecordInfo();
                        sMsgRecordInfo.setMobile(encryptMobile);
                        sMsgRecordInfo.setSendSmsTime(new Date());
                        sMsgRecordInfo.setStatus(1);
                        failCount++;
                        failPhones.add(mobile);
                    }
                    sendMsgRecordService.updateByPhone(sMsgRecordInfo);
                }
            }

            //此接口的返回对象
            MsgSendResultVo msgSendResultVo = new MsgSendResultVo();
            msgSendResultVo.setTotalCount(totalCount);
            logger.info("发送短信总数量:"+totalCount);

            msgSendResultVo.setFailCount(failCount);
            logger.info("发送短信失败总数量:"+failCount);

            msgSendResultVo.setSuccessCount(successCount);
            logger.info("发送短信成功总数量:"+successCount);

            msgSendResultVo.setFailPhone(failPhones);
            logger.info("发送短信失败手机集合:"+JSON.toJSON(failPhones));

            logger.info("发送短信最终结果:"+ JSON.toJSON(msgSendResultVo));
            responseData.setData(msgSendResultVo);
        }catch (Exception e){
            logger.error("发送短信异常",e);
            responseData.error("服务器开小差");
        }
        return responseData;
    }


    @Override
    public ResponseData<MsgSendResultVo> sendUnloginMsg(Date nowDate) {
        logger.info("sendUnloginMsg start,nowDate:{}",nowDate);
        ResponseData responseData = ResponseData.success();
        if("OFF".equalsIgnoreCase(adminProperties.getLoginSmsSwitch())){
            logger.info("未登录发送短信通道开发关闭，today:{}",new Date());
            responseData = ResponseData.error("未登录短信开关已经关闭");
            return responseData;
        }
        try {
            if(null==nowDate){
                nowDate = new Date();
            }
            List<String> failPhones = new ArrayList<>();
            int totalCount = 0;
            int successCount = 0;
            int failCount = 0;
            MsgSendResultVo msgSendResultVo = new MsgSendResultVo();
            msgSendResultVo.setTotalCount(totalCount);
            msgSendResultVo.setSuccessCount(successCount);
            msgSendResultVo.setFailCount(failCount);
            msgSendResultVo.setFailPhone(failPhones);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strNowTime =simpleDateFormat.format(nowDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(nowDate);
            ca.add(Calendar.HOUR_OF_DAY,-1);
            String oneBeforeHour = simpleDateFormat.format(ca.getTime());
            ca.setTime(nowDate);
            ca.add(Calendar.HOUR_OF_DAY,-2);
            String twoBeforeHour = simpleDateFormat.format(ca.getTime());
            Map<String,String> timeMap = new HashMap<>();
            //一个小时内注册时间
            timeMap.put("startTime",oneBeforeHour);
            timeMap.put("endTime",strNowTime);
            List<String> firstMobiles =accountManagerService.findByTime(timeMap);
            //一个小时到两个小时内注册时间
            timeMap.put("startTime",twoBeforeHour);
            timeMap.put("endTime",oneBeforeHour);
            List<String> secondAccountInfos =accountManagerService.findByTime(timeMap);
            //发送一个小时内所有未登陆用户
            if(null!=firstMobiles && firstMobiles.size()>0){
                msgSendResultVo.setTotalCount(firstMobiles.size());
                for (String mobile : firstMobiles){
                    UnLoginMsgLogInfo unLoginMsgLogInfo = unLoginMsgLogManagerService.getByMobile(mobile);
                    if(null==unLoginMsgLogInfo){
                        UserSourceInfo userSourceInfo = userSourceManagerService.getByMobile(mobile);
                        if(null!=userSourceInfo){
                            String appName = userSourceInfo.getAppName();
                            boolean sendFlag = sendMsg(mobile,appName,LOGIN_SMS_TYPE_ONE,msgSendResultVo);
                            UnLoginMsgLogInfo saveMsgLog = new UnLoginMsgLogInfo();
                            saveMsgLog.setMobile(mobile);
                            saveMsgLog.setAppName(appName);
                            saveMsgLog.setSendCount(1);
                            saveMsgLog.setType(0);
                            saveMsgLog.setSendSmsTime(new Date());
                            if(sendFlag){
                                saveMsgLog.setStatus(0);
                            }else{
                                saveMsgLog.setStatus(1);
                            }
                            unLoginMsgLogManagerService.saveUnLoginMsgLog(saveMsgLog);
                        }
                    }
                }
            }
            //发送一小时到两小时内未登陆的用户
            if(null!=secondAccountInfos && secondAccountInfos.size()>0){
                msgSendResultVo.setTotalCount(msgSendResultVo.getTotalCount()+secondAccountInfos.size());
                for (String mobile : secondAccountInfos){
                    UnLoginMsgLogInfo unLoginMsgLogInfo = unLoginMsgLogManagerService.getByMobile(mobile);
                    if(null!=unLoginMsgLogInfo && unLoginMsgLogInfo.getSendCount()==1){
                        UserSourceInfo userSourceInfo = userSourceManagerService.getByMobile(mobile);
                        if(null!=userSourceInfo){
                            String appName = userSourceInfo.getAppName();
                            boolean sendFlag = sendMsg(mobile,appName,LOGIN_SMS_TYPE_TWO,msgSendResultVo);
                            UnLoginMsgLogInfo saveMsgLog = new UnLoginMsgLogInfo();
                            saveMsgLog.setMobile(mobile);
                            saveMsgLog.setSendCount(2);
                            saveMsgLog.setSendSmsTime(new Date());
                            if(sendFlag){
                                saveMsgLog.setStatus(0);
                            }else{
                                saveMsgLog.setStatus(1);
                            }
                            unLoginMsgLogManagerService.updateUnLoginMsgLog(saveMsgLog);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("sendUnloginMsg exception",e);
        }
        return responseData;
    }


    private boolean sendMsg(String mobile,String appName,Integer smsType,MsgSendResultVo msgSendResultVo){
        boolean flag = false;
        try {
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setSmsType(smsType);
            smsRequest.setCellphone(mobile);
            if(StringUtils.isBlank(appName)){
                smsRequest.setAppName("nyd");
            }else{
                smsRequest.setAppName(appName);
            }
            ResponseData msgResult = iSendSmsService.sendSingleSms(smsRequest);
            if(null==msgResult || !"0".equals(msgResult.getStatus())){
                msgSendResultVo.setFailCount(msgSendResultVo.getFailCount()+1);
                msgSendResultVo.getFailPhone().add(mobile);
            }else{
                msgSendResultVo.setSuccessCount(msgSendResultVo.getSuccessCount()+1);
                flag = true;
            }
        }catch (Exception e){
            logger.error("sendMsg exception,mobile:{}",mobile,e);
            msgSendResultVo.setFailCount(msgSendResultVo.getFailCount()+1);
            msgSendResultVo.getFailPhone().add(mobile);
        }
        return flag;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //加密
       /* String phone = "13621994034";
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] textByte = phone.getBytes("UTF-8");
        String encryptMobile = encoder.encode(textByte);
        System.out.println(encryptMobile);

//        String encryptMobile = "MTc1MjE1MzMxNTU=";
        try {
            String mobile = new String(new BASE64Decoder().decodeBuffer(encryptMobile),"UTF-8");
            System.out.println(mobile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
}
