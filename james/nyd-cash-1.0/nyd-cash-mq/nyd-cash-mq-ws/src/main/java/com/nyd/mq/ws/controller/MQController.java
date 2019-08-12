package com.nyd.mq.ws.controller;

import com.alibaba.fastjson.JSON;
import com.creativearts.das.query.api.message.AuditResultMessage;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.ibank.pay.model.mq.EvaFeeInfo;
import com.ibank.pay.model.mq.RecFeeInfo;
import com.ibank.pay.model.mq.RechargeFeeInfo;
import com.nyd.capital.model.RemitMessage;
import com.nyd.dsp.model.msg.DspToRinseMsg;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.mq.service.rabbit.*;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhujx on 2017/12/18.
 */
@RestController
@RequestMapping(value = "/mq")
public class MQController {

    private static Logger logger = LoggerFactory.getLogger(MQController.class);

    @Autowired
    RemitToZeusProducer remitToZeusProducer;

    @Autowired
    PayToOrderProducer payToOrderProducer;

    @Autowired
    RepayToZeusProducer repayToZeusProducer;

    @Autowired
    AuditToOrderProducer auditToOrderProducera;

    @Autowired
    AuditToOrderYmtProducer auditToOrderYmtProducer;

    @Autowired
    PayToMemberProducer payToMemberProducer;

    @Autowired
    RemitLogToZeusProducer remitLogToZeusProducer;

    @Autowired
    RepayLogToZeusProducer repayLogToZeusProducer;

    @Autowired
    DspToRinseProducer dspToRinseProducer;

    @Autowired
    RechargeFeeToUserYmtProducer rechargeFeeToUserYmtProducer;

    @Autowired
    EvaFeeToUserYmtProducer evaFeeToUserYmtProducer;

    @Autowired
    RecFeeToOrderYmtProducer recFeeToOrderYmtProducer;

    /**
     * 放款失败消息发送
     * @param remitMessage
     * @return
     */
    @RequestMapping(value = "/payToOrder", method = RequestMethod.POST, produces = "application/json")
    public ResponseData payToOrder(@RequestBody RemitMessage remitMessage){
        logger.info("payToOrder" + remitMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(remitMessage !=null ){
            payToOrderProducer.sendMsg(remitMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 放款成功消息发送
     * @param remitMessage
     * @return
     */
    @RequestMapping(value = "/remitToZeus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData remitToZeus(@RequestBody RemitMessage remitMessage){
        logger.info("remitToZeus" + remitMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(remitMessage !=null ){
            remitToZeusProducer.sendMsg(remitMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 还款成功后发消息
     * @param repayMessage
     * @return
     */
    @RequestMapping(value = "/repayToZeus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData repayToZeus(@RequestBody RepayMessage repayMessage){
        logger.info("payToOrder" + repayMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(repayMessage !=null ){
            repayToZeusProducer.sendMsg(repayMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 审核完成后发消息
     * @param auditMessage
     * @return
     */
    @RequestMapping(value = "/auditToOrder", method = RequestMethod.POST, produces = "application/json")
    public ResponseData auditToOrder(@RequestBody AuditResultMessage auditMessage){
        logger.info("auditToOrder" + auditMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(auditMessage !=null ){
            auditToOrderProducera.sendMsg(auditMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 审核完成后发消息
     * @param auditMessage
     * @return
     */
    @RequestMapping(value = "/auditToOrderYmt", method = RequestMethod.POST, produces = "application/json")
    public ResponseData auditToOrderYmt(@RequestBody AuditResultMessage auditMessage){
        logger.info("auditToOrderYmt" + auditMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(auditMessage !=null ){
            auditToOrderYmtProducer.sendMsg(auditMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 代扣会员费后发消息
     * @param memberFeeLogMessage
     * @return
     */
    @RequestMapping(value = "/payToMember", method = RequestMethod.POST, produces = "application/json")
    public ResponseData payToMember(@RequestBody MemberFeeLogMessage memberFeeLogMessage){
        logger.info("payToMember" + memberFeeLogMessage.toString());
        ResponseData responseData = ResponseData.success();
        if(memberFeeLogMessage !=null ){
            payToMemberProducer.sendMsg(memberFeeLogMessage);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 放款log消息
     * @param remitInfo
     * @return
     */
    @RequestMapping(value = "/remitLogToZeus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData remitLogToZeus(@RequestBody RemitInfo remitInfo){
        logger.info("remitLogToZeus" + remitInfo.toString());
        ResponseData responseData = ResponseData.success();
        if(remitInfo !=null ){
            remitLogToZeusProducer.sendMsg(remitInfo);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 还款log消息
     * @param message
     * @return
     */
    @RequestMapping(value = "/repayLogToZeus", method = RequestMethod.POST, produces = "application/json")
    public ResponseData repayLogToZeus(@RequestBody RepayInfo message){
        logger.info("repayLogToZeus" + message.toString());
        ResponseData responseData = ResponseData.success();
        if(message !=null ){
            repayLogToZeusProducer.sendMsg(message);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 数据源发往清洗服务消息
     * @param dspToRinseMsg
     * @return
     */
    @RequestMapping(value = "/dspToRinse", method = RequestMethod.POST, produces = "application/json")
    public ResponseData dspToRinse(@RequestBody DspToRinseMsg dspToRinseMsg){
        logger.info("dspToRinse" + dspToRinseMsg.toString());
        ResponseData responseData = ResponseData.success();
        if(dspToRinseMsg !=null ){
            dspToRinseProducer.sendMsg(dspToRinseMsg);
        }else{
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     *充值完成之后发送mq
     * @param msg
     * @return
     */
    @RequestMapping(value = "/rechargeFeeToUserYmt", method = RequestMethod.POST, produces = "application/json")
    public ResponseData ibankPaytoUserRecharge(@RequestBody RechargeFeeInfo msg) {
        logger.info("rechargeFee to user,message is "+ JSON.toJSONString(msg));
        ResponseData responseData = ResponseData.success();
        if (msg!=null){
            rechargeFeeToUserYmtProducer.sendMsg(msg);
        } else {
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    /**
     * 评估费支付成功mq
     * @param msg
     * @return
     */
    @RequestMapping(value = "/evaFeeToUserYmt", method = RequestMethod.POST, produces = "application/json")
    public ResponseData evaFeeToUserMq(@RequestBody EvaFeeInfo msg) {
        logger.info("EvaFeeInfo to user,message is "+ JSON.toJSONString(msg));
        ResponseData responseData = ResponseData.success();
        if (msg!=null){
            evaFeeToUserYmtProducer.sendMsg(msg);
        } else {
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }

    @RequestMapping(value = "/recFeeToOrderYmt", method = RequestMethod.POST, produces = "application/json")
    public ResponseData recFeeToOrderYmt(@RequestBody RecFeeInfo msg) {
        logger.info("RecFeeInfo to order,message is "+ JSON.toJSONString(msg));
        ResponseData responseData = ResponseData.success();
        if (msg!=null){
            recFeeToOrderYmtProducer.sendMsg(msg);
        } else {
            responseData = ResponseData.error("有参数为空");
        }
        return responseData;
    }


}
