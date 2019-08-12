package com.nyd.user.service.mq;

import com.alibaba.fastjson.JSON;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Step;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.service.StepInfoService;
import com.nyd.user.service.UserTargetService;
import com.nyd.user.service.util.Md5Util;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HitLibraryConsumerProcessor implements RabbitmqMessageProcesser<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(HitLibraryConsumerProcessor.class);

    @Autowired
    private UserTargetService userTargetService;

    @Autowired
    private StepInfoService stepInfoService;

    @Override
    public void processMessage(byte[] message) {
        logger.info("侬要贷撞库,消费者进来了++++++++++++++");
        String s = new String(message);     
        try {
            if (StringUtils.isNotBlank(s)){
                List<Account> list = JSON.parseArray(s, Account.class);
                logger.info("将接收到的消息转为集合:"+JSON.toJSONString(list));
                for (Account account : list){
                    String mobile = "";
                    if(null != account.getLastActiveTime()){
                        mobile = account.getAccountNumber();
                        List<UserTarget> userTargetList = userTargetService.findByMobile(mobile);

                        //表示打标表里已经存在该对象,接下来进行更新操作
                        if (userTargetList != null && userTargetList.size() > 0 ){
                            //打标对象：
                            UserTarget userTarget = new UserTarget();

                            //找出这个人的信息完整度
                            if (StringUtils.isNotBlank(account.getUserId())){
                                String userId = account.getUserId();
                                ResponseData<Step> data = stepInfoService.findByUserId(userId);
                                if ("0".equals(data.getStatus())){
                                    Step step = data.getData();
                                    if (step != null){
                                        String mobileFlag = step.getMobileFlag();           //资料完整度  0：不完整  1：完整
                                        if ("1".equals(mobileFlag)){
                                            userTarget.setTargetFour(1);
                                        }else {
                                            userTarget.setTargetFour(0);
                                        }
                                    }else {
                                        userTarget.setTargetFour(0);
                                    }
                                }
                            }else {
                                userTarget.setTargetFour(0);
                            }

                            userTarget.setMobile(mobile);                               //手机号
                            //更新MD5手机号以及sha手机号不需要更新(此处考虑测试环境之前没有加入加密手机号，所以此处一并更新了)
                            String md5Mobile = md5Encrypt(mobile);                  //MD5手机号
                            String shaMobile = shaEncrypt(mobile);                  //sha手机号
                            userTarget.setMd5Mobile(md5Mobile);
                            userTarget.setShaMobile(shaMobile);
                            userTarget.setIfRegister(1);

                            Date lastActiveTime = account.getLastActiveTime();
                            String startDate = changeDate(lastActiveTime);
                            Date date = new Date();
                            String endDate = changeDate(date);
                            long daySub = getDaySub(startDate, endDate);
                            int a = Integer.parseInt(String.valueOf(daySub));

                            if (a>=0 && a<=30){
                                userTarget.setTargetOne(1);
                                userTarget.setTargetTwo(1);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a > 30 && a<= 90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(1);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a >= 60 && a<= 90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(0);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a >90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(0);
                                userTarget.setTargetThree(0);
                                userTarget.setTargetFive(1);
                            }

                            userTargetService.updateUserTarget(userTarget);

                        }else {           // 表示打标表里不存在该对象,接下来进行save操作
                            //打标对象：
                            UserTarget userTarget = new UserTarget();
                            //找出这个人的信息完整度
                            if (StringUtils.isNotBlank(account.getUserId())){
                                String userId = account.getUserId();
                                ResponseData<Step> data = stepInfoService.findByUserId(userId);
                                if ("0".equals(data.getStatus())){
                                    Step step = data.getData();
                                    if (step != null){
                                        String mobileFlag = step.getMobileFlag();           //资料完整度  0：不完整  1：完整
                                        if ("1".equals(mobileFlag)){
                                            userTarget.setTargetFour(1);
                                        }else {
                                            userTarget.setTargetFour(0);
                                        }
                                    }else {
                                        userTarget.setTargetFour(0);
                                    }
                                }
                            }else {
                                userTarget.setTargetFour(0);
                            }

                            userTarget.setMobile(mobile);                           //手机号
                            String md5Mobile = md5Encrypt(mobile);                  //MD5手机号
                            String shaMobile = shaEncrypt(mobile);                  //sha手机号
                            userTarget.setMd5Mobile(md5Mobile);
                            userTarget.setShaMobile(shaMobile);
                            userTarget.setIfRegister(1);

                            Date lastActiveTime = account.getLastActiveTime();
                            String startDate = changeDate(lastActiveTime);
                            Date date = new Date();
                            String endDate = changeDate(date);
                            long daySub = getDaySub(startDate, endDate);
                            int a = Integer.parseInt(String.valueOf(daySub));
                            if (a>=0 && a<=30){
                                userTarget.setTargetOne(1);
                                userTarget.setTargetTwo(1);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a > 30 && a<= 90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(1);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a >= 60 && a<= 90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(0);
                                userTarget.setTargetThree(1);
                                userTarget.setTargetFive(0);
                            }else if (a >90){
                                userTarget.setTargetOne(0);
                                userTarget.setTargetTwo(0);
                                userTarget.setTargetThree(0);
                                userTarget.setTargetFive(1);
                            }

                            userTargetService.save(userTarget);

                        }

                    }else {      //表示最后登录时间为空
                        /**
                         * 先查一次是否存在
                         */
                        List<UserTarget> userTargetList = userTargetService.findByMobile(account.getAccountNumber());
                        if (userTargetList != null && userTargetList.size() > 0 ){
                            UserTarget userTarget = new UserTarget();
                            userTarget.setMobile(account.getAccountNumber());                           //手机号
                            String md5Mobile = md5Encrypt(account.getAccountNumber());                  //MD5手机号
                            String shaMobile = shaEncrypt(account.getAccountNumber());                  //sha手机号
                            userTarget.setMd5Mobile(md5Mobile);
                            userTarget.setShaMobile(shaMobile);
                            userTarget.setIfRegister(0);
                            userTarget.setTargetOne(0);
                            userTarget.setTargetTwo(0);
                            userTarget.setTargetThree(0);
                            userTarget.setTargetFour(0);
                            userTarget.setTargetFive(0);
                            userTargetService.updateUserTarget(userTarget);

                        }else {
                            UserTarget userTarget = new UserTarget();
                            userTarget.setMobile(account.getAccountNumber());                           //手机号
                            String md5Mobile = md5Encrypt(account.getAccountNumber());                  //MD5手机号
                            String shaMobile = shaEncrypt(account.getAccountNumber());                  //sha手机号
                            userTarget.setMd5Mobile(md5Mobile);
                            userTarget.setShaMobile(shaMobile);
                            userTarget.setIfRegister(0);
                            userTarget.setTargetOne(0);
                            userTarget.setTargetTwo(0);
                            userTarget.setTargetThree(0);
                            userTarget.setTargetFour(0);
                            userTarget.setTargetFive(0);
                            userTargetService.save(userTarget);
                        }



                    }
                }
            }
            logger.info("侬要贷数据打标消费者完毕++++++++++++++++++++");
        }catch (Exception e){
            logger.error("侬要贷用户标记出错",e);
        }


    }

    /**
     * 手机号MD5加密
     * @param accountNumber
     * @return
     */
    public static String md5Encrypt(String accountNumber){
        String mobile = Md5Util.getMD5To32LowCaseSign(accountNumber);
        return mobile;
    }

    /**
     * 手机号sha256加密
     * @param accountNumber
     * @return
     */
    public static String shaEncrypt(String accountNumber){
        String mobile = DigestUtils.sha256Hex(accountNumber.getBytes());
        return mobile;
    }

    /**
     * 时间转换  Date  ---> String
     */
    public static String changeDate(Date date){
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(date);
        return day;
    }

    /**
     * 时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        } catch (Exception e) {
            logger.error("时间相减处理出错",e);
        }
        return day;
    }
}
