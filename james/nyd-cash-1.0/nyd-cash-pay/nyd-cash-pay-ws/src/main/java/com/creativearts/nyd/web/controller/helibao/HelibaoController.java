package com.creativearts.nyd.web.controller.helibao;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.enums.SourceType;
import com.creativearts.nyd.pay.model.helibao.*;
import com.creativearts.nyd.pay.service.PayRouteService;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.helibao.HelibaoPayService;
import com.creativearts.nyd.pay.service.validator.ValidateUtil;
import com.creativearts.nyd.web.controller.BaseController;
import com.nyd.pay.api.enums.WithHoldType;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;

/**
 * Cong Yuxiang
 * 2017/12/15  love~money
 **/
@RestController
@RequestMapping("/pay/hlb")
public class HelibaoController extends BaseController {

    //    Logger log = LoggerFactory.getLogger(HelibaoController.class);
    @Autowired
    private PayRouteService payRouteService;

//    @Autowired
//    private MemberContract memberContract;


    @Autowired
    private ValidateUtil validateUtil;

    @Autowired
    private HelibaoPayService helibaoPayService;


    @Autowired
    private RedisProcessService redisProcessService;

    @RequestMapping("/quikPay")
    public ResponseData quikPay(@RequestBody NydHlbVo vo) {

        logger.info("quick" + JSON.toJSONString(vo));

//        if (StringUtils.isBlank(vo.getBillNo())) {
//            vo.setBillNo(vo.getUserId());
//        }
        if (!SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            PayStatus payStatus = redisProcessService.checkIfPay(vo.getBillNo());

            if (payStatus == PayStatus.PAY_SUCESS) {

                ResponseData responseData = ResponseData.error();
                responseData.setMsg("已经支付成功");
                return responseData;

            }
        }
        if (vo.getSourceType() == null) {
            vo.setSourceType(SourceType.REPAY_NYD.getType());
        }

        if (SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            if (StringUtils.isNotBlank(vo.getBillNo())) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("异常");
                return responseData;
            }
            return payRouteService.HlbMemberFeePay(vo);
        } else if (SourceType.REPAY_NYD.getType().equals(vo.getSourceType())) {
            return payRouteService.HlbReturnPay(vo);
        } else {
            ResponseData responseData = ResponseData.error();
            responseData.setMsg("无效的支付");
            return responseData;
        }


    }

    @RequestMapping("/directPay")
    public ResponseData directPay(@RequestBody NydHlbVo vo) {

        logger.info("direct" + JSON.toJSONString(vo));
        validateUtil.validate(vo);

//        if (SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
//            vo.setBillNo(vo.getUserId());
//        }
        if (!SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            PayStatus payStatus = redisProcessService.checkIfPay(vo.getBillNo());

            if (payStatus == PayStatus.PAY_SUCESS) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("已经支付成功");
                return responseData;
            }
        }
        if (vo.getSourceType() == null) {
            vo.setSourceType(SourceType.REPAY_NYD.getType());
        }

        if (SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            if (StringUtils.isNotBlank(vo.getBillNo())) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("异常");
                return responseData;
            }
            return payRouteService.HlbMemberFeePay(vo);
        } else if (SourceType.REPAY_NYD.getType().equals(vo.getSourceType())) {
            return payRouteService.HlbReturnPay(vo);
        } else {
            ResponseData responseData = ResponseData.error();
            responseData.setMsg("无效的支付");
            return responseData;
        }


    }

    @RequestMapping(value = "/withholdSingle")
    public ResponseData createOrder(CreateOrderVo orderVo) {
        return helibaoPayService.withHold(orderVo, null);

    }

    @RequestMapping(value = "/withholdQuery")
    public ResponseData withholdQuery(QueryOrderVo queryOrderVo) {
        return helibaoPayService.withHoldQuery(queryOrderVo);
    }

    @ResponseBody
    @RequestMapping(value = "/callback")
    public String notify(@RequestBody String callback) {
//        logger.info("**"+callback);

        return helibaoPayService.callBack(callback);
    }

    @RequestMapping(value = "/withholdBatch")
    public ResponseData withholdBatch(WithholdBatchVo withholdBatchVo) {
        return helibaoPayService.withHoldBatch(withholdBatchVo);
    }

    @RequestMapping(value = "/withholdBatchQuery")
    public ResponseData withholdBatchQuery(QueryBatchOrderVo queryBatchOrderVo) {
        return helibaoPayService.withHoldBatchQuery(queryBatchOrderVo);
    }


    /**
     * 合利宝银行卡快捷支付
     */
    @RequestMapping("/hlbQuickPay")
    public ResponseData hlbQuickPay(@RequestBody NydHlbVo vo) {
        //默认赋值给appName=nyd
        if (StringUtils.isBlank(vo.getAppName())){
            vo.setAppName("nyd");
        }
        logger.info("hlbQuickPay" + JSON.toJSONString(vo));
        validateUtil.validate(vo);

//        if (SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
//            vo.setBillNo(vo.getUserId());
//        }
        if (!SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            PayStatus payStatus = redisProcessService.checkIfPay(vo.getBillNo());

            if (payStatus == PayStatus.PAY_SUCESS) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("表示已经支付成功");
                return responseData;
            }
        }
        if (vo.getSourceType() == null) {
            vo.setSourceType(SourceType.REPAY_NYD.getType());
        }

        //支付会员费或者 还款
        if (SourceType.MEMBER_FEE.getType().equals(vo.getSourceType())) {
            if (StringUtils.isNotBlank(vo.getBillNo())) {
                ResponseData responseData = ResponseData.error();
                responseData.setMsg("异常");
                return responseData;
            }
            ResponseData data = payRouteService.PayHlbMemberFee(vo);                //支付评估费（之前的会员费）
            logger.info("支付会员费success："+JSON.toJSON(data));
            return data;

        } else if (SourceType.REPAY_NYD.getType().equals(vo.getSourceType())) {     //还款
            ResponseData data = payRouteService.PayHlbReturn(vo);
            logger.info("还款success："+JSON.toJSON(data));
            return data;
        } else if (SourceType.CASH_COUPON.getType().equals(vo.getSourceType())) {     //充值现金券
            logger.info("进入合利宝充值现金券+++++++++++++++++");
            ResponseData data = payRouteService.PayHlbCashCoupon(vo);
            logger.info("利用合利宝充值现金券success："+JSON.toJSON(data));
            return data;
        }else if (SourceType.KZJR_REPAY_YMT.getType().equals(vo.getSourceType())) {     //空中金融还款
            logger.info("进入空中金融还款:"+JSON.toJSON(vo));
            ResponseData data = payRouteService.PayHlbKzjrRepay(vo);
            logger.info("空中金融还款success："+JSON.toJSON(data));
            return data;
        } else {
            ResponseData responseData = ResponseData.error();
            responseData.setMsg("无效的支付");
            return responseData;
        }


    }


    /**
     * 首次支付下单
     * @return
     */
    @RequestMapping(value = "/firstPay")
    public ResponseData firstPay(@RequestBody FirstPayCreateOrderVo vo){
        logger.info("firstPay"+JSON.toJSONString(vo));
        return helibaoPayService.firstPay(vo);
    }

    /**
     * 首次支付短信
     */
    @RequestMapping(value = "/firstPayMessage")
    public ResponseData firstPayMessage(@RequestBody SendValidateCodeVo vo){
        return helibaoPayService.firstPayMessage(vo);
    }


    /**
     * 确认支付
     */
    @RequestMapping(value = "/confirmPay")
//    public ResponseData confirmPay(@RequestBody ConfirmPayVo vo){
//        return helibaoPayService.confirmPay(vo, null);
//    }
    public ResponseData confirmPay(@RequestBody SurePayVo surePayVo){
        ConfirmPayVo vo = new ConfirmPayVo();
        vo.setP3_orderId(surePayVo.getOrderNo());
        vo.setP5_validateCode(surePayVo.getMessageCode());
        return helibaoPayService.confirmPay(vo);
    }

    /**
     * 异步通知接口
     */
    @ResponseBody
    @RequestMapping(value = "/hlbCallback")
    public String hlbNotify(@RequestBody String callback) {
        logger.info("接收通知参数："+callback);
        return helibaoPayService.hlbCallback(callback);
    }


    /**
     * 合利宝绑卡支付
     */

    /**
     * 鉴权绑卡短信
     */
    @RequestMapping(value = "/bindCardMessage")
    public ResponseData bindCardMessage(@RequestBody BindCardMessageVo vo){
        return helibaoPayService.bindCardMessage(vo);
    }

    /**
     * 鉴权绑卡
     */
    @RequestMapping(value = "/bindCard")
    public ResponseData bindCard(@RequestBody BindCardVo vo){
        return helibaoPayService.bindCard(vo);
    }

    /**
     * 绑卡支付短信
     */
    @RequestMapping(value = "/bindCardPayMessage")
    public ResponseData bindCardPayMessage(@RequestBody BindCardPayMessageVo vo){
        return helibaoPayService.bindCardPayMessage(vo);
    }

    /**
     * 绑卡支付
     */
    @RequestMapping(value = "/bindCardPay")
    public ResponseData bindCardPay(@RequestBody BindCardPayVo vo){
        return helibaoPayService.bindCardPay(vo);
    }

    /**
     * 订单查询
     */
    @RequestMapping(value = "/queryOrder")
    public ResponseData queryOrder(@RequestBody OrderQueryVo vo){
        return helibaoPayService.queryOrder(vo);
    }

    /**
     * 银行卡解绑
     */
    @RequestMapping(value = "/unBindCard")
    public ResponseData unBindCard(@RequestBody UnBindCardVo vo){
        return helibaoPayService.unBindCard(vo);
    }

    /**
     * 用户绑定银行卡信息查询（仅限于交易卡）,暂不处理，目前还用不到此接口。
     */
    @RequestMapping(value = "/bindCardInformationQuery")
    public ResponseData bindCardInformationQuery(@RequestBody BindCardInformationQueryVo vo){
        return helibaoPayService.bindCardInformationQuery(vo);
    }

}
