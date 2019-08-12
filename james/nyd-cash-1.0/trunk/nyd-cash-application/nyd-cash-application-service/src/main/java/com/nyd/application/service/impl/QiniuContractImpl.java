package com.nyd.application.service.impl;

import com.creativearts.das.query.api.CreditReportService;
import com.creativearts.das.query.api.model.CreditReportParamDto;
import com.creativearts.feature.common.util.DateUtils;
import com.creativearts.feature.syn.api.FeatureCommon;
import com.creativearts.feature.syn.api.dto.PhoneRegionDto;
import com.nyd.application.api.QiniuContract;
import com.nyd.application.model.mongo.FilePDFInfo;
import com.nyd.application.model.request.AttachmentModel;
import com.nyd.application.service.util.*;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.entity.Attachment;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dengw on 2017/11/16
 */
@Service(value = "qiniuContract")
public class QiniuContractImpl implements QiniuContract {
    private static Logger LOGGER = LoggerFactory.getLogger(QiniuContractImpl.class);

    @Autowired
    private QiniuApi qiniuApi;
    @Autowired
    private MongoApi mongoApi;
    @Autowired
    private CreditReportService creditReport;
    @Autowired(required = false)
    private UserAccountContract userAccountContract;
    @Autowired(required = false)
    private UserIdentityContract userIdentityContract;
    @Autowired(required = false)
    private FeatureCommon featureCommon;
    @Autowired
    private PDFTempletTicket pdfTempletTicket;
    @Autowired
    private AppProperties appProperties;

    @Override
    public ResponseData base64Upload(String file) {
        LOGGER.info("begin to save photo,file is " + file);
        ResponseData responseData = ResponseData.success();
        String fileName = qiniuApi.base64Upload(file);
        if (fileName == null) {
            responseData = ResponseData.error("上传七牛失败！");
        }
        responseData.setData(fileName);
        LOGGER.info("save photo success !");
        return responseData;
    }

    @Override
    public ResponseData base64Upload(AttachmentModel attachmentModel) {
        LOGGER.info("begin to upload photo,userId is " + attachmentModel.getUserId());
        ResponseData responseData = ResponseData.success();
        try {
            String filePath = qiniuApi.base64Upload(attachmentModel.getFile());
            if (filePath == null) {
                responseData = ResponseData.error("上传七牛失败！");
            } else {
                attachmentModel.setFile(filePath);
                Map<String, Object> map = CommonUtil.transBeantoMap(attachmentModel);
                mongoApi.save(map, "attachment");
            }
            responseData.setData(filePath);
            LOGGER.info("save photo success !");
        } catch (Exception e) {
            LOGGER.error("upload photo error !", e);
        }
        return responseData;
    }

    @Override
    public ResponseData download(String fileName) {
        LOGGER.info("begin to download photo,fileName is " + fileName);
        ResponseData responseData = ResponseData.success();
        String fileUrl = qiniuApi.download(fileName);
        responseData.setData(fileUrl);
        LOGGER.info("download photo success !");
        return responseData;
    }

    /**
     * 通过userId查询PDF文件qiniu路径，再到qiniu获取url
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseData downloadFilePDFInfo(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            // 查询文件路径
            FilePDFInfo filePDFInfo = mongoApi.getFilePDFInfo(userId);
            LOGGER.info("查询PDF文件qiniu路径成功：{}", filePDFInfo);

            if (filePDFInfo != null && StringUtils.isNotBlank(filePDFInfo.getFilePDFPath())) {
                // 下载PDF文件
                LOGGER.info("》》》》》该用户已生成PDF文件,开始下载");
                String filePath = qiniuApi.download(filePDFInfo.getFilePDFPath());
                responseData.setData(filePath);
                LOGGER.info("》》》》》该用户已生成PDF文件,下载完成,路径为:{}", filePath);
            }
//            else {
//                // 文件不存在，重新生成，并下载
//                LOGGER.info("》》》》》未查询到PDF文件,重新生成");
//                saveFilePDFInfo(userId);
//                LOGGER.info("》》》》》未查询到PDF文件,生成成功");
//
//                FilePDFInfo newFilePDFInfo = mongoApi.getFilePDFInfo(userId);
//                LOGGER.info("新生成PDF文件qiniu查询路径成功：{}", newFilePDFInfo);
//
//                if (newFilePDFInfo != null && StringUtils.isNotBlank(newFilePDFInfo.getFilePDFPath())) {
//                    LOGGER.info("》》》》》新生成PDF文件,开始下载");
//                    String newFilePath = qiniuApi.download(newFilePDFInfo.getFilePDFPath());
//                    responseData.setData(newFilePath);
//                    LOGGER.info("》》》》》新生成PDF文件,下载完成,路径为:{}", newFilePath);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("下载文件异常:" + e.toString());
        }
        return responseData;
    }

    /**
     * 上传PDF文件到qiniu,并保存PDF信息至mongo
     *
     * @param userId
     * @throws Exception
     */
    @Override
    public void saveFilePDFInfo(String userId) throws Exception {
        try {
            FilePDFInfo filePDFData = mongoApi.getFilePDFInfo(userId);
            LOGGER.info("查询该客户已存在信审报告信息:{}", filePDFData);

            if (filePDFData == null || (filePDFData != null && StringUtils.isBlank(filePDFData.getFilePDFPath()))) {

                List<CreditReportParamDto> creditReportParamDtos = ResponseDataUtil.responseUnifyDealList(creditReport.getCreditReport(userId), CreditReportParamDto.class);
                LOGGER.info("查询命中指标数:{},返回信息为:{}", creditReportParamDtos.size(), creditReportParamDtos.toString());

                // PDF报告编码
                String reportCode = userId + DateUtils.getDay(new Date());
                // 封装Ticket对象
                Ticket ticket = new Ticket();
                ticket = restoreTicketInfo(ticket, userId, reportCode, creditReportParamDtos);
                LOGGER.info("封装Ticket对象为:{}", ticket.toString());

                // 将信息填写到PDF文件中
                String pdfTemplatePath = appProperties.getPdfTemplatePath();
                LOGGER.info("模板路径为{}", pdfTemplatePath);

                if (StringUtils.isNotBlank(pdfTemplatePath)) {
                    File baseFile = new File(pdfTemplatePath);
                    if (!baseFile.exists()) {
                        throw new RuntimeException("PDF模板文件不存在");
                    }
                    pdfTempletTicket.setTemplatePdfPath(pdfTemplatePath);
                    pdfTempletTicket.setTicket(ticket);
                    byte[] bytes = pdfTempletTicket.templetTicket();

                    // PDF文件存储到qiniu
                    String pdfFilePath = qiniuApi.upload(bytes);
                    LOGGER.info("PDF存储qiniu成功,路径为:{}", pdfFilePath);

                    // 保存PDF关联信息存储到mongo
                    FilePDFInfo filePDFInfo = new FilePDFInfo();
                    filePDFInfo.setUserId(userId);
                    filePDFInfo.setFilePDFPath(pdfFilePath);
                    filePDFInfo.setReportCode(reportCode);
                    mongoApi.saveFilePDFInfo(filePDFInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("保存PDF文件异常:{}", e.toString());
            throw e;
        }
    }

    /**
     * 封装Ticket对象
     *
     * @param userId
     * @param creditReportData
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private Ticket restoreTicketInfo(Ticket ticket, String userId, String reportCode, List<CreditReportParamDto> creditReportData) throws NoSuchFieldException, IllegalAccessException {
        Field[] declaredFields = ticket.getClass().getDeclaredFields();
        // 默认赋值为0
        for (int i = 0; i < declaredFields.length; i++) {
            String keyTicket = declaredFields[i].getName();
            Field declaredField = ticket.getClass().getDeclaredField(keyTicket);
            declaredField.setAccessible(true);
            declaredField.set(ticket, "0");
        }
        // 查询到的指标结果进行赋值
        if (CollectionUtils.isNotEmpty(creditReportData)) {
            Map<String, Long> resultMap = creditReportData.stream().collect(Collectors.groupingBy(CreditReportParamDto::getRuleCode, Collectors.counting()));
            LOGGER.info("命中指标,数据处理结果:{}", resultMap.toString());
            if (!resultMap.isEmpty()) {
                // 剔除命中黑名单指标
                String[] keys = resultMap.keySet().toArray(new String[0]);
                for (int j = 0; j < keys.length; j++) {
                    if (keys[j].contains("A00")) {
                        resultMap.remove(keys[j]);
                    }
                }
                LOGGER.info("命中指标,剔除黑名单数据后结果:{}", resultMap.toString());
                // 当命中指标大于3条时，随机只取3条记录
                Map<String, Long> randomMap = new HashMap<>();
                if (resultMap.size() > 3) {
                    Random random = new Random();
                    for (int i = 0; i < 3; i++) {
                        String randomKey = resultMap.keySet().toArray(new String[0])[random.nextInt(resultMap.size())];
                        if (randomMap.size() > 0 && randomMap.containsKey(randomKey)) {
                            i--;
                            continue;
                        }
                        randomMap.put(randomKey, resultMap.get(randomKey));
                    }
                } else {
                    randomMap = resultMap;
                }
                LOGGER.info("命中指标,保留三条数据以内后结果:{}", randomMap.toString());
                // 指标记录赋值到ticket对应属性字段
                for (int i = 0; i < declaredFields.length; i++) {
                    String keyTicket = declaredFields[i].getName();
                    String keyTicketSub = keyTicket.substring(keyTicket.length() - 4, keyTicket.length());
                    Field declaredField = declaredFields[i];

                    for (String keyMap : randomMap.keySet()) {
                        declaredField.setAccessible(true);
                        if (keyTicketSub.equals(keyMap.trim())) {
                            declaredField.set(ticket, randomMap.get(keyMap).toString());
                        }
                    }
                }
            }
        }
        LOGGER.info("命中指标,赋值处理后结果:{}", ticket.toString());

        // 查询时间
        ticket.setQueryTime(DateUtils.convert(new Date(), "yyyy/MM/dd-HHmmss"));
        // 报告编码
        ticket.setReportCode(reportCode);
        // 用户信息
        UserDetailInfo userDetailInfo = ResponseDataUtil.responseUnifyDeal(userIdentityContract.getUserDetailInfo(userId), UserDetailInfo.class);
        LOGGER.info("NYD订单，查询用户信息为{}", userDetailInfo == null ? null : userDetailInfo.toString());
        if (userDetailInfo != null) {
            ticket.setUserName(userDetailInfo.getRealName());
            ticket.setUserCard(userDetailInfo.getIdNumber());
            ticket.setUserAddress(userDetailInfo.getIdAddress());
        }
        // 手机号和运营商数据
        AccountDto accountDto = ResponseDataUtil.responseUnifyDeal(userAccountContract.getAccount(userId), AccountDto.class);
        LOGGER.info("NYD订单，查询账户信息为{}", accountDto == null ? null : accountDto.toString());
        if (accountDto != null) {
            String telephone = accountDto.getAccountNumber();
            if (StringUtils.isNotBlank(telephone)) {
                // 封装手机号
                ticket.setUserPhone(telephone);
                PhoneRegionDto phoneRegionDto = ResponseDataUtil.responseUnifyDeal(featureCommon.queryPhoneRegion(telephone), PhoneRegionDto.class);
                LOGGER.info("NYD订单，查询号码运营商信息为{}", phoneRegionDto == null ? null : phoneRegionDto.toString());
                if (phoneRegionDto != null) {
                    String operator = phoneRegionDto.getOperator();
                    // 封装运营商数据
                    ticket.setPhoneType(operator);
                }
            }
        }
        // 封装汇总信息
        ticket.setSumBlack((Integer.parseInt(ticket.getBlackA001()) + Integer.parseInt(ticket.getBlackA002()) + Integer.parseInt(ticket.getBlackA003()) + Integer.parseInt(ticket.getBlackA004()) + Integer.parseInt(ticket.getBlackA005())) + "");
        ticket.setSumBehavior((Integer.parseInt(ticket.getBehaviorB001()) + Integer.parseInt(ticket.getBehaviorB002()) + Integer.parseInt(ticket.getBehaviorB003()) + Integer.parseInt(ticket.getBehaviorB004()) + Integer.parseInt(ticket.getBehaviorB005()) + Integer.parseInt(ticket.getBehaviorB006())) + "");
        ticket.setSumLoan((Integer.parseInt(ticket.getLoanC001()) + Integer.parseInt(ticket.getLoanC002()) + Integer.parseInt(ticket.getLoanC003())) + "");
        ticket.setSumOverdue((Integer.parseInt(ticket.getOverdueD001()) + Integer.parseInt(ticket.getOverdueD002()) + Integer.parseInt(ticket.getOverdueD003()) + Integer.parseInt(ticket.getOverdueD004()) + Integer.parseInt(ticket.getOverdueD005()) + Integer.parseInt(ticket.getOverdueD006())) + "");
        ticket.setSumMessDiff((Integer.parseInt(ticket.getMessDiffE001()) + Integer.parseInt(ticket.getMessDiffE002()) + Integer.parseInt(ticket.getMessDiffE003()) + Integer.parseInt(ticket.getMessDiffE004()) + Integer.parseInt(ticket.getMessDiffE005()) + Integer.parseInt(ticket.getMessDiffE006()) + Integer.parseInt(ticket.getMessDiffE007()) + Integer.parseInt(ticket.getMessDiffE008())) + "");
        ticket.setSumNativeMess((Integer.parseInt(ticket.getNativeMessF001()) + Integer.parseInt(ticket.getNativeMessF002()) + Integer.parseInt(ticket.getNativeMessF003()) + Integer.parseInt(ticket.getNativeMessF004()) + Integer.parseInt(ticket.getNativeMessF005()) + Integer.parseInt(ticket.getNativeMessF006()) + Integer.parseInt(ticket.getNativeMessF007()) + Integer.parseInt(ticket.getNativeMessF008()) + Integer.parseInt(ticket.getNativeMessF009()) + Integer.parseInt(ticket.getNativeMessF010()) + Integer.parseInt(ticket.getNativeMessF011())) + "");
        ticket.setSumShareMess((Integer.parseInt(ticket.getShareMessG001()) + Integer.parseInt(ticket.getShareMessG002()) + Integer.parseInt(ticket.getShareMessG003()) + Integer.parseInt(ticket.getShareMessG004()) + Integer.parseInt(ticket.getShareMessG005())) + "");
        ticket.setSumJudMess((Integer.parseInt(ticket.getJudMessH001()) + Integer.parseInt(ticket.getJudMessH002()) + Integer.parseInt(ticket.getJudMessH003()) + Integer.parseInt(ticket.getJudMessH004()) + Integer.parseInt(ticket.getJudMessH005())) + "");
        ticket.setSumTotal((Integer.parseInt(ticket.getSumBlack()) + Integer.parseInt(ticket.getSumBehavior()) + Integer.parseInt(ticket.getSumLoan()) + Integer.parseInt(ticket.getSumOverdue()) + Integer.parseInt(ticket.getSumMessDiff()) + Integer.parseInt(ticket.getSumNativeMess()) + Integer.parseInt(ticket.getSumShareMess()) + Integer.parseInt(ticket.getSumJudMess())) + "");

        return ticket;
    }

}
