package com.creativearts.nyd.web.controller.yinshengbao;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.enums.SourceType;
import com.creativearts.nyd.pay.model.yinshengbao.*;
import com.creativearts.nyd.pay.service.PayRouteService;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.utils.CallBackUtils;
import com.creativearts.nyd.pay.service.yinshengbao.properties.QuickPayYsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.properties.YsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.util.Md5Encrypt;
import com.creativearts.nyd.pay.service.yinshengbao.util.YsbQuickPayService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;

/**
 * 银生宝快捷支付对接
 *
 * @author cm
 */


@RestController
@RequestMapping(value="/pay/ysb")
public class YinShengBaoQuickPayController {

    static Logger logger = LoggerFactory.getLogger(YinShengBaoQuickPayController.class);

    @Autowired
    private QuickPayYsbProperties quickPayYsbProperties;
    @Autowired
    private RedisProcessService redisProcessService;

    @Autowired
    private YsbQuickPayService ysbQuickPayService;

    @Autowired
    private PayRouteService payRouteService;



    /**
     * 银生宝银行卡快捷支付
     */
    @RequestMapping(value="/ysbQuickPay")
    public ResponseData quickPay(@RequestBody NydYsbVo nydYsbVo) throws Exception {
        logger.info("银生宝快捷支付入口参数"+ JSON.toJSONString(nydYsbVo));

        if (!SourceType.MEMBER_FEE.getType().equals(nydYsbVo.getSourceType())) {
            PayStatus payStatus = redisProcessService.checkIfPay(nydYsbVo.getBillNo());

            if (payStatus == PayStatus.PAY_SUCESS) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("已经支付成功!");
                return responseData;
            }
        }
        if (nydYsbVo.getSourceType() == null) {
            nydYsbVo.setSourceType(SourceType.REPAY_NYD.getType());
        }

        if (SourceType.MEMBER_FEE.getType().equals(nydYsbVo.getSourceType())) {        //支付评估费（之前的会员费）
            logger.info("进入银生宝快捷支付评估费（之前的会员费）+++++++++++++++++");
            if(StringUtils.isNotBlank(nydYsbVo.getBillNo())){
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("支付异常");
                return responseData;
            }
            ResponseData data = payRouteService.YsbQuickPayMemberFee(nydYsbVo);
            logger.info("ysb快捷支付,支付会员费success："+JSON.toJSON(data));
            return data;

        } else if (SourceType.REPAY_NYD.getType().equals(nydYsbVo.getSourceType())) {   //还款
            logger.info("进入银生宝快捷支付还款+++++++++++++++++");
            ResponseData data =  payRouteService.YsbQuickPayReturnPay(nydYsbVo);
            logger.info("ysb快捷支付,还款success："+JSON.toJSON(data));
            return data;

        } else if (SourceType.CASH_COUPON.getType().equals(nydYsbVo.getSourceType())){   //充值现金券
            logger.info("进入银生宝快捷支付充值现金券+++++++++++++++++");
            ResponseData data = payRouteService.YsbQuickPayCouponPay(nydYsbVo);
            logger.info("ysb快捷支付,利用银生宝充值现金券success："+JSON.toJSON(data));
            return data;

        } else {
            ResponseData responseData = ResponseData.error();
            responseData.setMsg("无效的支付");
            return responseData;
        }

    }


    /**
     *发送短信验证码接口
     */
    @RequestMapping(value = "/ysbSendMessage")
    public ResponseData ysbSendMessage(@RequestBody YsbSendMessageVo vo){
        ResponseData data =  ysbQuickPayService.ysbSendMessage(vo);
        return data;
    }


    /**
     * 确认支付接口
     */
    @RequestMapping(value = "/ysbQuickPayConfirm")
    public ResponseData ysbQuickPayConfirm(@RequestBody MessageReturn messageReturn){
        logger.info("确认支付入口参数："+ JSON.toJSON(messageReturn));
        YsbQuickPayConfirmVo vo =new YsbQuickPayConfirmVo();
        vo.setToken(messageReturn.getToken());
        vo.setCustomerId(messageReturn.getCustomerId());
        vo.setOrderNo(messageReturn.getOrderNo());
        vo.setVericode(messageReturn.getVericode());
        logger.info("确认支付参数vo："+ JSON.toJSON(vo));
        ResponseData data =  ysbQuickPayService.ysbQuickPayConfirm(vo);
        logger.info("confirm pay success:"+JSON.toJSON(data));
        return data;
    }

    /**
     *银生宝快捷支付回调通知接口
     */
    @ResponseBody
    @RequestMapping(value = "/ysbQuickPayCallback")
    public String notify(@RequestBody String callback) {
        logger.info("银生宝快捷支付回调通知:+++++++++++++++++++++++");
        logger.info("银生宝回调通知参数："+callback);
        try {
            YsbQuickPayNotifyResponseVo ysbQuickPayNotifyResponseVo = CallBackUtils.parse(callback, YsbQuickPayNotifyResponseVo.class);
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(quickPayYsbProperties.getAccountId());
            sf.append("&orderNo=").append(ysbQuickPayNotifyResponseVo.getOrderNo());
            sf.append("&userId=").append(ysbQuickPayNotifyResponseVo.getUserId());
            sf.append("&bankName=").append(ysbQuickPayNotifyResponseVo.getBankName());
            sf.append("&tailNo=").append(ysbQuickPayNotifyResponseVo.getTailNo());
            sf.append("&token=").append(ysbQuickPayNotifyResponseVo.getToken());
            sf.append("&amount=").append(ysbQuickPayNotifyResponseVo.getAmount());
            sf.append("&result_code=").append(ysbQuickPayNotifyResponseVo.getResult_code());
            if (StringUtils.isNotBlank(ysbQuickPayNotifyResponseVo.getResult_msg())){
                sf.append("&result_msg=").append(ysbQuickPayNotifyResponseVo.getResult_msg());
            }else {
                sf.append("&result_msg=").append("");
            }

            sf.append("&key=").append(quickPayYsbProperties.getKey());
            logger.info("验签串>>>："+sf.toString());
            String mac= Md5Encrypt.md5(sf.toString()).toUpperCase();
            logger.info("加密MAC:"+mac);

            String responseMac = ysbQuickPayNotifyResponseVo.getMac();
            logger.info("接收到的MAC:"+responseMac);

            if (responseMac.equals(mac)) {
                logger.info("快捷支付回调验签成功");
                return ysbQuickPayService.handleCallBack(ysbQuickPayNotifyResponseVo);
            }else {
                logger.error("快捷支付回调验签失败");
                return "fail";
            }

        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }

    }

}
