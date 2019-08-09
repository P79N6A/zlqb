package com.nyd.zeus.ws.controller;

import com.nyd.zeus.model.BillWsmVo;
import com.nyd.zeus.model.ChargebackWsmVo;
import com.nyd.zeus.service.BillWsmService;
import com.nyd.zeus.service.ChargebackWsmService;
import com.nyd.zeus.service.excel.ExcelKit;
import com.nyd.zeus.service.excel.hanlder.ReadHandler;
import com.nyd.zeus.ws.utils.Constants;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
@RestController
@RequestMapping(value = "/api/ops")
public class BillExcelController{

    @Autowired
    private ChargebackWsmService chargebackWsmService;

    @Autowired
    private BillWsmService billWsmService;

    @RequestMapping(value = "/import")
    public ResponseData importExcel(@RequestParam("file") CommonsMultipartFile file) throws Exception {

        String uploadPath = "E:" + File.separator + "upload";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + File.separator +new Date().getTime()+ file.getOriginalFilename();
        File storeFile = new File(filePath);

        OutputStream os = null;
        InputStream is = null;
        try {
            //获取输出流
            os=new FileOutputStream(storeFile);
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            is=file.getInputStream();
            int temp;
            //一个一个字节的读取并写入
            while((temp=is.read())!=(-1))
            {
                os.write(temp);
            }

            os.flush();

            final List<BillWsmVo> billWsmVos = new ArrayList<>();
            final List<ChargebackWsmVo> chargebackWsmVos = new ArrayList<>();
            final StringBuilder billWsmTable = new StringBuilder("");
            final StringBuilder chargebackWsmTable = new StringBuilder("");
            // 执行excel文件导入
            ExcelKit.$Import().readExcel(storeFile,new ReadHandler() {
                int i=0;
                int j=0;

                @Override
                public void handler(int sheetIndex, int rowIndex, List<String> row) {
//                    if(rowIndex >1) {
//                        System.out.println("######################");
//                        for(String s:row){
//
//                            System.out.println(s);
//                        }
//                        System.out.println("######################");
//                    }

                    if(sheetIndex==0){
                        if(rowIndex>1){
                            if("合计".equals(row.get(0))||row.get(0)==null||row.get(0).trim().length()==0){
                                return;
                            }
                            if(row.size()<22){
                                throw new RuntimeException("excex列的数量不足22");
                            }
                            if(billWsmTable.length()==0){
                                String date = row.get(8).split(" ")[0].replace("-","");
                                billWsmTable.append(Constants.BILL_WSM_PREFIX+date);
                                billWsmService.dropTable(billWsmTable.toString());
                                billWsmService.createTable(billWsmTable.toString());
                            }

                            BillWsmVo vo = new BillWsmVo();
                            vo.setSerialNo(Long.parseLong(row.get(0)));
                            vo.setMerchant(row.get(1));
                            vo.setIdentifier(row.get(2));
                            vo.setOrderNo(row.get(3));
                            vo.setUserName(row.get(4));
                            vo.setAmount(Double.valueOf(row.get(5)));
                            vo.setContractStartTime(row.get(6));
                            vo.setContractEndTime(row.get(7));
                            vo.setReconciliationDay(row.get(8));
                            vo.setMerchantOrderNo(row.get(9));
                            vo.setBorrowDays(Integer.valueOf(row.get(10)));
                            vo.setPeriodCount(Integer.valueOf(row.get(11)));
                            vo.setWsmServiceRate(Double.valueOf(row.get(12)));
                            vo.setServiceCostTotal(Double.valueOf(row.get(13)));
                            vo.setBankServiceCost(Double.valueOf(row.get(14)));
                            vo.setWsmServiceCost(Double.valueOf(row.get(15)));
                            vo.setExtraServiceRate(Double.valueOf(row.get(16)));
                            vo.setExtraServiceCost(Double.valueOf(row.get(17)));
                            vo.setServiceCostPayday(row.get(18));
                            vo.setBankServiceRate(Double.valueOf(row.get(19)));
                            vo.setInterestTotal(Double.valueOf(row.get(20)));
                            vo.setPayday(row.get(21));
                            billWsmVos.add(vo);
                            i++;
                            if(i==500){

                                    billWsmService.saveList(billWsmVos,billWsmTable.toString());
                                    i=0;
                                    billWsmVos.clear();
                            }

                        }
                    }else if(sheetIndex==1){
                        if(rowIndex>0) {
                            if ("合计".equals(row.get(0))||row.get(0)==null||row.get(0).trim().length()==0) {
                                return;
                            }
                            if (row.size() < 14) {
                                throw new RuntimeException("退单excex列的数量不足14");
                            }
                            if(chargebackWsmTable.length()==0){
                                String date = row.get(8).split(" ")[0].replace("-","");
                                chargebackWsmTable.append(Constants.CHARGE_BACK_WSM_PREFIX+date);
                                chargebackWsmService.dropTable(chargebackWsmTable.toString());
                                chargebackWsmService.createTable(chargebackWsmTable.toString());
                            }
                            ChargebackWsmVo vo = new ChargebackWsmVo();
                            vo.setSerialNo(Long.parseLong(row.get(0)));
                            vo.setMerchant(row.get(1));
                            vo.setIdentifier(row.get(2));
                            vo.setOrderNo(row.get(3));
                            vo.setUserName(row.get(4));
                            vo.setAmount(Double.valueOf(row.get(5)));
                            vo.setContractStartTime(row.get(6));
                            vo.setContractEndTime(row.get(7));
                            vo.setReconciliationDay(row.get(8));
                            vo.setMerchantOrderNo(row.get(9));
                            vo.setBorrowDays(Integer.valueOf(row.get(10)));
                            vo.setPeriodCount(Integer.valueOf(row.get(11)));
                            vo.setServiceRate(Double.valueOf(row.get(12)));
                            vo.setServiceCost(Double.valueOf(row.get(13)));
                            chargebackWsmVos.add(vo);

                            j++;
                            if(j==500){

                                chargebackWsmService.saveList(chargebackWsmVos,chargebackWsmTable.toString());
                                j=0;
                                chargebackWsmVos.clear();
                            }
                        }
                    }


                }

            });
            billWsmService.saveList(billWsmVos,billWsmTable.toString());
            chargebackWsmService.saveList(chargebackWsmVos,chargebackWsmTable.toString());

//            System.out.println(JSON.toJSONString(billWsmVos));
//            System.out.println(JSON.toJSONString(chargebackWsmVos));

            return ResponseData.success();


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }finally {
            if(storeFile.exists()) {
                storeFile.delete();
            }
            if(os!=null){
                os.close();
            }
            if(is!=null){
                is.close();
            }
        }


    }
    @RequestMapping("/export")
    public void exportExcel(HttpServletResponse response) throws Exception {
        ExcelKit.$Export(Object.class, response)
                .setMaxSheetRecords(5)
                .toExcel(new ArrayList<>(), "对账单");
    }

}
