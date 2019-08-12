package com.nyd.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.dsp.api.BankVerifyContract;
import com.nyd.dsp.model.request.yuanjin.BankFourModel;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.dao.BankDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.entity.Bank;
import com.nyd.user.entity.Step;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.BankResponse;
import com.nyd.user.model.UserInfo;
import com.nyd.user.service.BankInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/3.
 */
@Service(value = "userBankContract")
public class BankInfoServiceImpl implements BankInfoService,UserBankContract {
    private static Logger LOGGER = LoggerFactory.getLogger(BankInfoServiceImpl.class);

    @Autowired
    private UserLoginServiceImpl userLoginServiceImpl;
    @Autowired
    private BankDao bankDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StepDao stepDao;
    @Autowired(required = false)
    private BankVerifyContract bankVerifyContract;
    @Autowired
    private UserProperties userProperties;
    @Autowired(required = false)
    private AgreeMentContract agreeMentContract;
    
    private static Map<String,String> bankListMap = null;
    
    @Autowired
	RestTemplateApi restTemplateApi;

    /**
     * 保存银行卡信息
     * @param bankInfo
     * @return
     */
    @Override
    public ResponseData saveBankInfo(BankInfo bankInfo){
        LOGGER.info("begin to save bankInfo, userId is " + bankInfo.getUserId());
        ResponseData responseData = ResponseData.success();
        if(!userLoginServiceImpl.verifyMsgCode(bankInfo.getReservedPhone(),bankInfo.getSmsCode())){
            LOGGER.info("modify password failed , msgCode is wrong ! mobile" + bankInfo.getReservedPhone());
            return ResponseData.error(UserConsts.BANK_MSG_ERROR);
        }
        try {
            List<BankInfo> bankList = bankDao.getBanksByBankAccount(bankInfo.getBankAccount());
            if(bankList != null && bankList.size()>0){
                return ResponseData.error(UserConsts.BANK_ALREADY_USE);
            }
        } catch (Exception e) {
            LOGGER.error("get bankInfo error! userId = "+bankInfo.getUserId(),e);
            return ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        //就行银行卡四要素验证
        if(!judgeFourKey(bankInfo)){
            return ResponseData.error(UserConsts.BANK_FOUR_ERROR);
        }
        try {
            bankDao.save(bankInfo);
            LOGGER.info("save bankInfo success");
            try {
                agreeMentContract.signAutoRepay(bankInfo);
            } catch (Exception e) {
                LOGGER.error("saveBankInfo signAutoRepay error! userId = "+bankInfo.getUserId(),e);
            }
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("save bankInfo error! userId = "+bankInfo.getUserId(),e);
        }
        try {
            //更新信息完整度
            LOGGER.info("begin to update stepInfo");
            Step step = new Step();
            step.setUserId(bankInfo.getUserId());
            step.setBankFlag(UserConsts.FILL_FLAG);
            stepDao.updateStep(step);
            LOGGER.info("update stepInfo success");
        } catch (Exception e) {
            LOGGER.error("save bankInfo success，but update stepInfo failed! userId = "+bankInfo.getUserId(),e);
        }
        return responseData;
    }
    /**
     * 保存银行卡信息
     * @param bankInfo
     * @return
     */
    @Override
    public ResponseData saveBankInfoNoJudge(BankInfo bankInfo){
    	LOGGER.info("begin to save bankInfo, userId is " + bankInfo.getUserId());
    	ResponseData responseData = ResponseData.success();
    	try {
    		List<BankInfo> bankList = bankDao.getBanksByBankAccount(bankInfo.getBankAccount());
    		if(bankList != null && bankList.size()>0){
    			//如果是已有绑定卡，则返回成功
    			return ResponseData.success();
    		}
    	} catch (Exception e) {
    		LOGGER.error("get bankInfo error! userId = "+bankInfo.getUserId(),e);
    		return ResponseData.error(UserConsts.DB_ERROR_MSG);
    	}
    	try {
    		bankInfo.setSoure(1);
            bankInfo.setChannelCode("changjie");
            bankDao.save(bankInfo);
    		LOGGER.info("save bankInfo success");
    		try {
    			agreeMentContract.signAutoRepay(bankInfo);
    		} catch (Exception e) {
    			LOGGER.error("saveBankInfo signAutoRepay error! userId = "+bankInfo.getUserId(),e);
    		}
    	} catch (Exception e) {
    		responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
    		LOGGER.error("save bankInfo error! userId = "+bankInfo.getUserId(),e);
    	}
    	try {
    		//更新信息完整度
    		LOGGER.info("begin to update stepInfo");
    		Step step = new Step();
    		step.setUserId(bankInfo.getUserId());
    		step.setBankFlag(UserConsts.FILL_FLAG);
    		stepDao.updateStep(step);
    		LOGGER.info("update stepInfo success");
    	} catch (Exception e) {
    		LOGGER.error("save bankInfo success，but update stepInfo failed! userId = "+bankInfo.getUserId(),e);
    	}
    	return responseData;
    }

    /**
     * 获取银行卡信息
     * @param userId
     * @return
     */
    @Override
    public ResponseData<List<BankInfo>> getBankInfos(String userId){
        LOGGER.info("begin to get bankList, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            List<BankInfo> banklist = bankDao.getBanksByUserId(userId);
            responseData.setData(banklist);
            LOGGER.info("get bankList success");
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get bankInfo error! userId = "+userId,e);
        }
        return responseData;
    }
    
    @Override
    public ResponseData<List<BankInfo>> getBankInfosChang(String userId,Integer source){
        LOGGER.info("begin to get bankList, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            List<BankInfo> banklist = bankDao.getBanksByUserIdAndSource(userId, source);
            responseData.setData(banklist);
            LOGGER.info("get bankList success");
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("get bankInfo error! userId = "+userId,e);
        }
        return responseData;
    }
    /**
     * 获取银行卡信息
     * @param bankAccout
     * @return
     */
    @Override
    public ResponseData<List<BankInfo>> getBankInfosByBankAccout(String bankAccout){
    	LOGGER.info("begin to get bankList, bankAccout is " + bankAccout);
    	ResponseData responseData = ResponseData.success();
    	try {
    		List<BankInfo> banklist = bankDao.getBanksByBankAccount(bankAccout);
    		responseData.setData(banklist);
    		LOGGER.info("get bankList success");
    	} catch (Exception e) {
    		responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
    		LOGGER.error("get bankInfo error! userId = "+bankAccout,e);
    	}
    	return responseData;
    }

    /**
     * 银行卡四要素验证
     * @param bankInfo
     * @return boolean
     */
    private boolean judgeFourKey(BankInfo bankInfo){
        LOGGER.info("begin to judge bank fourKey，bankinfo is",bankInfo.toString());
        if (!"ON".equals(userProperties.getBankVerifyFlag())){ //如果没有开启验证 直接返回true
            return true;
        }
        boolean judge = false;
        BankFourModel model = new BankFourModel();
        model.setAppId(bankInfo.getUserId());
        model.setName(bankInfo.getAccountName());
        model.setMobile(bankInfo.getReservedPhone());
        model.setCardnumber(bankInfo.getBankAccount());
        //通过userId获取身份证号
        try {
            List<UserInfo> userList = userDao.getUsersByUserId(bankInfo.getUserId());
            if(userList != null && userList.size()>0){
                model.setIdnumber(userList.get(0).getIdNumber());
            }
        } catch (Exception e) {
            LOGGER.error("judge bank fourKey error! userId = "+bankInfo.getUserId(),e);
        }
        ResponseData<String> response = bankVerifyContract.bankVerify(model);
        if("0".equals(response.getStatus())) {
            String flag = response.getData();//1一致（神州融和爰金的1都代表一致）
            if("1".equals(flag)){
                judge = true;
            }
        }
        LOGGER.info("judge bank fourKey result is " + judge);
        return judge;
    }

    @Override
    public void saveBank(Bank bank) {
        try {
            bankDao.saveBank(bank);
        }catch (Exception e){
            LOGGER.error("保存银码头传输过阿里的银行卡信息失败");
        }
        try {
            //更新信息完整度
            LOGGER.info("begin to update stepInfo");
            Step step = new Step();
            step.setUserId(bank.getUserId());
            step.setBankFlag(UserConsts.FILL_FLAG);
//            stepDao.saveStep(step);
           stepDao.updateStep(step);
            LOGGER.info("update stepInfo success");
        } catch (Exception e) {
            LOGGER.error("save bankInfo success，but update stepInfo failed! userId = "+bank.getUserId(),e);
        }

    }
    @Override
    public void updateBank(Bank bank) {
    	try {
    		bankDao.updateBankByBankAccount(bank);
    	}catch (Exception e){
    		LOGGER.error("保存银码头传输过阿里的银行卡信息失败");
    	}
    }
	@Override
	public ResponseData getBankList(BankInfo bankInfo) {
		Map<String, Object> param = new HashMap<String, Object>();
		if(bankInfo == null || bankInfo.getAppName() == null) {
			param.put("appCode", "xxd");
		}else {
			param.put("appCode", bankInfo.getAppName());
		}
		LOGGER.info("查询银行卡列表参数： " + JSON.toJSONString(param));
		try {
			ResponseData resp = restTemplateApi.postForObject(userProperties.getUserCardListUri(), param,ResponseData.class);
			//汇聚支付绑卡统一使用nyd
	        if("P".equals(bankInfo.getPayOrLoanFlag())) {
	        	//2、使用JSONArray
	            String banks = JSON.toJSONString(resp.getData());
	        	List<BankResponse> list = JSON.parseArray(banks, BankResponse.class);
	        	List<BankResponse> temp = new ArrayList<BankResponse>();
	            for(BankResponse ob : list) {
	            	if(JSON.toJSONString(ob).indexOf("招商") >= 0) {
	            		continue;
	            	}
	            	if(JSON.toJSONString(ob).indexOf("邮政") >= 0) {
	            		continue;
	            	}
	            	temp.add(ob);
	            }
	            resp.setData(temp);
	        }
			LOGGER.info("查询银行卡列表响应信息：{} " , JSON.toJSONString(resp));
			return resp;
		}catch(Exception e) {
			LOGGER.error("查询银行列表失败：{}",e.getMessage());
			return ResponseData.error();
		}
	}
	@Override
	public ResponseData getXunlianBankList(BankInfo bankInfo) {
		ResponseData resp = new ResponseData();
		try {
			if(bankListMap==null||bankListMap.isEmpty()){
				bankListMap = getBankListMap();
			}
	        List<BankResponse> temp = new ArrayList<BankResponse>();
			for(Map.Entry<String, String> entry : bankListMap.entrySet()){
				BankResponse bank = new BankResponse();
			    String mapKey = entry.getKey();
			    String mapValue = entry.getValue();
			    bank.setBankCode(mapKey);
			    bank.setBankName(mapValue);
			    temp.add(bank);
			}   
			resp.setStatus("0");
	        resp.setData(temp);
			LOGGER.info("查询银行卡列表响应信息：{} " , JSON.toJSONString(resp));
			return resp;
		}catch(Exception e) {
			LOGGER.error("查询银行列表失败：{}",e.getMessage());
			resp.setStatus("1");
			return ResponseData.error();
		}
	}
	
	public Map<String,String> getBankListMap(){
		bankListMap = new HashMap<String,String>();
		bankListMap.put("4000100005", "中国工商银行");
		bankListMap.put("4000200006", "中国农业银行");
		bankListMap.put("4000300007", "中国银行");
		bankListMap.put("4000400008", "中国建设银行");
		bankListMap.put("4000500009", "邮储银行");
		bankListMap.put("4000600000", "交通银行");
		bankListMap.put("4000700001", "中信银行");
		//bankListMap.put("4000800002", "光大银行");
		bankListMap.put("4000900003", "华夏银行");
		bankListMap.put("4001000005", "民生银行");
		bankListMap.put("4001100006", "广发银行");
		//bankListMap.put("4001200007", "招商银行");
		bankListMap.put("4001300008", "兴业银行");
		bankListMap.put("4001400009", "浦发银行");
		bankListMap.put("4001500000", "平安银行");
		bankListMap.put("4001600001", "上海银行");
		bankListMap.put("4001800003", "恒丰银行");
		bankListMap.put("4001900004", "浙商银行");
		return bankListMap;
	}

}
