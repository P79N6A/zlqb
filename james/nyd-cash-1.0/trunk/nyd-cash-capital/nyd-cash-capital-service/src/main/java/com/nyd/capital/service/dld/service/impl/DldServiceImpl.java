package com.nyd.capital.service.dld.service.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.DeviceInfoContract;
import com.nyd.capital.entity.UserDld;
import com.nyd.capital.entity.UserDldLoan;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.dld.ContractSignatureParams;
import com.nyd.capital.model.dld.DldUserBalance;
import com.nyd.capital.model.dld.LoanCallBackParams;
import com.nyd.capital.model.dld.LoanCommitParams;
import com.nyd.capital.model.dld.LoanOrderQueryParams;
import com.nyd.capital.model.dld.OrderReportParams;
import com.nyd.capital.model.dld.RegisterUserParams;
import com.nyd.capital.service.UserDldService;
import com.nyd.capital.service.dld.config.AppConst;
import com.nyd.capital.service.dld.config.DldConfig;
import com.nyd.capital.service.dld.service.DldService;
import com.nyd.capital.service.dld.service.IbankDeviceContract;
import com.nyd.capital.service.dld.utils.D2DUtil;
import com.nyd.capital.service.dld.utils.DateUtil;
import com.nyd.capital.service.dld.utils.HttpClientUtil;
import com.nyd.capital.service.dld.utils.JsonUtils;
import com.nyd.capital.service.dld.utils.RSAUtil;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.jx.util.PasswordUtil;
import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 多来点借款服务对接
 *
 * @author zhangdk
 */

@Service
public class DldServiceImpl implements DldService {

    Logger logger = LoggerFactory.getLogger(DldServiceImpl.class);

    private static final String REGISTER_USER_SUFFIX = "/Service/RegisterMerUserInfo";
    private static final String LOAN_SUBMIT_SUFFIX = "/Service/LoanMerPay";
    private static final String CONTRACT_SIGN_SUFFIX = "/Service/ContractSignature";
    private static final String LOAN_REPORT_SUFFIX = "/Service/GetLoanReport";
    private static final String LOAN_ORDER_QUERY_SUFFIX = "/Service/LoanOrderQuery";
    private static final String LOAN_USER_BALANCE_SUFFIX = "/Service/UserBalanceQuery";

    @Autowired
    private DldConfig dldConfig;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private DeviceInfoContract deviceInfoContractNyd;
    @Autowired
    private UserDldService userDldService;
    @Autowired
    private UserBankContract userBankContract;

    @Autowired(required = false)
    private OrderContract orderContract;
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Autowired
    private IbankDeviceContract ibankDeviceContractYmt;


    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData registerUserByUserId(String userId, String ibankUserId) {
        // 查询客户基本信息及详情
        logger.info("+++++++用户ID：" + userId);
        UserInfo user = userIdentityContract.getUserInfo(userId).getData();
        logger.info("+++++++用户信息：" + JSON.toJSONString(user));
        //手机号为空会报错
        user.setAccountNumber(user.getUserId());
        RegisterUserParams params = new RegisterUserParams();
        params.setIdentityNumber(user.getIdNumber());
        params.setPhone(user.getAccountNumber());
        params.setPpFlag("01");
        params.setRealName(user.getRealName());
        File file1 = null;
        File file2 = null;
        try {
            file1 = getFileFromDownloadUrl(userId, ibankUserId, "1");
            file2 = getFileFromDownloadUrl(userId, ibankUserId, "2");
        } catch (Exception e) {
            logger.error("*******获取用户身份证资料异常：" + e.getMessage());
            return ResponseData.error("获取用户身份证资料失败");
        }
        params.setPhotoOne(file1);
        params.setPhotoTwo(file2);
        return registerUser(params, userId, ibankUserId, file1, file2);
    }


    private File getFileFromDownloadUrl(String userId, String ibankUserId, String type) throws Exception {
        logger.info("客户userId:{},客户ymt userId:{}",userId,ibankUserId);
        String urlD = "";
        if(ibankUserId == null || ibankUserId == "") {
            urlD = deviceInfoContractNyd.getAttachmentModelUrl(userId, type);
        }else {
            urlD = ibankDeviceContractYmt.getAttachmentModelUrl(ibankUserId, type);
        }
        if(urlD == null || urlD == "") {
            urlD = deviceInfoContractNyd.getAttachmentModelUrl(userId, type);
        }
        logger.info("获取下载地址：{}",urlD);
        URL url = new URL(urlD);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        String filename = urlD.substring(urlD.indexOf("e=") + 2, urlD.indexOf("&token"));
        // 读文件流
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(dldConfig.getTempFilePath() + File.separatorChar + filename + type + ".jpg"));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }

        } catch (Exception e) {
            logger.error("关闭流异常：" + e.getMessage());
        }
        //logger.info(new File(dldConfig.getTempFilePath()+File.separatorChar+filename +type+ ".temp").length()+"");
        return new File(dldConfig.getTempFilePath() + File.separatorChar + filename + type + ".jpg");

    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData registerUser(RegisterUserParams params, String userId, String orderId, File file1, File file2) {
        logger.info("+++++++接收借款客户信息：" + JSON.toJSONString(params));
        //签名
        Map<String, Object> signParam = signature(params);
        //转换借款客户信息请求参数
        Map<String, ContentBody> reqParam = convert2RegisterReqParam(params, signParam);
        //用户信息入库
        /*if (!insertRegister(params, userId)) {
            return ResponseData.error("借款客户信息入库异常！");
        }*/
        String retVal = "";
        try {
            //logger.info("+++++++++多来点借款客户信息上送接口参数：" + JSON.toJSONString(reqParam));
            retVal = HttpClientUtil.postFileMultiPart(dldConfig.getTestUrl() + REGISTER_USER_SUFFIX, reqParam);
            logger.info("----------多来点借款客户信息推送响应结果：" + retVal);
        } catch (ClientProtocolException e) {
            logger.error("*********推送借款客户信息异常:" + e.getMessage());
            return new ResponseData().error("推送借款客户信息异常！");
        } catch (IOException e) {
            logger.error("**********推送借款客户信息异常:" + e.getMessage());
            return new ResponseData().error("推送借款客户信息异常！");
        } finally {
            gcResorve(file1, file2);
        }
        if (null != retVal && retVal.indexOf("customerId") >= 0) {
            //updateRegister(userId, retVal);
            insertRegister(params, userId,retVal);
        } else {
            logger.info("上送用户信息失败，响应信息：" + retVal);
            return ResponseData.error("上送用户信息失败" + retVal);
        }
        return ResponseData.success(retVal);
    }

    private void gcResorve(File file1, File file2) {
        System.gc();
        if (file1.exists()) {
            file1.delete();
        }
        if (file2.exists()) {
            file2.delete();
        }
    }

    private boolean insertRegister(RegisterUserParams params, String userId,String retVal) {
        boolean flag = true;
        UserDld dld = new UserDld();
        dld.setUserId(userId);
        dld.setMobile("");
        dld.setStage(1);
        dld.setDldCustomerId(JsonUtils.getValue(retVal, 3, new String[]{"data", "data", "customerId"}).toString());
        try {
            userDldService.save(dld);
        } catch (Exception e) {
            logger.error("********客户信息入库异常：" + e.getMessage());
            flag = false;
        }
        return flag;
    }

    private boolean updateRegister(String userId, String retVal) {
        boolean flag = true;
        UserDld dld = new UserDld();
        dld.setUserId(userId);
        dld.setStage(1);
        dld.setDldCustomerId(JsonUtils.getValue(retVal, 3, new String[]{"data", "data", "customerId"}).toString());
        try {
            userDldService.update(dld);
        } catch (Exception e) {
            logger.error("更新数据异常：" + e.getMessage());
            flag = false;
        }
        return flag;
    }

    /**
     * 请求参数签名
     *
     * @param params
     * @return
     */
    private Map<String, Object> signature(RegisterUserParams params) {
        Map<String, Object> signParam = new HashMap<String, Object>();
        signParam.put("Version", AppConst.SHOP_VERSION);
        signParam.put("UserId", dldConfig.getShopID());
        signParam.put("UserKey", dldConfig.getShopKEY());
        signParam.put("Signature", dldConfig.getShopKEY());
        signParam.put("RealName", params.getRealName());
        signParam.put("IdentityNumber", params.getIdentityNumber());
        signParam.put("Phone", params.getPhone());
        signParam.put("PpFlag", params.getPpFlag());
        Set<String> removeKey = new HashSet<String>();
        removeKey.add("Signature");
        removeKey.add("PhotoOne");
        removeKey.add("PhotoTwo");
        removeKey.add("PhotoThree");
        D2DUtil.setSignature(signParam, removeKey, dldConfig.getShopKEY());
        return signParam;

    }

    /**
     * 转换借款客户信息请求参数。
     *
     * @param params
     * @param signParam
     * @return
     */
    private Map<String, ContentBody> convert2RegisterReqParam(RegisterUserParams params, Map<String, Object> signParam) {
        Map<String, ContentBody> reqParam = new HashMap<String, ContentBody>();
        reqParam.put("Version", new StringBody(AppConst.SHOP_VERSION, ContentType.DEFAULT_TEXT));
        reqParam.put("UserId", new StringBody(dldConfig.getShopID(), ContentType.DEFAULT_TEXT));
        reqParam.put("UserKey", new StringBody(dldConfig.getShopKEY(), ContentType.DEFAULT_TEXT));
        reqParam.put("Signature", new StringBody(String.valueOf(signParam.get("Signature")), ContentType.DEFAULT_TEXT));
        reqParam.put("RealName", new StringBody(RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getRealName()),
                ContentType.DEFAULT_TEXT));
        reqParam.put("IdentityNumber", new StringBody(
                RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getIdentityNumber()), ContentType.DEFAULT_TEXT));
        reqParam.put("Phone",
                new StringBody(RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getPhone()), ContentType.DEFAULT_TEXT));
        reqParam.put("PpFlag", new StringBody(params.getPpFlag(), ContentType.DEFAULT_TEXT));

        reqParam.put("PhotoOne", new FileBody(params.getPhotoOne(), ContentType.DEFAULT_BINARY));
        reqParam.put("PhotoTwo", new FileBody(params.getPhotoTwo(), ContentType.DEFAULT_BINARY));
        return reqParam;
    }

    @Override
    public ResponseData loanSubmitByOrderNo(String userId, String orderNo, String custId) {
        OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo.substring(0,18)).getData();
        LoanCommitParams params = new LoanCommitParams();
        params.setCard(orderInfo.getBankAccount());
        BankInfo bankInfo = null;
        ResponseData<List<BankInfo>> bankInfos = userBankContract.getBankInfosByBankAccout(orderInfo.getBankAccount());
        if ("0".equals(bankInfos.getStatus())) {
            List<BankInfo> bankInfoList = bankInfos.getData();
            bankInfo = bankInfoList.get(0);
        }
        UserInfo user = userIdentityContract.getUserInfo(userId).getData();
        params.setCustom(bankInfo.getAccountName());
        params.setPhoneNo(bankInfo.getReservedPhone());
        params.setCertifyId(user.getIdNumber());
        params.setLoanAmt(orderInfo.getLoanAmount() + "");
        params.setLoanDays(orderInfo.getBorrowTime() + "");
        params.setLoanInterestrate(orderInfo.getAnnualizedRate() + "");
        params.setPurpose(orderInfo.getLoanPurpose());
        params.setPeriodization(orderInfo.getBorrowPeriods() + "");
        params.setPeriodizationDays(orderInfo.getBorrowTime().toString());//每期天数，无
        params.setPeriodizationFee(orderInfo.getRealLoanAmount().toString());//每期还款金额，无
        params.setCustomerId(custId);
        params.setLoanEndDate(DateUtil.parseDateToStr(DateUtils.addDays(new Date(), orderInfo.getBorrowTime()), DateUtil.DATE_FORMAT_YYYYMMDD));
        return loanSubmit(params, orderNo, userId);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData loanSubmit(LoanCommitParams params, String orderNo, String userId) {

        Map<String, Object> reqParam = new HashMap<String, Object>();
        String merOrderNo = orderNo+"_" + System.currentTimeMillis();
        reqParam.put("Version", AppConst.SHOP_VERSION);
        reqParam.put("UserId", dldConfig.getShopID());
        reqParam.put("UserKey", dldConfig.getShopKEY());
        reqParam.put("Signature", dldConfig.getShopKEY());
        reqParam.put("MerOrderNo", merOrderNo);
        reqParam.put("Cur", "CNY");
        reqParam.put("Card", params.getCard());
        reqParam.put("Custom", params.getCustom()); // 持卡人姓名/公司名称 string 必填 (RSA加密处理【对公代付则填公司名称】)
        reqParam.put("PhoneNo", params.getPhoneNo()); // 手机号码 string 条件 (如需四要素鉴权则必填)
        reqParam.put("CertifyTp", "01"); // 证件类型 string 必填 定值"01";
        reqParam.put("CertifyId", params.getCertifyId()); // 证件ID(身份证) string 条件 (RSA加密处理【如需四要素鉴权则必填】)
        reqParam.put("PpFlag", "01"); // 对公对私标识 string 必填 (00=对公 01=对私)
        reqParam.put("LoanAmt", params.getLoanAmt()); // 借款金额 string 必填 (格式0.00)
        reqParam.put("Charge", "0.00"); // 借款服务费费率 string 必填 (格式0.00,如18%则填18.00)
        reqParam.put("LoanDays", params.getLoanDays()); // 借款天数 string 必填
        reqParam.put("LoanInterestrate", params.getLoanInterestrate()); // 借款年利率 string 必填 (格式0.00,如18%则填18.00)
        reqParam.put("Purpose", params.getPurpose()); // 借款用途 string 必填
        reqParam.put("Periodization", params.getPeriodization()); // 分期数 string 必填
        reqParam.put("PeriodizationDays", params.getPeriodizationDays()); // 每期天数 string 必填
        reqParam.put("PeriodizationFee", params.getPeriodizationFee()); // 每期金额 string 必填 (格式0.00)
        reqParam.put("TpMerId", dldConfig.getPTpMerId()); // 保理公司第三方商户号 string 必填
        reqParam.put("MerCallBackUrl", dldConfig.getMerCallBackUrl());
        reqParam.put("CustomerId", params.getCustomerId()); // 借款人编号 string 必填 注册借款人时返回参数
        if(params.getLoanEndDate() != null) {
            reqParam.put("LoanEndDate", params.getLoanEndDate());
        }

        logger.info("++++++++多带点签名之前请求参数：" + JSON.toJSONString(reqParam));
        D2DUtil.setSignature(reqParam, dldConfig.getShopKEY());

        reqParam.put("Card", RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getCard()));
        reqParam.put("Custom", RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getCustom()));
        reqParam.put("CertifyId", RSAUtil.EncodePwd(dldConfig.getShopRSA(), params.getCertifyId()));
        try {
            reqParam.put("MerCallBackUrl",
                    Base64.encodeBase64String(dldConfig.getMerCallBackUrl().getBytes(AppConst._Encoding))); // 商户回调通知地址
            // string 必填
            // (base64编码)
        } catch (UnsupportedEncodingException e1) {
            logger.error("***********回调地址转码异常：" + e1.getMessage());
            return ResponseData.error("转换请求参数异常！");
        }
        UserDldLoan loan = new UserDldLoan();
        loan.setCard(params.getCard());
        loan.setUserId(userId);
        loan.setCertifyId(params.getCertifyId());
        loan.setChannalCode("DLD");
        loan.setCustom(params.getCustom());
        loan.setCustomerId(params.getCustomerId());
        loan.setLoanAmt(params.getLoanAmt());
        loan.setLoanDays(params.getLoanDays());
        loan.setMerOrderNo(merOrderNo);
        loan.setMobile(params.getPhoneNo());
        loan.setPurpose(params.getPurpose());
        loan.setPeriodization(params.getPeriodization());
        try {
            userDldService.saveLoan(loan);
        } catch (Exception e) {
            logger.error("********借款请求入库异常：" + e.getMessage());
            //return ResponseData.error("借款请求入库异常");
        }
        String retVal = "";
        try {
            logger.info("++++++++多带点借款请求参数：" + JSON.toJSONString(reqParam));
            retVal = HttpClientUtil.post(dldConfig.getTestUrl() + LOAN_SUBMIT_SUFFIX, reqParam);
            logger.info("---------多来点借款请求服务响应信息：" + retVal);
            if (null != retVal && retVal.indexOf("TS1001") >= 0) {
            	if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + merOrderNo)) {
                    logger.error("dld借款结果已处理");
                    throw new Exception("dld借款结果已处理");
                } else {
                    redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + merOrderNo, "1",10,TimeUnit.HOURS);
                }
                loan.setStage(1);
            } else if (null != retVal && retVal.indexOf("TS1111") >= 0) {
                loan.setStage(2);
            } else {
                loan.setStage(3);
            }
            userDldService.updateLoan(loan);
        } catch (Exception e) {
            logger.info("*******请求多来点借款请求服务异常：" + e.getMessage());
            if(e.getMessage().indexOf("Read") >= 0) {
            	redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + merOrderNo);
            }
            //return ResponseData.error("多来点借款请求异常！");
        }
        return ResponseData.success(retVal);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData uploadLoanPro(String params) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData getContractDownloadUrlByOrderNo(String orderNo) {
        ContractSignatureParams params = new ContractSignatureParams();
        params.setLoanOrderNo(orderNo);
        return getContractDownloadUrl(params);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData getContractDownloadUrl(ContractSignatureParams params) {
        Map<String, Object> reqParam = new HashMap<String, Object>();
        reqParam.put("Version", AppConst.SHOP_VERSION);
        reqParam.put("UserId", dldConfig.getShopID());
        reqParam.put("UserKey", dldConfig.getShopKEY());
        reqParam.put("Signature", dldConfig.getShopKEY());
        reqParam.put("LoanOrderNo", params.getLoanOrderNo());
        D2DUtil.setSignature(reqParam, dldConfig.getShopKEY());
        reqParam.put("LoanOrderNo", (String) reqParam.get("LoanOrderNo"));
        String retVal = "";
        try {
            retVal = HttpClientUtil.post(dldConfig.getTestUrl() + CONTRACT_SIGN_SUFFIX, reqParam);
            logger.info("-----------多来点合同下载地址请求响应信息：" + retVal);
            if (null != retVal && retVal.indexOf("contractUrl") >= 0) {
                UserDldLoan loan = userDldService.getUserDldLoanByOrderNo(params.getLoanOrderNo());
                loan.setContractUrl(JsonUtils.getValue(retVal, 3, new String[]{"data", "data", "contractUrl"}).toString());
                userDldService.updateLoan(loan);
            }
        } catch (Exception e) {
            logger.error("*********请求多来点合同下载地址服务异常：" + e.getMessage());
            return ResponseData.error("请求多来点合同下载地址服务异常");
        }
        return ResponseData.success(retVal);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData getLoanReport(OrderReportParams params) {
        Map<String, Object> reqParam = new HashMap<String, Object>();
        reqParam.put("Version", AppConst.SHOP_VERSION);
        reqParam.put("UserId", dldConfig.getShopID());
        reqParam.put("UserKey", dldConfig.getShopKEY());
        reqParam.put("Signature", dldConfig.getShopKEY());
        reqParam.put("PageIndex", params.getPageIndex());
        reqParam.put("StartDate", params.getStartDate());
        reqParam.put("EndDate", params.getEndDate());
        D2DUtil.setSignature(reqParam, dldConfig.getShopKEY());
        String retVal = "";
        try {
            retVal = HttpClientUtil.post(dldConfig.getTestUrl() + LOAN_REPORT_SUFFIX, reqParam);
            logger.info("-----------多来点多来点订单报表请求响应信息：" + retVal);
        } catch (Exception e) {
            logger.info("******请求多来点订单报表服务异常：" + e.getMessage());
            return ResponseData.error("请求多来点订单报表服务异常");
        }
        return ResponseData.success(retVal);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public BigDecimal userBalanceQuery() {
    	Map<String, Object> reqParam = new HashMap<String, Object>();
        reqParam.put("Version", AppConst.SHOP_VERSION);
        reqParam.put("UserId", dldConfig.getShopID());
        reqParam.put("UserKey", dldConfig.getShopKEY());
        D2DUtil.setSignature(reqParam, dldConfig.getShopKEY());
        String retVal = null;
        BigDecimal balanceR = null;
		try {
			retVal = HttpClientUtil.post(dldConfig.getTestUrl() + "/Service/UserBalanceQuery", reqParam);
			logger.info("-----多来点账户信息查询响应结果：" + retVal);
			JSONObject data = JSON.parseObject(retVal);
			String balance = data.getString("data");
			logger.info(balance);
			if(data == null || null == balance) {
				return null;
			}
			balanceR = new BigDecimal(balance);
			String redisKey = Constants.DLD_USER_BALANCE_PREFIX + dldConfig.getShopID();
			DldUserBalance balanceO = new DldUserBalance();
			if (redisTemplate.hasKey(redisKey)) {
				balanceO = (DldUserBalance)JSON.parseObject((String)redisTemplate.opsForValue().get(redisKey), DldUserBalance.class);
				if(balanceO.getFirstBalance().compareTo(balanceO.getWarnBalance()) >= 0 && balanceR.compareTo(balanceO.getWarnBalance()) <= 0) {
					DingdingUtil.getErrMsg("多来点渠道账户余额预警，余额为：" + balanceR + "！@周玲");
				}
				balanceO.setFirstBalance(balanceR);
				balanceO.setQueryTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS));
            } else {
            	balanceO.setFirstBalance(balanceR);
            	balanceO.setWarnBalance(dldConfig.getWarnBalance());
            	balanceO.setQueryTime(DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS));
            }
			redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(balanceO));
		} catch (Exception e) {
			logger.error("******多来点账户余额查询异常：" + e.getMessage());
			return null;
		}
		return balanceR;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData loanOrderQueryByOrderNo(String orderNo) {
        LoanOrderQueryParams params = new LoanOrderQueryParams();
        params.setMerOrderNo(orderNo);
        return loanOrderQuery(params);
    }

    @Override
    public ResponseData errerHandle(OrderMessage message) {
        logger.info("多来点补发消息:" + JSON.toJSON(message));
        if (StringUtils.isBlank(message.getOrderNo())) {
            return ResponseData.error("订单号不能为空");
        }
        if (!(message.getMqType() == 1 || message.getMqType() == 2 || message.getMqType() == 3 || message.getMqType() == 4)) {
            return ResponseData.error("请传入正确的消息类型!");
        }
        try {

            //资产提交
            String orderNo = message.getOrderNo();
            String userId = message.getUserId();
            String customerId = message.getCustomerId();
            String respCode = null;

            //借款请求成功
            OrderInfo orderInfo = null;
            try {
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                if (orderInfo == null) {
                    logger.info("根据订单号号查询订单不存在");
                    return ResponseData.error("根据订单号号查询订单不存在");
                }
            } catch (Exception e) {
                logger.error("根据订单号查询订单发生异常");
                return ResponseData.error("根据订单号查询订单发生异常");
            }
            logger.info("orderInfo:" + JSON.toJSONString(orderInfo));


            if (message.getMqType()==4) {
                ResponseData responseData = loanSubmitByOrderNo(userId, orderNo+ PasswordUtil.getPassword(), customerId);
                if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                    return ResponseData.error("发起多来点借款失败");
                }
                String dataData = (String) responseData.getData();
                JSONObject data = JSON.parseObject(dataData);
                JSONObject jsonObject = data.getJSONObject("data");
                if (jsonObject == null) {
                    String msg = data.getString("msg");
                    return ResponseData.error("多来点发起借款申请失败," + "msg:" + msg + ",订单号为:" + orderNo);
                }
                respCode = jsonObject.getString("respCode");
            }


            if (message.getMqType() == 1 || (StringUtils.isNotBlank(respCode) && "TS1001".equals(respCode))) {
                //放款成功,通知zues
                RemitMessage remitMessage = new RemitMessage();
                remitMessage.setRemitStatus("0");
                remitMessage.setRemitAmount(orderInfo.getLoanAmount());
                remitMessage.setOrderNo(orderInfo.getOrderNo());
                rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
                //如果是null 默认为nyd的订单来源
                if (orderInfo.getChannel() == null) {
                    orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
                }

                //发送 到 ibank
                if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                    remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
                    logger.info("放款成功发送ibank." + JSON.toJSONString(remitMessage));
                    rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
                }
            }
            if (message.getMqType() == 1 || message.getMqType() == 2  || (StringUtils.isNotBlank(respCode) && "TS1001".equals(respCode))) {
                RemitInfo remitInfo = new RemitInfo();
                remitInfo.setRemitTime(new Date());
                remitInfo.setOrderNo(orderInfo.getOrderNo());
                remitInfo.setRemitStatus("0");
                remitInfo.setFundCode("dld");
                remitInfo.setChannel(orderInfo.getChannel());
                remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                logger.info("放款流水:" + JSON.toJSON(remitInfo));
                try {
                    rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                    logger.info("放款流水发送mq成功");
                } catch (Exception e) {
                    logger.error("发送mq消息发生异常");
                    return ResponseData.error("发送放款记录mq发生异常");
                }
            }

            if (message.getMqType() == 3) {
                //借款失败
                orderInfo.setOrderStatus(40);
                //生成异常订单记录
              //生成异常订单记录
                try {
                	orderExceptionContract.saveByOrderInfo(orderInfo);
                }catch(Exception e) {
                	logger.error("生成异常订单信息异常：" + e.getMessage());
                }
                orderContract.updateOrderInfo(orderInfo);
                return ResponseData.success();
            }
        } catch (Exception e) {
            logger.error("多来点补发消息!", e);
            return ResponseData.error("多来点补发消息");
        }
        return ResponseData.success();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResponseData loanOrderQuery(LoanOrderQueryParams params) {
        Map<String, Object> reqParam = new HashMap<String, Object>();
        reqParam.put("Version", AppConst.SHOP_VERSION);
        reqParam.put("UserId", dldConfig.getShopID());
        reqParam.put("UserKey", dldConfig.getShopKEY());
        reqParam.put("MerOrderNo", params.getMerOrderNo());
        D2DUtil.setSignature(reqParam, dldConfig.getShopKEY());
        String retVal = "";
        try {
            logger.info("++++++++查询订单详情请求参数：" + JSON.toJSONString(reqParam));
            retVal = HttpClientUtil.post(dldConfig.getTestUrl() + LOAN_ORDER_QUERY_SUFFIX, reqParam);
            logger.info("-------多来点借款订单查询响应信息：" + retVal);
            //String res = JsonUtils.getValue(retVal, 3, new String[] {"data","data","contractUrl"}).toString();
        } catch (Exception e) {
            logger.error("**********请求多来点借款订单查询服务异常：" + e.getMessage());
            return ResponseData.error("请求多来点借款订单查询服务异常");
        }
        return ResponseData.success(retVal);
    }

    @Override
    public String callBack(Map<String, Object> map) {
        // 避免重复的通知
    	String merOrderNo = (String)map.get("orderid");
    	String orderNo = merOrderNo.split("_")[0];
        try {
            if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + merOrderNo)) {
                logger.error("有重复通知" + JSON.toJSONString(map));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + merOrderNo, "1",24*60,TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            logger.error("写redis出错" , e);
        }
        //防止跑批中已经处理
        try {
            if (redisTemplate.hasKey(Constants.CAPITAL_QUERY_KEY + merOrderNo)) {
                logger.error("跑批中已经处理" + JSON.toJSONString(map));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.CAPITAL_QUERY_KEY + merOrderNo, "1",24*60,TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            logger.error("写redis出错" + e.getMessage());
        }

        logger.info("多来点放款通知："+JSON.toJSONString(map));
        UserDldLoan loan = new UserDldLoan();
        loan.setMerOrderNo(merOrderNo);
        if ("1001".equals((String) map.get("code"))) {
            loan.setStage(1);
        } else {
            loan.setStage(3);
        }

        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus("0");
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            if (orderInfo == null) {
                logger.info("根据订单号号查询订单不存在");
                return "fail";
            }
            if(orderInfo.getOrderStatus().equals(50)) {
            	return "success";
            }
        } catch (Exception e) {
            logger.error("根据订单号查询订单发生异常");
            return "fail";
        }
        logger.info("orderInfo:" + JSON.toJSONString(orderInfo));
        remitMessage.setRemitAmount(orderInfo.getLoanAmount());

        if (loan.getStage() == 1) {
            remitMessage.setOrderNo(orderInfo.getOrderNo());
            remitMessage.setRemitTime(DateUtil.parseStrToDate((String)map.get("LentTime"),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
            rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
        }
        //如果是null 默认为nyd的订单来源
        if (orderInfo.getChannel() == null) {
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }

        //发送 到 ibank
        if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
            if (loan.getStage() == 1) {
                remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
                logger.info("放款成功发送ibank." + JSON.toJSONString(remitMessage));
                rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
            }
        }

        RemitInfo remitInfo = new RemitInfo();
        
        remitInfo.setRemitTime(DateUtil.parseStrToDate((String)map.get("LentTime"),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
        remitInfo.setOrderNo(orderInfo.getOrderNo());
        if (loan.getStage() == 1) {
            remitInfo.setRemitStatus("0");
        } else {
            remitInfo.setRemitStatus("1");
        }
        remitInfo.setFundCode("dld");
        remitInfo.setRemitNo(merOrderNo);
        remitInfo.setChannel(orderInfo.getChannel());
        remitInfo.setRemitAmount(orderInfo.getLoanAmount());
        logger.info("放款流水:" + JSON.toJSON(remitInfo));
        try {
            rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
            logger.info("放款流水发送mq成功");
        } catch (Exception e) {
            logger.error("发送mq消息发生异常");
        }
        try {
            userDldService.updateLoan(loan);
        } catch (Exception ex) {
            logger.error("更新订单状态失败：" + ex.getMessage());
            //return "fail";
        }
        return "success";
    }
    @Override
    public String callBackRetry(LoanCallBackParams map) {
    	
    	logger.info(map.getFundCode() + "放款通知："+JSON.toJSONString(map));
    	UserDldLoan loan = new UserDldLoan();
    	loan.setMerOrderNo(map.getOrderid());
    	if ("1001".equals( map.getCode())) {
    		loan.setStage(1);
    	} else {
    		loan.setStage(3);
    	}
    	
    	RemitMessage remitMessage = new RemitMessage();
    	remitMessage.setRemitStatus("0");
    	OrderInfo orderInfo = null;
    	try {
    		orderInfo = orderContract.getOrderByOrderNo(loan.getMerOrderNo().substring(0,18)).getData();
    		if (orderInfo == null) {
    			logger.info("根据订单号号查询订单不存在");
    			logger.info(DateUtil.parseStrToDate(map.getLentTime(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_).toString());
    			return "fail";
    		}
    	} catch (Exception e) {
    		logger.error("根据订单号查询订单发生异常");
    		return "fail";
    	}
    	logger.info("orderInfo:" + JSON.toJSONString(orderInfo));
    	remitMessage.setRemitAmount(orderInfo.getLoanAmount());
    	
    	if (loan.getStage() == 1) {
    		remitMessage.setOrderNo(orderInfo.getOrderNo());
    		remitMessage.setRemitTime(DateUtil.parseStrToDate(map.getLentTime(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_));
    		rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
    	}
    	//如果是null 默认为nyd的订单来源
    	if (orderInfo.getChannel() == null) {
    		orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
    	}
    	
    	//发送 到 ibank
    	if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
    		if (loan.getStage() == 1) {
    			remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
    			logger.info("放款成功发送ibank." + JSON.toJSONString(remitMessage));
    			rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
    		}
    	}
    	
    	RemitInfo remitInfo = new RemitInfo();
    	
    	remitInfo.setRemitTime(DateUtil.parseStrToDate(map.getLentTime(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_));
    	remitInfo.setOrderNo(orderInfo.getOrderNo());
    	if (loan.getStage() == 1) {
    		remitInfo.setRemitStatus("0");
    	} else {
    		remitInfo.setRemitStatus("1");
    	}
    	remitInfo.setFundCode(map.getFundCode());
    	remitInfo.setChannel(orderInfo.getChannel());
    	remitInfo.setRemitAmount(orderInfo.getLoanAmount());
    	logger.info("放款流水:" + JSON.toJSON(remitInfo));
    	try {
    		rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
    		logger.info("放款流水发送mq成功");
    	} catch (Exception e) {
    		logger.error("发送mq消息发生异常");
    	}
    	try {
    		userDldService.updateLoan(loan);
    	} catch (Exception ex) {
    		logger.error("更新订单状态失败：" + loan.getMerOrderNo());
    	}
    	return "success";
    }
}
