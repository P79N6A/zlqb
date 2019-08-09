package com.creativearts.nyd.web.controller.yinshengbao;


import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.enums.SourceType;
import com.creativearts.nyd.pay.model.yinshengbao.NydYsbVo;
import com.creativearts.nyd.pay.model.yinshengbao.SubContractIdDelay;
import com.creativearts.nyd.pay.model.yinshengbao.YsbNotifyResponseVo;
import com.creativearts.nyd.pay.service.PayRouteService;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.utils.CallBackUtils;
import com.creativearts.nyd.pay.service.yinshengbao.YsbPayService;
import com.creativearts.nyd.pay.service.yinshengbao.YsbUserService;
import com.creativearts.nyd.pay.service.yinshengbao.properties.YsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.util.Md5Encrypt;
import com.nyd.user.api.UserIdentityContract;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 银生宝对接
 *chaiming
 */


@RestController
@RequestMapping(value="/pay/ysb")
public class YinShengBaoController {
    static Logger logger = LoggerFactory.getLogger(YinShengBaoController.class);

    @Autowired
    private YsbPayService ysbPayService;

    @Autowired
    private YsbUserService ysbUserService;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private YsbProperties ysbProperties;
    @Autowired
    private RedisProcessService redisProcessService;

    @Autowired
    private PayRouteService payRouteService;

    /**
     *
     * 子协议录入
     * 委托代扣
     *
     */
    @RequestMapping(value="/quickPay")
    public ResponseData quickPay(@RequestBody NydYsbVo nydYsbVo) throws Exception {
        logger.info("quickPay银生宝入口参数"+JSON.toJSONString(nydYsbVo));

        if (!SourceType.MEMBER_FEE.getType().equals(nydYsbVo.getSourceType())) {
            PayStatus payStatus = redisProcessService.checkIfPay(nydYsbVo.getBillNo());

            if (payStatus == PayStatus.PAY_SUCESS) {
                    ResponseData responseData = ResponseData.error();
                    responseData.setMsg("已经支付成功");
                    return responseData;
            }
        }
        if (nydYsbVo.getSourceType() == null) {
            nydYsbVo.setSourceType(SourceType.REPAY_NYD.getType());
        }

        if (SourceType.MEMBER_FEE.getType().equals(nydYsbVo.getSourceType())) {        //支付评估费（之前的会员费）
            if(StringUtils.isNotBlank(nydYsbVo.getBillNo())){
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("异常");
                return responseData;
            }
            return payRouteService.YsbMemberFeePay(nydYsbVo);
        } else if (SourceType.REPAY_NYD.getType().equals(nydYsbVo.getSourceType())) {   //还款
            return payRouteService.YsbReturnPay(nydYsbVo);
        }else if (SourceType.CASH_COUPON.getType().equals(nydYsbVo.getSourceType())){   //充值现金券
            logger.info("进入银生宝充值现金券+++++++++++++++++");
            ResponseData data = payRouteService.YsbCashCouponPay(nydYsbVo);
            logger.info("利用银生宝充值现金券success："+JSON.toJSON(data));
            return data;
        } else {
            ResponseData responseData = ResponseData.error();
            responseData.setMsg("无效的支付");
            return responseData;
        }
//        return  ysbPayService.signAndDaikou(nydYsbVo, Constant.KPAY_TYPE);
    }

//    @RequestMapping(value="/signAndWithhold")
//    public ResponseData signAndDaikou(@RequestBody NydYsbVo nydYsbVo) throws Exception {
//
//
//
//
//    }

    /**
     * 扣款通知
     */
    @ResponseBody
    @RequestMapping(value = "/callback")
    public String notify(@RequestBody String callback) {
        logger.info("银生宝回调通知:++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("银生宝回调通知参数："+callback);
        try {
            YsbNotifyResponseVo notifyResponseVo = CallBackUtils.parse(callback, YsbNotifyResponseVo.class);
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(ysbProperties.getAccountId());
            sf.append("&orderId=").append(notifyResponseVo.getOrderId());
            sf.append("&amount=").append(notifyResponseVo.getAmount());
            sf.append("&result_code=").append(notifyResponseVo.getResult_code());
            sf.append("&result_msg=").append(notifyResponseVo.getResult_msg());
            sf.append("&key=").append(ysbProperties.getKey());

            logger.info("验签串>>>："+sf.toString());
            String responseMac=notifyResponseVo.getMac();
            logger.info("接收到的MAC>>>"+responseMac);
            String mac=Md5Encrypt.md5(sf.toString()).toUpperCase();
            logger.info("加密MAC>>>"+mac);
            if (responseMac.equals(mac)) {
                logger.info("验签成功");
                return ysbPayService.callBackProcess(notifyResponseVo);

//                if (notifyResponseVo.getResult_code().equals("00")){            //扣款成功
//                    return ysbPayService.callBackProcess(notifyResponseVo);
//                }else if (notifyResponseVo.getResult_code().equals("10")){      //处理中
//                    return notifyResponseVo.getResult_msg();
//                }else if (notifyResponseVo.getResult_code().equals("20")){  //余额不足,扣款失败
//                    return notifyResponseVo.getResult_msg();
//                }else {                                     //扣款失败
//                    return notifyResponseVo.getResult_msg();
//                }

//                return "success";
            }else {
                logger.error("验签失败");
                return "fail";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }



    }



    /**
     * 订单状态查询
     *
     */
    @RequestMapping(value="/orderQuery")
    public ResponseData query(@RequestBody NydYsbVo nydYsbVo){
        return  ysbPayService.queryOrderStatus(nydYsbVo);
    }

    /**
     * 子协议号查询
     */
    @RequestMapping(value="/subContractIdQuery")
    public ResponseData subContractIdQuery(@RequestBody NydYsbVo nydYsbVo){
        return ysbPayService.querySubContractId(nydYsbVo);
    }

    /**
     * 子协议延期
     */
    @RequestMapping(value="/subContractIdDelay")
    public ResponseData subContractIdDelay(@RequestBody NydYsbVo nydYsbVo) throws Exception{
        //得到子协议编号
        String subContractId = ysbPayService.signSimpleSubContract(nydYsbVo);
        SubContractIdDelay subContractIdDelay = new SubContractIdDelay();
        subContractIdDelay.setSubContractId(subContractId);
        subContractIdDelay.setStartDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        subContractIdDelay.setEndDate("22000101");
        //得到用户参数
        String accountId = ysbProperties.getAccountId();
        String contractId = ysbProperties.getContractId();
        String key = ysbProperties.getKey();
        subContractIdDelay.setAccountId(accountId);
        subContractIdDelay.setContractId(contractId);
        String mac="";
        StringBuffer sf = new StringBuffer();
        sf.append("accountId=").append(accountId);
        sf.append("&contractId=").append(contractId);
        sf.append("&subContractId=").append(subContractId);
        sf.append("&startDate=").append(subContractIdDelay.getStartDate());
        sf.append("&endDate=").append(subContractIdDelay.getEndDate());
        sf.append("&key=").append(key);
        logger.info("加密前+++++++++"+sf.toString());
        mac= Md5Encrypt.md5(sf.toString()).toUpperCase();
        subContractIdDelay.setMac(mac);
        logger.info("子协议延期请求参数："+JSON.toJSONString(subContractIdDelay));

        return ysbPayService.subContractIdExtension(subContractIdDelay);
    }






}
