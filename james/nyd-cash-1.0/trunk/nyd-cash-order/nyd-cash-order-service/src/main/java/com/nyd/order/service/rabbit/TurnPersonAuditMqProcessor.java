package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.creativearts.das.query.api.message.AuditResultMessage;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 机器审核通过，如果是灰色用户转人工审核,mq后续处理逻辑
 */
public class TurnPersonAuditMqProcessor implements RabbitmqMessageProcesser<AuditResultMessage>{

    Logger logger = LoggerFactory.getLogger(TurnPersonAuditMqProcessor.class);

    @Autowired
    private OrderDao orderDao;

    @Override
    public void processMessage(AuditResultMessage message) {
        logger.info("机审通过,但是客户是灰色用户,转人审消费者入口参数:"+ JSON.toJSON(message));
        if (message != null){
                OrderInfo orderInfo = new OrderInfo();
                //订单号
                orderInfo.setOrderNo(message.getOrderNo());
                //人审还是机审(0：机审；1：人审)
                orderInfo.setWhoAudit(1);
                logger.info("机审通过,但是客户是灰色用户,转人审的订单信息:"+JSON.toJSON(orderInfo));
            try {
                orderDao.update(orderInfo);
                logger.info("机审通过,但是客户是灰色用户,转人审成功");
            }catch (Exception e){
                logger.error("Turn Person Audit fail",e);
            }

        }else {
            logger.error("message is null!");
        }


    }
}
