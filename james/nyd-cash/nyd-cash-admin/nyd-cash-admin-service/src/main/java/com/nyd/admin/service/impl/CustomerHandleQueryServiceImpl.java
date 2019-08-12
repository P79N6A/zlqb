package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.das.query.api.model.ReportType;
import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.mapper.CustomerHandleQueryMapper;
import com.nyd.admin.model.Info.CustomerQueryInfo;
import com.nyd.admin.model.Info.OrderDetailsInfo;
import com.nyd.admin.model.Info.RechargePaymentRecordInfo;
import com.nyd.admin.model.Info.WithHoldAgreementInfo;
import com.nyd.admin.model.Vo.CustomerQueryVo;
import com.nyd.admin.model.Vo.WithHoldAgreementVo;
import com.nyd.admin.model.dto.CustomerQueryDto;
import com.nyd.admin.model.dto.RechargePaymentRecordDto;
import com.nyd.admin.service.CustomerHandleQueryService;
import com.nyd.admin.service.CustomerHandleService;
import com.nyd.admin.service.utils.PagingUtils;
import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.enums.AppType;
import com.nyd.member.api.MemberLogContract;
import com.nyd.member.model.MemberLogModel;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cm
 */
@Service
public class CustomerHandleQueryServiceImpl implements CustomerHandleQueryService {
    private static Logger logger = LoggerFactory.getLogger(CustomerHandleQueryServiceImpl.class);

    @Autowired
    private CustomerHandleQueryMapper customerHandleQueryMapper;

    @Autowired
    private QiniuContract qiniuContract;

    @Autowired
    private com.creativearts.das.query.api.QiniuContract dasQiniuContract;

    @Autowired
    private UserAccountContract userAccountContract;
    @Autowired
    private CustomerHandleService customerHandleService;
    @Autowired
    private UserBankContract userBankContract;

    @Autowired
    private MemberLogContract memberLogContract;

    @Autowired
    private MongoTemplate mongoAppTemplate;

    /**
     * 客户处理查询
     * @param customerQueryDto
     * @return
     */
    @Override
    public ResponseData findAllCustomer(CustomerQueryDto customerQueryDto) {
        ResponseData response = ResponseData.success();
        /*if (!StringUtils.isNotBlank(customerQueryDto.getAccountNumber())){
            return ResponseData.error("请输入手机号查询");
        }*/
        List<CustomerQueryVo> customerQueryVoList = new ArrayList<>();
        try {
            List<CustomerQueryInfo> list = customerHandleQueryMapper.getAllCustomer(customerQueryDto);
            logger.info("根据手机号找到的客户信息:"+ JSON.toJSON(list));
            if (list != null && list.size() > 0){
                for(CustomerQueryInfo customerQueryInfo : list){
                    CustomerQueryVo customerQueryVo = new CustomerQueryVo();
                    BeanUtils.copyProperties(customerQueryInfo,customerQueryVo);
                    //app名称(如果APP名称没找到,就默认为nyd)
                    if (!StringUtils.isNotBlank(customerQueryInfo.getAppName())){
                        if ("ymt".equals(customerQueryInfo.getSource())){
                            customerQueryVo.setAppName("ymt");
                        }else {
                            customerQueryVo.setAppName("nyd");
                        }

                    }
                    //身份证脱敏处理
                    String idNumber = customerQueryInfo.getIdNumber();
                    String newIdNumber = idHide(idNumber);
                    customerQueryVo.setIdNumber(newIdNumber);

                    //注册时间
                    Date time = customerQueryInfo.getCreateTime();
//                    String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(time);
                    SimpleDateFormat tokyoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
                    String dateStr = tokyoSdf.format(time);
                    customerQueryVo.setRegisterTime(dateStr);


                    //账户总余额
                    BigDecimal totalCount = new BigDecimal(0);
                    ResponseData<AccountDto> data = userAccountContract.selectByAccountNumber(customerQueryDto.getAccountNumber().trim());
                    if ("0".equals(data.getStatus())){
                        AccountDto accountDto = data.getData();
                        logger.info("根据手机号找到对应人的账户信息:"+JSON.toJSON(accountDto));
                        BigDecimal balance = new BigDecimal(0);
                        BigDecimal returnBalance = new BigDecimal(0);
                        if (accountDto.getBalance().compareTo(new BigDecimal(0)) == 1 || accountDto.getBalance().compareTo(new BigDecimal(0)) == 0){
                             balance = accountDto.getBalance();
                        }
                        if (accountDto.getReturnBalance().compareTo(new BigDecimal(0)) == 1 || accountDto.getReturnBalance().compareTo(new BigDecimal(0)) == 0){
                            returnBalance = accountDto.getReturnBalance();
                        }

                        totalCount = balance.add(returnBalance);
                    }
                    customerQueryVo.setTotalCount(totalCount);

                    //绑卡信息
                    String userId = customerQueryInfo.getUserId();
                    ResponseData<List<BankInfo>> datas = userBankContract.getBankInfos(userId);
                    if ("0".equals(datas.getStatus())){
                        List<BankInfo> bankInfos = datas.getData();
                        if (bankInfos != null && bankInfos.size() > 0){
//                            BankInfo bankInfo = bankInfos.get(bankInfos.size() - 1);
                            BankInfo bankInfo = bankInfos.get(0);
                            logger.info("userId:"+userId+",对应的绑卡信息:"+JSON.toJSON(bankInfo));
                            //银行名称
                            customerQueryVo.setBankName(bankInfo.getBankName());
                            //银行卡号
                            customerQueryVo.setCardNo(hideCardNo(bankInfo.getBankAccount()));
                            //预留手机号
                            customerQueryVo.setReserveMobile(hidePhoneNo(bankInfo.getReservedPhone()));
                        }
                    }


                    logger.info("客户处理查询,返回给前台的对象:"+JSON.toJSON(customerQueryVo));
                    customerQueryVoList.add(customerQueryVo);
                }
                response.setData(customerQueryVoList);
            }else {
                ResponseData.error("未找到该用户信息,请核对手机号码");
            }

        }catch (Exception e){
            logger.error("客户处理查询出错啦",e);
        }

        return response;
    }

    /**
     * 充值付费记录
     * @param rechargePaymentRecordDto
     * @return
     */
    @Override
    public ResponseData findrechargePaymentRecordsByUserId(RechargePaymentRecordDto rechargePaymentRecordDto) {
        ResponseData responseData = ResponseData.success();
        if(StringUtils.isBlank(rechargePaymentRecordDto.getUserId())){
            return responseData.error("userId is null~~~~");
        }
        try {
            Integer pageNum = rechargePaymentRecordDto.getPageNum();
            Integer pageSize = rechargePaymentRecordDto.getPageSize();
            if (pageNum == null) {
                pageNum = 1;
            }
            if (pageSize == null || pageSize == 0) {
                pageSize = 20;
            }
            //Integer total = customerHandleService.findPayAssessCount(rechargePaymentRecordDto);
            //logger.info("充值付费记录总个数 total is " + total);
            List<RechargePaymentRecordInfo> resultList = customerHandleService.findRechargePaymentDetails(rechargePaymentRecordDto);
            PagingUtils page = PagingUtils.pagination(resultList.size(), pageSize, pageNum);
            Integer fromIndex  = page.getQueryIndex();//每页开始查询的下标
            int toIndex = 0;//每页的结束下标
            if (fromIndex + page.getPageSize() >= resultList.size()){
                toIndex = resultList.size();
            }else {
                toIndex = fromIndex +  page.getPageSize();
            }
            if (fromIndex > toIndex){
                return responseData.setData(Collections.EMPTY_LIST);
            }

            Collections.sort(resultList, new Comparator<RechargePaymentRecordInfo>() {
                @Override
                public int compare(RechargePaymentRecordInfo o1, RechargePaymentRecordInfo o2) {
                    return o2.getCreateTime().compareTo(o1.getCreateTime());
                }
            });
            logger.info("查询到用户充值记录结果是result===" + resultList);
            List<RechargePaymentRecordInfo> list = resultList.subList(fromIndex, toIndex);
            PageInfo pageInfo = new PageInfo(list);
            pageInfo.setTotal(resultList.size());
            responseData.setData(pageInfo);
        } catch (Exception e) {
            logger.error("用户充值付费记录出错~~~~", e);
            return responseData.error("服务器开小差了！");
        }
        return responseData;
    }

    /**
     * 风险速查报告
     * @param rechargePaymentRecordDto
     * @return
     */
    @Override
    public ResponseData findRiskInspectionReport(RechargePaymentRecordDto rechargePaymentRecordDto) {
        ResponseData response = ResponseData.success();
       if(!(rechargePaymentRecordDto != null && rechargePaymentRecordDto.getUserId() != null)){
            return response.error("rechargePaymentRecordDto is null or userId is null~~~~");
        }
        String filePath = null;
        try {
            response = dasQiniuContract.downloadFilePDFInfo(rechargePaymentRecordDto.getUserId(), ReportType.FQZ);
            if(response != null && "0".equals(response.getStatus())){
                filePath = (String) response.getData();
                logger.info("qiniu down load PDF Info filePath is " + filePath);
                if(StringUtils.isNotBlank(filePath)){
                    logger.info("获取风险速查报告信息 1***："+JSON.toJSONString(response));
                }
            } else {
                response = qiniuContract.downloadFilePDFInfo(rechargePaymentRecordDto.getUserId());
                logger.info("获取风险速查报告信息 2***："+JSON.toJSONString(response));
           }
           if (StringUtils.isBlank(filePath)) {
               response = qiniuContract.downloadFilePDFInfo(rechargePaymentRecordDto.getUserId());
               logger.info("获取风险速查报告信息 3***："+JSON.toJSONString(response));
           }
        } catch (Exception e) {
            logger.error("获取风险速查报告信息~~~",e);
            return response.error("服务器开小差了~~~");
        }
        return response;
    }

    /**
     * 订单详情
     * @param rechargePaymentRecordDto
     * @return
     */
    @Override
    public ResponseData findorderdetailsByUserId(RechargePaymentRecordDto rechargePaymentRecordDto) {
        ResponseData response = ResponseData.success();
        if(!(rechargePaymentRecordDto != null && rechargePaymentRecordDto.getUserId() != null)){
            return response.error("rechargePaymentRecordDto is null or userId is null~~~~");
        }
        try {
            List<OrderDetailsInfo> list = new ArrayList<>();
            List<OrderDetailsInfo> resultList = customerHandleQueryMapper.findOrderDetails(rechargePaymentRecordDto);
            logger.info("find order details result is " + resultList);
            if(resultList != null && resultList.size() > 0){
                for (OrderDetailsInfo orderDetails:resultList) {
                    if(orderDetails.getPayTime() == null){
                        orderDetails.setPayTime("");
                    }
                    list.add(orderDetails);
                }
            }
            response.setData(list);
        } catch (Exception e){
            logger.error("查询订单记录出错~~~~", e);
            return response.error("服务器开小差了！");
        }
        return response;
    }

    /**
     * 代扣协议查询
     * @param userId
     * @return
     */
    @Override
    public ResponseData queryWithHoldAgreement(String userId) {
        ResponseData response = ResponseData.success();
        String debitFlag = "2";   //2表示代扣
        List<WithHoldAgreementVo> vos = new ArrayList();
        try {
            ResponseData<List<MemberLogModel>> responseData = memberLogContract.getMemberLog(userId, debitFlag);
            if ("0".equals(responseData.getStatus())){
                List<MemberLogModel> list = responseData.getData();
                for (MemberLogModel memberLogModel : list){
                    if (StringUtils.isBlank(memberLogModel.getAppName())){
                        memberLogModel.setAppName("nyd");
                    }
                    if (StringUtils.isNotBlank(memberLogModel.getAppName())){
                        String appName = memberLogModel.getAppName();
                        String memberId = memberLogModel.getMemberId();
                        String str = "daik"+memberId+getDesByName(appName); //查询元素
                        logger.info("userId:"+userId+",查询app的MongoDB库,查询元素:"+str);
                        Criteria criteria = new Criteria();
                        Query query = Query.query(criteria.andOperator(Criteria.where("_id").is(str)));
                        List<WithHoldAgreementInfo> WithHoldAgreementInfoS = mongoAppTemplate.find(query,WithHoldAgreementInfo.class,"fadada_Result");
                        logger.info("根据userId:"+userId+",查找到的代扣协议结果:"+JSON.toJSON(WithHoldAgreementInfoS));
                        if (WithHoldAgreementInfoS != null && WithHoldAgreementInfoS.size() >0 ){
                            WithHoldAgreementInfo WithHoldAgreementInfo = WithHoldAgreementInfoS.get(0);
                            Date create_time = WithHoldAgreementInfo.getCreate_time();
                            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(create_time);
                            WithHoldAgreementVo withHoldAgreementVo = new WithHoldAgreementVo();
                            BeanUtils.copyProperties(WithHoldAgreementInfo,withHoldAgreementVo);
                            withHoldAgreementVo.setTime(dateStr);
                            logger.info("代扣协议对象:"+JSON.toJSON(withHoldAgreementVo));
                            vos.add(withHoldAgreementVo);
                        }
                    }

                }
            }
            response.setData(vos);
            logger.info("返回给前台的对象:"+JSON.toJSON(responseData));

        }catch (Exception e){
            logger.info("代扣协议查询出错",e);
            return response.error("服务器开小差了！");
        }
        return response;
    }



    public static String getDesByName(String appName) {
        if (StringUtils.isBlank(appName)) {
            return "";
        }
        for (AppType appType :AppType.values()) {
            if (appType.getType().equals(appName)) {
                return appType.getDescription();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        String appName= "xqh";
        String name = getDesByName(appName);
        System.out.println(name);
    }




    /**
     * 身份证脱敏处理
     * @param id
     * @return
     */
    private static String idHide(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    /**
     * 银行卡号隐藏处理
     *
     */
    public static String hideCardNo(String cardNo) {
        if(StringUtils.isBlank(cardNo)) {
            return cardNo;
        }
        int length = cardNo.length();
        int beforeLength = 4;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }
        return sb.toString();
    }


    /**
     * 预留手机号隐藏处理
     * @param reserveMobile
     * @return
     */
    public static String hidePhoneNo(String reserveMobile) {
        if(StringUtils.isBlank(reserveMobile)) {
            return reserveMobile;
        }
        int length = reserveMobile.length();
        int beforeLength = 3;
        int afterLength = 3;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(reserveMobile.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }
        return sb.toString();
    }
}
