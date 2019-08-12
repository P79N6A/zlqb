package com.nyd.batch.ws;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.MaturityBill;
import com.nyd.batch.service.CuimiService;
import com.nyd.batch.service.quartz.*;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.pay.api.service.PayService;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/28
 **/
@RestController
@RequestMapping("/nyd/batch")
public class TestController {
    @Autowired
    private SendSmsTask sendSmsTask;

    @Autowired
    private FinanceReportTask financeReportTask;
    @Autowired
    private CuimiTask cuimiTask;
    @Autowired
    private SendExcelTask sendExcelTask;

    @Autowired
    private CuimiService cuimiService;

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private PayService payService;
@Autowired
    private OrderContract orderContract;
    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private BillTask billTask;

    @Autowired
    private KzjrSubbitAssetTask kzjrSubbitAssetTask;

    @Autowired
    private OrderFailTask orderFailTask;

    Logger logger = LoggerFactory.getLogger(TestController.class);


    @RequestMapping("/bill")
    public ResponseData bill(){
        System.out.println("bill**************");
        billTask.run();
        return ResponseData.success();
    }

    @RequestMapping("/kzjr")
    public ResponseData kzjrfail(){
        System.out.println("kzjr**************");
        kzjrSubbitAssetTask.run();
        return ResponseData.success();
    }


    @RequestMapping("/start")
    public ResponseData process(){
        System.out.println("test**************");
        sendSmsTask.run();
        return ResponseData.success();
    }
//    @RequestMapping(value = "/exportRepayReport",method = RequestMethod.GET)
    @RequestMapping(value = "/report",method = RequestMethod.GET)
    public ResponseData financeReport(String cdate,String remitflag,String repayflag){
        System.out.println("report**************"+cdate);
        financeReportTask.test(cdate,remitflag,repayflag);
        return ResponseData.success();
    }

    @RequestMapping(value = "/onlycuimi",method = RequestMethod.GET)
    public ResponseData onlycuimi(){
        System.out.println("onlycuimi");
        cuimiTask.run1();
        return ResponseData.success();
    }
    @RequestMapping(value = "/sendEmail",method = RequestMethod.GET)
    public ResponseData sendEmail(String cdate){
        System.out.println("sendEmail");
        sendExcelTask.runManual(cdate);
        return ResponseData.success();
    }

    //跑之前 逾期还款状况。
    @RequestMapping(value = "/overduestatus",method = RequestMethod.GET)
    public ResponseData overduestatus(String startDate,String endDate){
        System.out.println("overduestatus");
        cuimiService.generateOverdueReturnStatusTest(startDate,endDate);
        return ResponseData.success();
    }

    @RequestMapping(value = "/haha",method = RequestMethod.GET)
    public ResponseData haha(){
        System.out.println("haha");
        cuimiService.generateOverdueReturnStatus();
        return ResponseData.success();
    }

    @RequestMapping(value = "/buckle",method = RequestMethod.GET)
    public ResponseData buckle(String billNo,String amount){
        Map map = new HashMap();
        map.put("billNo", billNo);
        List<MaturityBill> maturityBills = billMapper.getMaturityBill(map);
        for(MaturityBill maturityBill:maturityBills){
            OrderInfo orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
            int loopCount = 10;
            while (orderInfo == null&&loopCount>0){
                orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
                loopCount--;
            }
            if(orderInfo==null){
                continue;
            }
            UserInfo userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
//            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(maturityBill.getOrderNo()).getData();
            loopCount = 10;
            while (userInfo == null&&loopCount>0){
                userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
                loopCount--;
            }
            if(userInfo == null){
                continue;
            }



//            for(int i=0;i<5;i++) {
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                CreateOrderVo vo = new CreateOrderVo();


                vo.setP3_orderId(maturityBill.getBillNo()+"-"+System.currentTimeMillis()+"-q");
                vo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
                vo.setP8_cardNo(orderInfo.getBankAccount());
                vo.setP7_idCardNo(userInfo.getIdNumber());
                vo.setP5_payerName(userInfo.getRealName());

//                if(i==0) {
//                    vo.setP11_orderAmount(maturityBill.getWaitRepayAmount());
//                }else {
//                    vo.setP11_orderAmount(maturityBill.getWaitRepayAmount().divide(new BigDecimal(2*i),2, RoundingMode.HALF_UP));
//                }
            if(amount==null||amount.trim().length()==0) {
                vo.setP11_orderAmount(maturityBill.getWaitRepayAmount());
            }else {
                vo.setP11_orderAmount(new BigDecimal(amount));
            }
                ResponseData responseData = payService.withHold(vo, WithHoldType.WITH_HOLD);

                if ("0".equals(responseData.getStatus())) {
                    RepayMessage repayMessage = new RepayMessage();
                    repayMessage.setBillNo(maturityBill.getBillNo());
                    repayMessage.setRepayAmount(maturityBill.getWaitRepayAmount());
                    repayMessage.setRepayStatus("0");
                   logger.info(maturityBill.getBillNo()+"第"+1+"次强扣还款回写mq"+ JSON.toJSONString(responseData));
//                    rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);
//                    if(i==0){
//                        break;
//                    }


                } else if (JSON.toJSONString(responseData).contains("余额不足")) {
                    logger.info(maturityBill.getBillNo()+"第"+1+"次余额不足"+JSON.toJSONString(responseData));
                    continue;
                } else {
                    logger.info(maturityBill.getBillNo()+"第"+1+"次错误："+JSON.toJSONString(responseData));
                    continue;
                }

//            }
        }
        return ResponseData.success();
    }


    @RequestMapping(value = "/updateremitfailorder",method = RequestMethod.GET)
    public ResponseData updateRemitFailOrder(){
        orderFailTask.run();
        return ResponseData.success();
    }


}
