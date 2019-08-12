package com.nyd.settlement.ws.controller;

import com.alibaba.fastjson.JSON;
import com.ibank.pay.api.service.PayService;
import com.ibank.pay.model.alipay.AliRefundResponse;
import com.ibank.pay.model.api.RefundRequest;
import com.ibank.pay.model.api.WeiXinRefundRequest;
import com.ibank.pay.model.weixinpay.WeixinRefundResponse;
import com.nyd.settlement.entity.YmtOrder;
import com.nyd.settlement.entity.refund.YmtRefund;
import com.nyd.settlement.entity.repay.YmtPayFlow;
import com.nyd.settlement.model.dto.RecommendFeeQueryDto;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.nyd.settlement.model.dto.RefundDetailDto;
import com.nyd.settlement.model.dto.ValuationFeeQueryDto;
import com.nyd.settlement.service.CostQueryService;
import com.nyd.settlement.service.YmtOrderService;
import com.nyd.settlement.service.YmtPayFlowService;
import com.nyd.settlement.service.YmtRefundService;
import com.nyd.settlement.service.utils.DateUtil;
import com.nyd.settlement.ws.utils.ExcelVersionUtils;
import com.nyd.settlement.ws.utils.FilePath;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author chaiming
 */
@RestController
@RequestMapping("/ymt/query")
public class CostQueryController {
    private static Logger logger = LoggerFactory.getLogger(CostQueryController.class);

    @Autowired
    private CostQueryService costQueryService;

    @Autowired
    private YmtRefundService ymtRefundService;

    @Autowired
    private YmtPayFlowService ymtPayFlowService;

    @Autowired
    private YmtOrderService ymtOrderService;

    @Autowired
    private PayService payServiceYmt;



    /**
     * 评估费查询
     */
    @RequestMapping(value = "/valuationQuery", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryValuation(@RequestBody ValuationFeeQueryDto dto) {
        ResponseData responseData = ResponseData.success();
        responseData.setData(costQueryService.queryValuationFee(dto));
        return responseData;
    }


    /**
     * 推荐费查询
     */
    @RequestMapping(value = "/recommendQuery", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryRecommend(@RequestBody RecommendFeeQueryDto dto) {
        ResponseData responseData = ResponseData.success();
        responseData.setData(costQueryService.queryRecommendFee(dto));
        return responseData;
    }

    /**
     * 已退款列表查询
     */
    @RequestMapping(value = "/refundDetailQuery", method = RequestMethod.POST, produces = "application/json")
    public ResponseData queryReFundDetail(@RequestBody RefundDetailDto dto) {
        ResponseData responseData = ResponseData.success();
        responseData.setData(ymtRefundService.findRefundDetail(dto));
        return responseData;
    }

    /**
     * 推荐费退款(仅仅记录一条记录到表里面)，点击退款按钮进行的操作
     */
    @RequestMapping(value = "/recommendRefund", method = RequestMethod.POST, produces = "application/json")
    public ResponseData appendRecommendRefund(@RequestBody RecommendRefundDto dto) {
//        dto.setRefundTime(dto.getRefundTime()+" 00:00:00");
        return ymtRefundService.addRecommendRefund(dto);
    }



    /**
     * 推荐费批量退款(将Excel表里的记录，记录到表t_refund里面)，点击批量退款进行的操作
     */
    @RequestMapping(value = "/import/recommendRefundBatch")
    public ResponseData batchControl(@RequestParam("file") MultipartFile file) {
        ResponseData responseData = ResponseData.success();

        //判断文件是否为空
        if(file==null){
            logger.info("文件不能为空！");
            return  ResponseData.error("文件为空");
        }
        //获取文件名
        String fileName=file.getOriginalFilename();

        //验证文件名是否合格
        if(!ExcelVersionUtils.validateExcel(fileName)){
            logger.info("文件必须是excel格式！");
            ResponseData.error("文件不是excel格式");
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(StringUtils.isEmpty(fileName) || size==0){
            logger.info("文件不能为空！");
            ResponseData.error("文件为空");
        }

        String uploadPath = FilePath.getUploadPath();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + File.separator +new Date().getTime()+ file.getOriginalFilename();
        File tempFile = new File(filePath);

//            File uploadDir = new  File("E:\\fileupload");
//            //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();
//            }
//            //新建一个文件
//            File tempFile = new File("E:\\fileupload\\" + new Date().getTime() + ".xlsx");

        //初始化输入流
        InputStream is = null;
        Workbook wb = null;//根据版本选择创建Workbook的方式
        try{
            //将上传的文件写入新建的文件中
            file.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据文件名判断文件是2003版本还是2007版本
            if(ExcelVersionUtils.isExcel2007(fileName)){
                wb = new XSSFWorkbook(is);
            }else{
                wb = new HSSFWorkbook(is);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.info("创建Workbook失败");
        } finally{
            if(is !=null) {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        //得到第一个shell
        Sheet sheet=wb.getSheetAt(0);

        //获得表头
        Row rowHead = sheet.getRow(0);
        logger.info("表头数量："+rowHead);

        //判断表头是否正确
        if(rowHead.getPhysicalNumberOfCells() != 3) {
            logger.info("表头的数量不对");
            return ResponseData.error("表头的数量不对");
        }

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        logger.info("总行数:"+totalRowNum);
        logger.info("总列数："+rowHead.getPhysicalNumberOfCells());

        //想要获得属性
        String repayNo = "";                 //支付宝交易编号
        String refundAmount = "";            //退款金额
        String refundType = "";              //退款类型
        String refundFee = "";               //手续费
        String refundTime = "";              //退款时间

        //获得所有数据        （备注：行、列均是从0开始算的）
        for(int i = 3 ; i <= totalRowNum - 1 ; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            //获得第i行第2列的：   支付宝交易号
            if (row.getCell(1) != null){
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell2 = row.getCell((short) 1);
                repayNo = cell2.getStringCellValue();
                logger.info("支付宝交易号："+repayNo);
            }

            //获得第i行第3列的：   退款金额
            if (row.getCell(2) != null){
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell3 = row.getCell((short) 2);
                refundAmount = cell3.getStringCellValue();
                logger.info("支付宝交易金额："+refundAmount);
            }

            //获得第i行第4列的：    退款类型
            if (row.getCell(3) != null){
                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell4 = row.getCell((short) 3);
                refundType = cell4.getStringCellValue();
                logger.info("支付宝交易类型："+refundType);
            }

            //获得第i行第5列的：    退款手续费
            if (row.getCell(4) != null){
                row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell5 = row.getCell((short) 4);
                refundFee = cell5.getStringCellValue();
                logger.info("退款手续费："+refundFee);
            }

            //获得第i行第6列的：     退款时间
            // 处理日期格式、时间格式  先要判断是否是时间格式，这个需要知道哪列是时间
            if (HSSFDateUtil.isCellDateFormatted(row.getCell(5))) {
                SimpleDateFormat sdf = null;
                sdf = new SimpleDateFormat("yyyy-MM-dd");  //设置转成的时间格式
                Date date = row.getCell(5).getDateCellValue();    //取得的是date类型
                refundTime = sdf.format(date);                       //这里是转成String类型了，要哪种按个人需求
                logger.info("退款时间："+refundTime);
            }

            //通过支付宝交易号去表t_ibank_repay中找到相关信息
            YmtPayFlow ymtPayFlow = ymtPayFlowService.findByRepayNo(repayNo);
            logger.info("交易对象："+JSON.toJSONString(ymtPayFlow));

            if (ymtPayFlow == null){
                logger.info("交易流水号为："+repayNo+"的交易对象不存在");
                return ResponseData.error("交易流水号为："+repayNo+"的交易对象不存在");
            }else {
                RecommendRefundDto dto = new RecommendRefundDto();
                //通过repayNo去表t_order中找到对应订单
                List<YmtOrder> list = ymtOrderService.findByRepayNo(repayNo);
                logger.info("对应订单对象："+JSON.toJSONString(list.get(0)));
                if (list.size() > 0 && list != null){
                    YmtOrder ymtOrder = list.get(0);
                    dto.setName(ymtOrder.getRealName());
                    dto.setMobile(ymtOrder.getMobile());
                    dto.setOrderNo(ymtOrder.getOrderNo());
                    dto.setRefundAccount(ymtPayFlow.getBuyerLogonId());
                    BigDecimal rd = new BigDecimal(refundAmount);
                    dto.setRefundAmount(rd);
//                    if ("推荐费".equals(refundType)){
//                        dto.setRefundType(2);
//                    }
                    dto.setRefundType(2);
                    /**
                     * 备注：此处模板中批量存入的均为支付宝退款记录，所以此处退款方式直接赋值zfb;如果模板中含有微信退款记录，
                     *   则需要在模板中新增一列退款方式。
                     */
                    dto.setRefundChannel("zfb");
                    dto.setRefundFlowNo(repayNo);
                    dto.setRefundTime(refundTime);
                    dto.setRefundReason("推荐费***退款");
                    dto.setRefundFee(new BigDecimal(refundFee));
                    dto.setRefundStatus(1);
                    logger.info("保存记录退款对象："+ JSON.toJSONString(dto));
                    /**
                     * 在进行推荐费退款记录保存之前，此处应该先做一个判断，如果退款记录表t_refund里面能找到对应的退款流水号，说明已经保存了
                     */
                    YmtRefund ymtRefund = ymtRefundService.findByRefundFlowNo(repayNo);
                    if (ymtRefund == null){
                        if (ymtRefundService.addRecommendRefund(dto).getStatus().equals("0")){
                            logger.info("退款流水号为："+repayNo+"的推荐费退款记录保存成功");
                        }else {
                            logger.info("退款流水号为："+repayNo+"的推荐费退款记录保存失败");
                            return responseData.setError("退款流水号为："+repayNo+"的推荐费退款记录保存失败");
                        }

                    }else {
                        logger.info("退款流水号为："+repayNo+"的推荐费退款记录已保存");
                        return responseData.setError("退款流水号为："+repayNo+"的推荐费退款记录已保存");
                    }

                }else{
                    logger.info("没找到退款流水号为："+repayNo+"的订单");
                    return responseData.setError("没找到退款流水号为："+repayNo+"的订单");
                }
            }

        }
        return responseData;

    }

    /**
     * 支付宝批量退款（线上进行真实的扣钱）
     *
     * 备注：扣完钱，再将交易记录保存到表里面
     */
    @RequestMapping(value = "/import/zfbRefund")
    public ResponseData zfbRefund(@RequestParam("file") MultipartFile file) {
        ResponseData responseData = ResponseData.success();
        //判断文件是否为空
        if (file == null) {
            logger.info("文件不能为空！");
            return ResponseData.error("文件为空");
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        //验证文件名是否合格
        if (!ExcelVersionUtils.validateExcel(fileName)) {
            logger.info("文件必须是excel格式！");
            ResponseData.error("文件不是excel格式");
        }
        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            logger.info("文件不能为空！");
            ResponseData.error("文件为空");
        }

//        File uploadDir = new  File("E:\\koukuanfileupload");
//        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//        //新建一个文件
//        File tempFile = new File("E:\\koukuanfileupload\\" + new Date().getTime() + ".xlsx");
        String uploadPath = FilePath.zfbUploadPath();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + File.separator + new Date().getTime() + file.getOriginalFilename();
        File tempFile = new File(filePath);
        //初始化输入流
        InputStream is = null;
        Workbook wb = null;//根据版本选择创建Workbook的方式
        try {
            //将上传的文件写入新建的文件中
            file.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelVersionUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("创建Workbook失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //获得表头
        Row rowHead = sheet.getRow(0);
        logger.info("表头数量：" + rowHead);
        //判断表头是否正确
        if (rowHead.getPhysicalNumberOfCells() != 3) {
            logger.info("表头的数量不对");
            return ResponseData.error("表头的数量不对");
        }
        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        logger.info("总行数:" + totalRowNum);
        logger.info("总列数：" + rowHead.getPhysicalNumberOfCells());
        //想要获得属性
        String repayNo = "";             //第三方流水号
        String amount = "";              //退款金额

        //支付宝退款请求对象
        RefundRequest request = new RefundRequest();
        request.setRefundReason("推荐费退款*********");
        //获得Excel所有数据
        for (int i = 3; i <= totalRowNum - 1; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            //获得第i行第2列的： 支付宝交易号
            if (row.getCell(1) != null) {
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell1 = row.getCell((short) 1);
                repayNo = cell1.getStringCellValue();
                request.setRepayNo(repayNo);
                logger.info("支付宝第三方流水号：" + repayNo);
            }
            //获得第i行第3列的： 退款金额
            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell2 = row.getCell((short) 2);
                amount = cell2.getStringCellValue();
                request.setAmount(amount);
                logger.info("退款金额：" + amount);
            }

            //通过第三方流水号repayNo从表t_ibank_repay找到本地单号tradeNo
            YmtPayFlow ymtPayFlow = ymtPayFlowService.findByRepayNo(repayNo);
            logger.info("交易对象：" + JSON.toJSONString(ymtPayFlow));
            if (ymtPayFlow != null) {
                request.setTradeNo(ymtPayFlow.getTradeNo());
                logger.info("本地单号：" + ymtPayFlow.getTradeNo());
            } else {
                responseData.error("交易对象不存在");
            }
            //发起支付宝扣款请求
            ResponseData<AliRefundResponse> result = null;
            try {
                result = payServiceYmt.tradeRefundAli(request);
                logger.info("支付宝退款请求返回的结果:" + JSON.toJSONString(result));
            }catch (Exception e){
                logger.info("支付宝退款请求fail");
                e.printStackTrace();
            }

            if ("0".equals(result.getStatus())) {                       //表示支付宝扣款请求成功
                AliRefundResponse resultData = result.getData();
                if ("Y".equals(resultData.getFund_change())) {
                    logger.info("交易流水号为："+resultData.getTrade_no()+"资金发生变化，退款成功");
                    //解析返回参数
                    String tradeNo = resultData.getTrade_no();                  //交易流水号
                    String refundAmount = resultData.getRefund_fee();           //退款金额
                    String refundTime = resultData.getGmt_refund_pay();         //退款时间
                    String buyerLogonId = resultData.getBuyer_logon_id();       //支付宝账户
                    String outTradeNo = resultData.getOut_trade_no();
                    //通过outTradeNo得到userId      11180672000001S1520511566866
                    int postion = outTradeNo.indexOf('s');
                    String userId = outTradeNo.substring(0, postion);           //用户id

                    //将支付宝扣款返回的结果记录到表t_refund里面
                    RecommendRefundDto dto = new RecommendRefundDto();

                    //通过repayNo去表t_order中找到订单
                    List<YmtOrder> list = ymtOrderService.findByRepayNo(tradeNo);
                    logger.info("对应订单对象：" + JSON.toJSONString(list.get(0)));

                    if (list.size() > 0 && list != null) {
                        YmtOrder ymtOrder = list.get(0);
                        dto.setName(ymtOrder.getRealName());
                        dto.setMobile(ymtOrder.getMobile());
                        dto.setOrderNo(ymtOrder.getOrderNo());
                        dto.setRefundAccount(buyerLogonId);
                        BigDecimal rd = new BigDecimal(refundAmount);
                        dto.setRefundAmount(rd);
                        dto.setRefundType(2);
                        dto.setRefundChannel("zfb");
                        dto.setRefundFlowNo(tradeNo);
                        dto.setRefundTime(refundTime);
                        dto.setRefundReason("推荐费退款********");
                        //退款手续费（鉴于支付宝返回字段里面没有退款手续费，故在此处设置）
                        if (new BigDecimal(resultData.getRefund_fee()).compareTo(new BigDecimal(100)) == -1) {//表示退款金额小于100元
                            dto.setRefundFee(new BigDecimal(1));
                        } else if (new BigDecimal(resultData.getRefund_fee()).compareTo(new BigDecimal(100)) == 1) {//表示退款金额大于100元
                            dto.setRefundFee(new BigDecimal(2));
                        }
                        dto.setRefundStatus(1);
                        logger.info("退款保存对象：" + JSON.toJSONString(dto));
                        if (ymtRefundService.addRecommendRefund(dto).getStatus().equals("0")) {  //表示退款记录保存到表t_refund中
                            logger.info("支付宝交易流水号为：" + tradeNo + "退款记录成功");
                            return responseData.setMsg("支付宝交易流水号为：" + tradeNo + "退款记录保存成功");
                        } else {
                            logger.info("推荐费批量退款记录保存失败：" + dto.getOrderNo());
                            return responseData.setMsg("订单号为：" + dto.getOrderNo() + "退款记录保存失败");
                        }
                    } else {
                        logger.info("支付宝交易流水号为：" + tradeNo + "没找到对应的订单信息，无法录入退款信息");
                        return responseData.setMsg("支付宝交易流水号为：" + tradeNo + "没找到对应的订单信息，无法录入退款信息");
                    }

                } else {
                    logger.info("支付宝交易流水号为：" + resultData.getTrade_no() + "资金没发生变化，退款失败");
                    return responseData.error("支付宝交易流水号为：" + resultData.getTrade_no() + "资金没发生变化，退款失败");
                }

            } else {          //退款请求失败，也写进表t_refund里面，记为未退款
                //将支付宝扣款返回的结果记录到表t_refund里面
                RecommendRefundDto dto = new RecommendRefundDto();
                //通过repayNo去表t_order中找到订单
                List<YmtOrder> list = ymtOrderService.findByRepayNo(repayNo);
                logger.info("对应订单对象：" + JSON.toJSONString(list.get(0)));
                if (list.size() > 0 && list != null) {
                    YmtOrder ymtOrder = list.get(0);
                    dto.setName(ymtOrder.getRealName());
                    dto.setMobile(ymtOrder.getMobile());
                    dto.setOrderNo(ymtOrder.getOrderNo());
                    dto.setRefundAccount(ymtPayFlow.getBuyerLogonId());
                    BigDecimal rd = new BigDecimal(amount);
                    dto.setRefundAmount(rd);
                    dto.setRefundType(2);
                    dto.setRefundChannel("zfb");
                    dto.setRefundFlowNo(repayNo);
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateformat.format(System.currentTimeMillis());
                    try {
                        dto.setRefundTime(DateUtil.transferFormat(dateStr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        logger.info(e.getMessage());
                    }
                    dto.setRefundReason("推荐费退款+++++++++");
                    //退款手续费（鉴于支付宝返回字段里面没有退款手续费，故在此处设置）
                    if (new BigDecimal(amount).compareTo(new BigDecimal(100)) == -1) {//表示退款金额小于100元
                        dto.setRefundFee(new BigDecimal(1));
                    } else if (new BigDecimal(amount).compareTo(new BigDecimal(100)) == 1) {//表示退款金额大于100元
                        dto.setRefundFee(new BigDecimal(2));
                    }
                    dto.setRefundStatus(0);
                    logger.info("退款保存对象：" + JSON.toJSONString(dto));
                    ymtRefundService.addRecommendRefund(dto); //表示退款记录保存到表t_refund中
                }
                logger.info("支付宝交易流水号为：" + repayNo + "退款请求失败");
                return responseData.error("支付宝交易流水号为：" + repayNo + "退款请求失败");
            }
        }
        return responseData;
    }


    /**
     * 微信批量退款(线上真实扣款)
     */
    @RequestMapping(value = "/import/weixinRefund")
    public ResponseData weixinRefund(@RequestParam("file") MultipartFile file) {
        ResponseData responseData = ResponseData.success();

        //判断文件是否为空
        if(file == null){
            logger.info("文件不能为空！");
            return  ResponseData.error("文件为空");
        }
        //获取文件名
        String fileName=file.getOriginalFilename();
        //验证文件名是否合格
        if(!ExcelVersionUtils.validateExcel(fileName)){
            logger.info("文件必须是excel格式！");
            ResponseData.error("文件不是excel格式");
        }
        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size=file.getSize();
        if(StringUtils.isEmpty(fileName) || size==0){
            logger.info("文件不能为空！");
            ResponseData.error("文件为空");
        }

//        File uploadDir = new  File("E:\\wxrefundfileupload");
//        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//        //新建一个文件
//        File tempFile = new File("E:\\zfbrefundfileupload\\" + new Date().getTime() + ".xlsx");
        String uploadPath = FilePath.wxUploadPath();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + File.separator +new Date().getTime()+ file.getOriginalFilename();
        File tempFile = new File(filePath);

        //初始化输入流
        InputStream is = null;
        Workbook wb = null;//根据版本选择创建Workbook的方式
        try{
            //将上传的文件写入新建的文件中
            file.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据文件名判断文件是2003版本还是2007版本
            if(ExcelVersionUtils.isExcel2007(fileName)){
                wb = new XSSFWorkbook(is);
            }else{
                wb = new HSSFWorkbook(is);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.info("创建Workbook失败");
        } finally{
            if(is !=null) {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        //得到第一个shell
        Sheet sheet=wb.getSheetAt(0);
        //获得表头
        Row rowHead = sheet.getRow(0);
        logger.info("表头数量："+rowHead);
        //判断表头是否正确
//        if(rowHead.getPhysicalNumberOfCells() != 5) {
//            logger.info("表头的数量不对");
//            return ResponseData.error("表头的数量不对");
//        }
        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();
        logger.info("总行数:"+totalRowNum);
        logger.info("总列数："+rowHead.getPhysicalNumberOfCells());
        //想要获得属性
        String out_trade_no = "";             //商户订单号
        String refund_fee = "";               //退款金额
        String total_fee = "";                //订单金额

        //微信退款请求对象
        WeiXinRefundRequest request = new WeiXinRefundRequest();
        //获得所有数据
        for(int i = 1 ; i <= totalRowNum ; i++) {
            //获得第i行对象
            Row row = sheet.getRow(i);
            //获得第i行第0列的： 商户订单号
            if (row.getCell(0) != null) {
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell0 = row.getCell((short) 0);
                out_trade_no = cell0.getStringCellValue();
                request.setOutTradeNo(out_trade_no);
                logger.info("商户订单号：" + out_trade_no);
            }

            //获得第i行第3列的： 退款金额
            if (row.getCell(1) != null) {
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                Cell cell1 = row.getCell((short) 1);
                refund_fee = cell1.getStringCellValue();
                total_fee = refund_fee;
                request.setRefundFee(new BigDecimal(refund_fee).multiply(new BigDecimal(100)).toString());
                request.setTotalFee(new BigDecimal(total_fee).multiply(new BigDecimal(100)).toString());
                logger.info("退款金额：" + refund_fee);
            }

            //发起微信退款请求
            ResponseData<WeixinRefundResponse> result = null;
            try {
                result = payServiceYmt.tradeRefundWeiXin(request);
                logger.info("商户订单号为："+out_trade_no+"退款请求成功");
                logger.info("微信退款请求返回的结果:"+JSON.toJSONString(result));
            } catch (Exception e) {
                logger.info("商户订单号为："+out_trade_no+"退款请求失败");
                return responseData.error("商户订单号为："+out_trade_no+"退款请求失败");
            }
        }
        return  responseData;
    }

}
