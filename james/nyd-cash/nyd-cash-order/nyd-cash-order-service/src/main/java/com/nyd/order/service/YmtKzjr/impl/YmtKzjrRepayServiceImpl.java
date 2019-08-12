package com.nyd.order.service.YmtKzjr.impl;

import com.alibaba.fastjson.JSON;
import com.ibank.order.api.KzjrRepayContract;
import com.ibank.order.model.RepayDetail;
import com.nyd.order.dao.YmtKzjrBill.BillYmtDao;
import com.nyd.order.dao.YmtKzjrBill.OverdueBillYmtDao;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.KzjrRepayDetail;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.nyd.order.service.YmtKzjr.YmtKzjrRepayService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class YmtKzjrRepayServiceImpl implements YmtKzjrRepayService{
    private static Logger logger = LoggerFactory.getLogger(YmtKzjrRepayServiceImpl.class);

    @Autowired
    private KzjrRepayContract kzjrRepayContract;

    @Autowired
    private BillYmtDao billYmtDao;

    @Autowired
    private OverdueBillYmtDao overdueBillYmtDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据子订单号找到一系列还款信息参数
     * @param orderSno
     * @return
     */
    @Override
    public ResponseData<KzjrRepayDetail> findKzjrRepayDetail(String orderSno) {
        logger.info("kzjr 还款信息请求参数："+orderSno);
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(orderSno)){
            logger.error("订单号为空");
            responseData = responseData.error("子订单号为空");
            return responseData;
        }

        KzjrRepayDetail kzjrRepayDetail = new KzjrRepayDetail();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {

            /**
             *根据子订单号找到t_order_detail中的还款信息
             */
            ResponseData<RepayDetail> data = kzjrRepayContract.queryByOrderSno(orderSno);
            if ("0".equals(data.getStatus())){
                RepayDetail repayDetail = data.getData();
                logger.info("根据子订单号找到的订单信息:"+JSON.toJSON(repayDetail));

                //订单号
                String orderNo = "";
                if (StringUtils.isNotBlank(repayDetail.getOrderNo())){
                    orderNo = repayDetail.getOrderNo();
                    kzjrRepayDetail.setOrderNo(orderNo);
                }

                //子订单号
                kzjrRepayDetail.setOrderSno(orderSno);

                //借款码
                String borrowCode = "";
                if (StringUtils.isNotBlank(repayDetail.getBorrowCode())){
                    borrowCode = repayDetail.getBorrowCode();
                    kzjrRepayDetail.setBorrowCode(borrowCode);
                }

                //借款金额
                kzjrRepayDetail.setBorrowAmount(repayDetail.getBorrowAmount());

                //借款人的user_id
                String userId = "";
                if (StringUtils.isNotBlank(repayDetail.getUserId())){
                    userId = repayDetail.getUserId();
                    kzjrRepayDetail.setUserId(userId);
                }

                //手机号码
                String accountNumber = "";
                if (StringUtils.isNotBlank(repayDetail.getAccountNumber())){
                    accountNumber = repayDetail.getAccountNumber();
                    kzjrRepayDetail.setAccountNumber(accountNumber);
                }

                //借款期限
                kzjrRepayDetail.setBorrowTime(repayDetail.getBorrowTime());

                //借款时长单位（天，期）
                String loanUnit = "";
                if (StringUtils.isNotBlank(repayDetail.getLoanUnit())){
                    loanUnit = repayDetail.getLoanUnit();
                    kzjrRepayDetail.setLoanUnit(loanUnit);
                }

                //放款时间
                String payTime = "";
                if (StringUtils.isNotBlank(repayDetail.getPayTime())){
                    payTime = repayDetail.getPayTime();
                    kzjrRepayDetail.setPayTime(payTime);
                }

                //app产品编码
                String appCode = "";
                if (StringUtils.isNotBlank(repayDetail.getAppCode())){
                    appCode = repayDetail.getAppCode();
                    kzjrRepayDetail.setAppCode(appCode);
                }

                //app产品名称
                String appName = "";
                if (StringUtils.isNotBlank(repayDetail.getAppName())){
                    appName = repayDetail.getAppName();
                    kzjrRepayDetail.setAppName(appName);
                }

                //姓名
                String userName = "";
                if (StringUtils.isNotBlank(repayDetail.getUserName())){
                    userName = repayDetail.getUserName();
                    kzjrRepayDetail.setUserName(userName);
                }

                //身份证号码
                String idNumber = "";
                if (StringUtils.isNotBlank(repayDetail.getIdNumber())){
                    idNumber = repayDetail.getIdNumber();
                    kzjrRepayDetail.setIdNumber(idNumber);
                }

                logger.info("根据子订单号找到的,订单号:"+ orderNo+",子订单号:"+orderSno+",借款码:"+borrowCode+",借款金额:"+repayDetail.getBorrowAmount()+
                ",借款人的user_id:"+userId+",手机号码:"+accountNumber+",借款期限:"+repayDetail.getBorrowTime()+",借款时长单位："+loanUnit+",放款时间:"+payTime+
                ",app产品编码:"+appCode+",app产品名称："+appName+",姓名:"+userName+",身份证号码:"+idNumber);
            }

            /**
             * 根据子订单号查找账单信息
             */
            List<BillYmtInfo> billYmtInfoList = billYmtDao.selectByOrderSno(orderSno);
            if (billYmtInfoList != null && billYmtInfoList.size() > 0){
                BillYmtInfo billYmtInfo = billYmtInfoList.get(0);
                logger.info("根据子订单号找到的账单信息:"+JSON.toJSON(billYmtInfo));

                //约定还款时间
                Date promiseRepaymentDate = billYmtInfo.getPromiseRepaymentDate();
                String str = dateFormat.format(promiseRepaymentDate);
                kzjrRepayDetail.setRepayTime(str);

                //资产编号
                String assetCode = billYmtInfo.getAssetCode();
                kzjrRepayDetail.setAssetCode(assetCode);

                //本次应还金额
                BigDecimal waitRepayAmount = billYmtInfo.getWaitRepayAmount();
                kzjrRepayDetail.setRepayAmount(waitRepayAmount);

                //应还利息
                kzjrRepayDetail.setRepayInterest(billYmtInfo.getRepayInterest());

                //账单编号
                kzjrRepayDetail.setBillNo(billYmtInfo.getBillNo());

                logger.info("根据子订单号找到的,约定还款时间:"+str+",资产编号:"+assetCode+",本次应还金额:"+waitRepayAmount+",应还利息:"+
                        billYmtInfo.getRepayInterest()+",账单编号"+billYmtInfo.getBillNo());
            }

            /**
             * 根据子订单号查找逾期相关信息
             */
            List<OverdueBillYmtInfo> overdueBillYmtInfoList = overdueBillYmtDao.selectByOrderSno(orderSno);
            if (overdueBillYmtInfoList != null && overdueBillYmtInfoList.size() > 0 ){
                OverdueBillYmtInfo overdueBillYmtInfo = overdueBillYmtInfoList.get(0);
                logger.info("根据子订单号找到的逾期信息:"+JSON.toJSON(overdueBillYmtInfo));

                //逾期天数
                kzjrRepayDetail.setOverdueDays(overdueBillYmtInfo.getOverdueDays());

                //滞纳金
                kzjrRepayDetail.setOverdueFine(overdueBillYmtInfo.getOverdueFine());

                //逾期金额（罚息）
                kzjrRepayDetail.setOverdueAmount(overdueBillYmtInfo.getOverdueAmount());
                logger.info("根据子订单号找到的,逾期天数:"+overdueBillYmtInfo.getOverdueDays()+",逾期金额（罚息）:"+overdueBillYmtInfo.getOverdueAmount());
            }

            String token = "";
            if (redisTemplate.hasKey("ibank_repay_switch")){
                token = (String)redisTemplate.opsForValue().get("ibank_repay_switch");
                logger.info("跑批开关："+token);
            }else{
                token = "0";
                logger.info("跑批开关："+token);
            }

            if ("1".equals(token)){    //关闭
                responseData.setStatus("1");
                responseData.setMsg("系统正在维护，稍后再尝试");
            }

            responseData.setData(kzjrRepayDetail);
            logger.info("根据子订单号:"+orderSno+",找到的还款信息:"+JSON.toJSON(kzjrRepayDetail));

        }catch (Exception e){
            logger.error("查找还款信息出错:",e);
            e.printStackTrace();
        }

        return responseData;
    }
}
