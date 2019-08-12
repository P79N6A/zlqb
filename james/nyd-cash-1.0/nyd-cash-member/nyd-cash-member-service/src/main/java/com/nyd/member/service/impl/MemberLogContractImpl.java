package com.nyd.member.service.impl;

import com.nyd.member.api.MemberLogContract;
import com.nyd.member.dao.MemberLogDao;
import com.nyd.member.entity.MemberLog;
import com.nyd.member.model.MemberLogModel;
import com.nyd.member.service.consts.MemberConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
@Service(value = "memberLogContract")
public class MemberLogContractImpl implements MemberLogContract {
    private static Logger LOGGER = LoggerFactory.getLogger(MemberLogContractImpl.class);

    @Autowired
    private MemberLogDao memberLogDao;

    @Override
    public ResponseData saveMemberLog(MemberLogModel memberLogModel) {
        LOGGER.info("begin to save memberLog, userId is " + memberLogModel.getUserId());
        ResponseData responseData = ResponseData.success();
        MemberLog memberLog = new MemberLog();
        BeanUtils.copyProperties(memberLogModel,memberLog);
        try {
            memberLogDao.save(memberLog);
            LOGGER.info("save memberLog success !");
        } catch (Exception e) {
            LOGGER.error("save memberLog has exception! userId is"+memberLogModel.getUserId(),e);
            return ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData updateMemberLog(String userId) {
        LOGGER.info("begin to update memberLog, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            memberLogDao.update(userId);
            LOGGER.info("begin memberLog success !");
        } catch (Exception e) {
            LOGGER.error("update memberLog has exception! userId is" + userId,e);
            ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public void updateMemberLogByUserId(MemberLog memberLog) throws Exception {
        memberLogDao.updateMemberLogByUserId(memberLog);
    }

    @Override
    public ResponseData<MemberLogModel> getMemberLogByUserIdAndDibitFlag(String userId, String debitFlag) {
        ResponseData responseData = ResponseData.success();
        try {
            MemberLogModel memberLogModel = memberLogDao.getMemberLogByUserIdAndDibitFlag(userId,debitFlag);
            responseData.setData(memberLogModel);
        } catch (Exception e) {
            LOGGER.error("getMemberLogByUserIdAndDibitFlag memberLog has exception! userId is" + userId,e);
            ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<MemberLogModel>> getMemberLog(String userId, String debitFlag) {
        ResponseData responseData = ResponseData.success();
        try {
            List<MemberLogModel> list = memberLogDao.getMemberLog(userId,debitFlag);
            responseData.setData(list);
        } catch (Exception e) {
            LOGGER.error("getMemberLog memberLog has exception! userId is" + userId,e);
            ResponseData.error(MemberConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

	@Override
	public ResponseData<MemberLogModel> getMemberLogByUserId(String userId) {
		 ResponseData responseData = ResponseData.success();
		 try {
			     MemberLogModel model = memberLogDao.findMemberLogByUserId(userId);
	             responseData.setData(model);
	        } catch (Exception e) {
	            LOGGER.error("find MemberLog memberLog has exception! userId is" + userId,e);
	            ResponseData.error(MemberConsts.DB_ERROR_MSG);
	        }
	        return responseData;
	}
}
