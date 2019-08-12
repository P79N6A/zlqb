package com.nyd.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nyd.activity.api.CouponContract;
import com.nyd.member.api.MemberContract;
import com.nyd.member.dao.MemberConfigDao;
import com.nyd.member.dao.MemberDao;
import com.nyd.member.dao.MemberLogDao;
import com.nyd.member.entity.Member;
import com.nyd.member.entity.MemberLog;

import com.nyd.member.model.*;
import com.nyd.member.model.enums.AppType;
import com.nyd.member.service.MemberInfoService;
import com.nyd.member.service.consts.MemberConsts;
import com.nyd.member.service.util.DateUtil;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2017/12/6
 */
@Service(value = "memberContract")
public class MemberContractImpl implements MemberContract,MemberInfoService {
    private static Logger LOGGER = LoggerFactory.getLogger(MemberContractImpl.class);

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberConfigDao memberConfigDao;
    @Autowired
    private MemberLogDao memberLogDao;
    @Autowired
    private CouponContract couponContract;

    @Autowired
    private UserAccountContract userAccountContract;



    /**
     * 保存或更新会员信息，保存会员信息进入会员日志表
     * @param memberModel 会员信息
     * @return
     */
    @Override
    public ResponseData saveMember(MemberModel memberModel) {
        LOGGER.info("begin to save member, object memberModel is " + JSON.toJSON(memberModel));
        ResponseData responseData = ResponseData.success();
        //参数校验
        try {
        	MemberConfigModel memberConfigModel = new MemberConfigModel();
            //去查询会员配置表
        	if(AppType.WWJ.getType().equals(memberModel.getAppName())) {//万万借
        		 memberConfigModel = memberConfigDao.getMermberConfigByTypeBusiness(memberModel.getMemberType(), memberModel.getAppName());
            }else {
            	 memberConfigModel = memberConfigDao.getMermberConfigByTypeBusiness(memberModel.getMemberType(),"nyd");
            }                         
            if ("1".equals(memberModel.getDebitFlag())||"2".equals(memberModel.getDebitFlag())) { //如果扣款成功才去做member表的更新
                //先查询是否之前有会员
                MemberModel model = memberDao.getMemberByUserId(memberModel.getUserId());
                Member member = new Member();
                BeanUtils.copyProperties(memberModel,member);
                if (model!=null) { //之前有会员
                    //判断是否过期
                    if (compareDate(new Date(),model.getExpireTime())) {
                        //没有过期
                        if (memberConfigModel!=null) {
                            member.setMemberTypeDescribe(memberConfigModel.getTypeDescribe());
                            member.setExpireTime(addDay(model.getExpireTime(),memberConfigModel.getEffectTime()));
                        }
                    } else {
                        //过期
                        member.setMemberTypeDescribe(memberConfigModel.getTypeDescribe());
                        member.setExpireTime(addDay(new Date(),memberConfigModel.getEffectTime()));
                    }
                    //更新会员表
                    memberDao.update(member);
                } else { //之前没会员
                    LOGGER.info("memberConfigModel:"+JSON.toJSONString(memberConfigModel));
                    member.setMemberTypeDescribe(memberConfigModel.getTypeDescribe());
                    member.setExpireTime(addDay(new Date(),memberConfigModel.getEffectTime()));
                    LOGGER.info("member:"+JSON.toJSONString(member));
                    memberDao.save(member);
                }
            }
            //保存会员日志表
            MemberLog memberLog = new MemberLog();
            BeanUtils.copyProperties(memberModel,memberLog);
            if (memberConfigModel!=null) {
                memberLog.setMemberTypeDescribe(memberConfigModel.getTypeDescribe());
                memberLog.setMemberFee(memberConfigModel.getDiscountFee());
            }
            memberLog.setStartTime(new Date());
            memberLogDao.save(memberLog);
            LOGGER.info("save member success !");



        } catch (Exception e) {
            LOGGER.error("getMember has exception! userId is"+memberModel.getUserId(),e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
            return responseData;
        }
        return responseData;
    }

    /**
     * 查询会员信息，过期会员返回空
     * @param userId 用户ID
     * @return
     */
    @Override
    public ResponseData<MemberModel> getMember(String userId) {
        LOGGER.info("begin to get member,userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            MemberModel memberModel = memberDao.getMemberByUserId(userId);
            //是否过期
            if (memberModel!=null) {
               if (compareDate(new Date(),memberModel.getExpireTime())) {
                   MemberLogModel memberLogModel= memberLogDao.getMemberLogByUserId(userId);
                   if(memberLogModel != null){
                       memberModel.setMemberId(memberLogModel.getMemberId());
                       memberModel.setMemberFee(memberLogModel.getMemberFee());
                   }
                   responseData.setData(memberModel);
               }
            }
            LOGGER.info("get member success !");
        } catch (Exception e) {
            LOGGER.error("getMember has exception! userId is"+userId,e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
            return responseData;
        }
        return responseData;
    }
    /**
     * 查询会员信息，过期会员返回空
     * @param userId 用户ID
     * @return
     */
    @Override
    public ResponseData<MemberModel> getMemberWithMemberId(String userId) {
    	LOGGER.info("begin to get member,userId is " + userId);
    	ResponseData responseData = ResponseData.success();
    	try {
    		MemberModel memberModel = memberDao.getMemberByUserId(userId);
    		//是否过期
    		if (memberModel!=null) {
    			if (compareDate(new Date(),memberModel.getExpireTime())) {
    				MemberLogModel memberLogModel= memberLogDao.getMemberLogWithMemberIdByUserId(userId);
    				if(memberLogModel != null){
    					memberModel.setMemberId(memberLogModel.getMemberId());
    					memberModel.setMemberFee(memberLogModel.getMemberFee());
    				}
    				LOGGER.info("查询会员信息：" + JSON.toJSONString(memberModel));
    				responseData.setData(memberModel);
    			}
    		}
    		LOGGER.info("get member success !");
    	} catch (Exception e) {
    		LOGGER.error("getMember has exception! userId is"+userId,e);
    		responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
    		return responseData;
    	}
    	return responseData;
    }

    /**
     * 会员购买页展示内容
     * @param baseInfo
     * @return
     */
    @Override
    public ResponseData<MemberInfo> fetchMemberInfo(BaseInfo baseInfo) {
        LOGGER.info("begin to fetch memberInfo,userId is " + baseInfo.getUserId());
        ResponseData responseData = ResponseData.success();
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMobile(baseInfo.getAccountNumber());
        try {
            MemberModel memberModel = memberDao.getMemberByUserId(baseInfo.getUserId());
            if (memberModel!=null) {//存在会员信息
                //是否过期
                if (compareDate(new Date(),memberModel.getExpireTime())) {
                    //没过期
                    BeanUtils.copyProperties(memberModel,memberInfo);
                    memberInfo.setExpireTime(DateUtil.dateToString(memberModel.getExpireTime()));
                } else {
                    memberInfo.setMemberStatus("已过期");
                }
            } else {
                memberInfo.setMemberStatus("未购买");
            }
        } catch (Exception e) {
            LOGGER.error("MemberContractImpl fetchMemberInfo getMemberByUserId has exception! userId is"+baseInfo.getUserId(),e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
            return responseData;
        }
        //查询会员配置信息
        try {
            List<MemberConfigModel> memberConfigList = memberConfigDao.getMermberConfigByBusiness("nyd");
            memberInfo.setMemberConfigList(memberConfigList);
        } catch (Exception e) {
            LOGGER.error("MemberContractImpl fetchMemberInfo getMermberConfigByBusiness has except! userId is"+baseInfo.getUserId());
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
            return responseData;
        }
        LOGGER.info("fetch memberInfo success !");

        /**
         * 优惠券数量
         */
        ResponseData<Integer> d = couponContract.getCouponNumberByAccountNumber(baseInfo.getAccountNumber());
        Integer num = 0;
        if ("0".equals(d.getStatus())){
             num = d.getData();
        }
        memberInfo.setCouponCount(num);

        /**
         * 账户总余额
         */
        try {
            ResponseData<AccountDto> responseResult = userAccountContract.queryBalance(baseInfo.getAccountNumber());
            LOGGER.info("用户account相关参数："+ JSON.toJSON(responseResult));
            String totalBalance = "";
            Gson gson = new Gson();
            if ("success".equals(responseResult.getMsg())){
                WZResponse response = gson.fromJson(gson.toJson(responseResult), WZResponse.class);
                WZResponse.Data data = response.getData();
                totalBalance = data.getTotalBalance();
                LOGGER.info("账户总余额:"+totalBalance);
            }
            memberInfo.setTotalBalance(totalBalance);

        }catch (Exception e){
            LOGGER.error("查询账户余额出错！",e);
            responseData = ResponseData.error();
            return responseData;
        }


        responseData.setData(memberInfo);
        return responseData;
    }

    /**
     * 比较日期大小
     * @return
     */
    private boolean compareDate(Date today,Date expireDate) {
        if (expireDate!=null) {
            if (expireDate.getTime()>today.getTime()) {
                return true;
            }
        }
        return false;
    }

    private Date addDay(Date date ,Integer effectTime) {
        if (date!=null&&effectTime!=null) {
         long time = date.getTime()+(effectTime.longValue()*24*60*60*1000);
         return new Date(time);
        }
        return date;
    }

    @Override
    public ResponseData<List<String>> doAssessTask() {
        LOGGER.info("begin to get doAssessTask");
        ResponseData responseData = ResponseData.success();
        try {
            List<String> memberModels =  memberDao.getMembers();
            responseData.setData(memberModels);
        } catch (Exception e) {
            LOGGER.error("doAssessTask has exception!",e);
            responseData = ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }


}
