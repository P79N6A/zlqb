package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.enums.AttachmentType;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.dsp.api.JoContract;
import com.nyd.dsp.model.request.IdNumberMobileNameModel;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserInfoContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.dao.UserDetailDao;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Step;
import com.nyd.user.model.*;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.UserDto;
import com.nyd.user.model.vo.IdentityVo;
import com.nyd.user.service.IdentityInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 17/11/3.
 */
@Service(value = "userIdentityContract")
public class IdentityInfoServiceImpl implements IdentityInfoService,UserIdentityContract,UserInfoContract {
    private static Logger LOGGER = LoggerFactory.getLogger(IdentityInfoServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired(required = false)
    private QiniuContract qiniuContract;
    @Autowired(required = false)
    private JoContract joContract;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private StepDao stepDao;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserProperties userProperties;
    @Autowired(required = false)
    private AgreeMentContract agreeMentContract;
    @Autowired
    private UserSourceMapper userSourceMapper;

    /**
     * 保存身份证信息
     * @param identityInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public ResponseData saveUserInfo(IdentityInfo identityInfo) throws Exception {
        LOGGER.info("begin to save userInfo, accountNumber is " + identityInfo.getAccountNumber());
        ResponseData responseData = ResponseData.success();
        if(!judgeJo(identityInfo)){
            return ResponseData.error(UserConsts.IDCARD_THREE_ERROR);
        }
        //先根据身份证号查询，如果有证明是老用户，没有是新户
        List<UserInfo> userList = null;
        try {
            LOGGER.info("begin to get userInfo, idNumber is " + identityInfo.getIdNumber());
            userList = userDao.getUsersByIdNumber(identityInfo.getIdNumber());
        } catch (Exception e) {
            LOGGER.error("get userInfo error! userId = "+identityInfo.getUserId(),e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            return responseData;
        }
        if (userList != null && userList.size()>0) {
            LOGGER.info("idNumber is already exist");
            //已存在用户
            responseData = ResponseData.error(UserConsts.IDCARD_ALREADY_REGISTER);
            return responseData;
        } else {//新户
            LOGGER.info("idNumber is not exist");
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
                LOGGER.error("save userInfo error! userId = "+userId,e);
                throw e;
            }
            //用户userId绑定账号信息
            LOGGER.info("begin to update accountInfo");
            Account account = new Account();
            account.setAccountNumber(identityInfo.getAccountNumber());
            account.setUserId(userId);
            try {
                accountDao.updateAccountByAccountNumber(account);
                LOGGER.info("update accountInfo success");
            } catch (Exception e) {
                LOGGER.error("account bind user error! accountNumber = "+identityInfo.getAccountNumber(),e);
                throw e;
            }
            try {
                //更新信息完整度
                LOGGER.info("begin to save stepInfo");
                Step step =new Step();
                step.setUserId(userInfo.getUserId());
                step.setIdentityFlag(UserConsts.FILL_FLAG);
                stepDao.saveStep(step);
                LOGGER.info("update stepInfo success");
            } catch (Exception e) {
                LOGGER.error("save userInfo success，but save stepInfo failed ! userId= "+userId, e);
            }
            //照片保存
            try {
                saveAttachment(userId,identityInfo.getIdCardFrontPhoto(),identityInfo.getIdCardBackPhoto());
            } catch (Exception e) {
                LOGGER.error("save photo error! userId = "+userId,e);
            }
            //签署注册及隐私协议
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId",userId);
                jsonObject.put("userName",identityInfo.getRealName());
                agreeMentContract.signRegisterAgreeMent(jsonObject);
            } catch (Exception e) {
                LOGGER.error("signRegisterAgreeMent has error! userId is "+userId,e);
            }
            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setUserId(userId);
            responseData.setData(baseInfo);
            LOGGER.info("save userInfo success, generate userId is " + userId);
            return responseData;
        }
    }

    /**
     * 根据userId查询身份证信息,用于H5
     * @param userId
     * @return
     */
    @Override
    public ResponseData<IdentityVo> getIdentityInfo(String userId){
        LOGGER.info("begin to get identityInfo, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            IdentityVo identityVo = new IdentityVo();
            List<UserDetailInfo> userList = userDetailDao.getUserDetailsByUserId(userId);
            if(userList != null && userList.size()>0){
                BeanUtils.copyProperties(userList.get(0),identityVo);
            }
            responseData.setData(identityVo);
            LOGGER.info("get identityInfo success");
        }catch (Exception e) {
            LOGGER.error("get userInfo error! userId = "+userId,e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 根据userId查询身份证信息,用于rpc
     * @param userId
     * @return
     */
    @Override
    public ResponseData<UserInfo> getUserInfo(String userId){
        ResponseData responseData = ResponseData.success();
        try {
            UserInfo userInfo = new UserInfo();
            List<UserInfo> userList = userDao.getUsersByUserId(userId);
            List<AccountInfo> mobileByUserId = userDao.getMobileByUserId(userId);
            if(userList != null && userList.size()>0){
                BeanUtils.copyProperties(userList.get(0),userInfo);
                if (mobileByUserId !=null && mobileByUserId.size() > 0){
                    userInfo.setAccountNumber(mobileByUserId.get(0).getAccountNumber());
                }
            }
            responseData.setData(userInfo);
        }catch (Exception e) {
            LOGGER.error("get userInfo error! userId = "+userId,e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<UserInfo>> getUserInfoByIdNo(String idNumber) {
        ResponseData responseData = ResponseData.success();
        try {
            UserInfo userInfo = new UserInfo();
            List<UserInfo> userList = userDao.getUsersByIdNumber(idNumber);
//            if(userList != null && userList.size()>0){
//                BeanUtils.copyProperties(userList.get(0),userInfo);
//            }
            responseData.setData(userList);
        }catch (Exception e) {
            LOGGER.error("get userInfo error! idNumber = "+idNumber,e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 根据userId查询身份证详细信息,用于rpc
     * @param userId
     * @return
     */
    @Override
    public ResponseData<UserDetailInfo> getUserDetailInfo(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            UserDetailInfo userDetailInfo = new UserDetailInfo();
            List<UserDetailInfo> detailList = userDetailDao.getUserDetailsByUserId(userId);
            if(detailList != null && detailList.size()>0){
                BeanUtils.copyProperties(detailList.get(0),userDetailInfo);
            }
            responseData.setData(userDetailInfo);
        }catch (Exception e) {
            LOGGER.error("get userDetailInfo error! userId = "+userId,e);
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 根据账号、居住地查询用户信息,用于rpc
     * @param userDto
     * @return
     */
    @Override
    public ResponseData<List<UserInfo>> getUserInfos(UserDto userDto) {
        ResponseData responseData = ResponseData.success();
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userDto,userInfo);
        List<UserInfo> userList = null;
        if(userDto.getAccountNumber() != null){
            try {
                List<Account> list = accountDao.getAccountsByAccountNumber(userDto.getAccountNumber());
                if(list != null && list.size()>0){
                    userInfo.setUserId(list.get(0).getUserId());
                }
                if(userInfo.getUserId()!=null){
                    userList = userDao.getUsers(userInfo);
                }
                responseData.setData(userList);
                return responseData;
            } catch (Exception e) {
                LOGGER.error("get userInfo error! userDto = "+userDto.toString(),e);
                responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            }
        }
        if(userDto.getLivingCity() != null && userDto.getLivingDistrict() != null){
            try {
                userList = userDao.getUsers(userInfo);
                responseData.setData(userList);
                return responseData;
            } catch (Exception e) {
                LOGGER.error("get userInfo error! userDto = "+userDto.toString(),e);
                responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            }
        }
        return responseData;
    }

    @Override
    public ResponseData<List<PayFeeInfo>> getPayFeeByBusiness(String business) {
            ResponseData responseData = ResponseData.success();
            try {
                List<PayFeeInfo> userList = userDao.getPayFeeByBusiness(business);
                responseData.setData(userList);
            }catch (Exception e) {
                responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            }
            return responseData;
    }


    /**
     * 保存身份证正反面照片
     */
    private void saveAttachment(String userId, String frontfile, String backfile) {
        new Thread(() -> {
            LOGGER.info("begin to save idCard front photo, userId is " + userId);
            //身份证正面照
            AttachmentModel frontModel = new AttachmentModel();
            frontModel.setUserId(userId);
            frontModel.setFile(frontfile);
            frontModel.setType(AttachmentType.IDCARD_FRONT_PHOTO.getType());
            frontModel.setFileName(AttachmentType.IDCARD_FRONT_PHOTO.getDescription());
            qiniuContract.base64Upload(frontModel);
            LOGGER.info("begin to save idCard back photo, userId is " + userId);
            //身份证反面照
            AttachmentModel backModel = new AttachmentModel();
            backModel.setUserId(userId);
            backModel.setFile(backfile);
            backModel.setType(AttachmentType.IDCARD_BACK_PHOTO.getType());
            backModel.setFileName(AttachmentType.IDCARD_BACK_PHOTO.getDescription());
            qiniuContract.base64Upload(backModel);
            LOGGER.info("save idCard photo success");
        }).start();
    }

    /**
     * 三要素验证
     * @param identityInfo
     * @return boolean
     */
    private boolean judgeJo(IdentityInfo identityInfo){
        LOGGER.info("begin to judgeJo, accountNumber is " + identityInfo.getAccountNumber());
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
        LOGGER.info("judgeJo response is "+ JSONObject.toJSONString(response));
        if("0".equals(response.getStatus())) {
            String flag = response.getData();
            if("0".equals(flag)){//集奥 0 代表一致
                judge = true;
            }
        }
        LOGGER.info("judgeJo result is " + judge);
        return judge;
    }

    /**
     * 根据用户身份证或手机号查找用户
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseData<UserInfo> getExistUserInfo(UserDto userDto) {
        ResponseData responseData = ResponseData.success();
        if (null == userDto || (StringUtils.isBlank(userDto.getAccountNumber()) && StringUtils.isBlank(userDto.getIdNumber()))) {
            LOGGER.error("getExistUserInfo error! userDto = " + userDto.toString());
            return null;
        }

        UserInfo userInfo = null;
        String userId="";
        try {
            //判断用户表是否存在身份证号
            if (!StringUtils.isBlank(userDto.getIdNumber())) {
                List<UserInfo> list = userDao.getUsersByIdNumber(userDto.getIdNumber());
                if (null != list && list.size() > 0) {
                    userInfo = list.get(0);
                    if (userInfo!=null){
                        userId = userInfo.getUserId();
                    }
                }
            }
            //判断用户详情里是否存在身份证号
            if (!StringUtils.isBlank(userDto.getIdNumber())) {
                List<UserDetailInfo> list = userDetailDao.getUserDetailsByIdNumber(userDto.getIdNumber());
                if (null != list && list.size() > 0) {
                    UserDetailInfo userDetailInfo = list.get(0);
                    userId = userDetailInfo.getUserId();
                    if(!StringUtils.isBlank(userDetailInfo.getIdNumber())){
                        if (userInfo==null){
                            userInfo= new UserInfo();
                        }
                        userInfo.setIdNumber(userDetailInfo.getIdNumber());
                    }
                }
            }
            if (StringUtils.isNotBlank(userId)){
                List<AccountDto> accountDtos = accountDao.getAccountByuserId(userId);
                if(null!=accountDtos && accountDtos.size() > 0){
                    if (userInfo==null){
                        userInfo= new UserInfo();
                    }
                    userInfo.setChannel(accountDtos.get(0).getChannel());
                }
            }
            //判断用户账号表是否存在 手机号
            if (!StringUtils.isBlank(userDto.getAccountNumber())) {
                List<Account> list = accountDao.getAccountsByAccountNumber(userDto.getAccountNumber());
                if (null != list && list.size() > 0) {
                    Account account = list.get(0);
                    if (userInfo==null){
                        userInfo= new UserInfo();
                    }
                    if (!StringUtils.isBlank(account.getAccountNumber())){
                        userInfo.setAccountNumber(account.getAccountNumber());
                    }
                    if(!StringUtils.isBlank(account.getSource())){
                        userInfo.setSource(account.getSource());
                    }
                    if(!StringUtils.isBlank(account.getUserId())){
                        userInfo.setUserId(account.getUserId());
                    }
                    userInfo.setChannel(account.getChannel());
                }
            }
            responseData.setData(userInfo);
        } catch (Exception e) {
            responseData = ResponseData.error();
            responseData.setMsg("服务器开小差");
            LOGGER.error("nyd getExistUserInfo 获取用户信息异常 userDto："+userDto);
        }
        return responseData;
    }


	public ResponseData<List<com.nyd.user.model.t.UserInfo>> getInfoByParam(String userName,String phone,String userId) {
		ResponseData responseData = ResponseData.success();
		try{
			Map<String,String> params=new HashMap<String,String>(3);
			params.put("userName",userName);
			params.put("phone",phone);
			params.put("userId",userId);
			responseData.setData( userSourceMapper.getInfoByParam(params));
		}catch(Exception e){
			responseData = ResponseData.error();
            responseData.setMsg("查询失败");
            LOGGER.error("nyd getInfoByParam 获取用户信息异常 userName："+userName+" phone="+phone+" userId="+userId);
		}
		return responseData;
	}
}
