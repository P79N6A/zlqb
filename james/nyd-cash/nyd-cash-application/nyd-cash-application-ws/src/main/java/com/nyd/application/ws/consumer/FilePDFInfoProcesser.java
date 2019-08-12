package com.nyd.application.ws.consumer;

import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.mongo.FilePDFInfo;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FilePDFInfoProcesser implements RabbitmqMessageProcesser<FilePDFInfo> {
    private static Logger logger = LoggerFactory.getLogger(FilePDFInfoProcesser.class);

    @Autowired
    private QiniuContract qiniuContract;

    @Override
    public void processMessage(FilePDFInfo pdfInfo) {
        String userId = pdfInfo.getUserId();
        logger.info("MQ_PDFfile处理消息开始>>>>{}", userId);

        try {
            qiniuContract.saveFilePDFInfo(userId);
            logger.info("MQ_PDFfile处理消息成功>>>>{}", userId);
        } catch (Exception e) {
            logger.error("MQ_PDFfile处理消息异常>>>>{}", e.toString());
        }
        logger.info("MQ_PDFfile处理消息结束>>>>{}", userId);
    }
}
