package com.zeus.test;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.model.helibao.NotifyResponseVo;
import com.nyd.zeus.dao.BillWsmDao;
import com.nyd.zeus.entity.Repay;
import com.nyd.zeus.model.ChargebackWsmVo;
import com.nyd.zeus.service.ChargebackWsmService;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/nyd/zeus/configs/ws/nyd-zeus-application.xml"})
public class ChargebackWsmTest {
    @Autowired
    private ChargebackWsmService chargebackWsmService;
    @Autowired
    private BillWsmDao billWsmDao;
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Test
    public void testSave(){
        ChargebackWsmVo vo = new ChargebackWsmVo();
        vo.setMerchant("cyxtest");
        vo.setReconciliationDay("2017-10-23");
        vo.setContractStartTime("2017-10-24 12:12:12");
        vo.setContractEndTime("2017-10-25 12:12:12");
//        try {
//            chargebackWsmService.save(vo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Test
    public void testSelect(){
//        System.out.println(billWsmDao.selectBy());
    }
    @Test
    public void insertLog() throws Exception {
        String directory = "E:\\tmplog";
        File directoryFile = new File(directory);
        File[] files = directoryFile.listFiles();
        for (File file:files){
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String data = null;
            while((data = br.readLine())!=null){
//                String[] dateArray = data.split(" ");
//                String date = dateArray[0]+" "+dateArray[1];
//                DateFormatUtils.format(DateUtils.parseDate(directory,"yyyy-MM-dd HH:mm:ss.SSS"),"yyyy-MM-dd HH:mm:ss");

                String[] jsons = data.split("回调");
                String json = jsons[1];
//                RepayInfo info = new RepayInfo();
                Repay repay = new Repay();
                NotifyResponseVo vo = JSON.parseObject(json, NotifyResponseVo.class);
                if("0000".equals(vo.getRt2_retCode())){
                    if(vo.getRt5_orderId().contains("_")) {
                        repay.setRepayStatus("0");
                        repay.setBillNo(vo.getRt5_orderId().split("_")[0]+"_"+DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss").getTime());
                        repay.setRepayAmount(new BigDecimal(vo.getRt7_orderAmount()));
                        repay.setRepayChannel("hlb");
                        repay.setRepayTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repay.setRepayNo(vo.getRt6_serialNumber());
                        repay.setRepayType(RepayType.MFEE.getCode());
                        repay.setCreateTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        crudTemplate.save(repay);
                    }else {
                        repay.setRepayStatus("0");
                        repay.setBillNo(vo.getRt5_orderId().split("-")[0]);
                        repay.setRepayAmount(new BigDecimal(vo.getRt7_orderAmount()));
                        repay.setRepayChannel("hlb");
                        repay.setRepayTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repay.setRepayNo(vo.getRt6_serialNumber());
                        repay.setRepayType(RepayType.UNKNOW.getCode());
                        repay.setCreateTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        crudTemplate.save(repay);
                    }
                }else {
                    if(vo.getRt5_orderId().contains("_")) {
                        repay.setRepayStatus("1");
                        repay.setBillNo(vo.getRt5_orderId().split("_")[0]+"_"+DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss").getTime());
                        repay.setRepayAmount(new BigDecimal(vo.getRt7_orderAmount()));
                        repay.setRepayChannel("hlb");
                        repay.setRepayTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repay.setRepayNo(vo.getRt6_serialNumber());
                        repay.setRepayType(RepayType.MFEE_FAIL.getCode());
                        repay.setCreateTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        crudTemplate.save(repay);
                    }else {
                        repay.setRepayStatus("1");
                        repay.setBillNo(vo.getRt5_orderId().split("-")[0]);
                        repay.setRepayAmount(new BigDecimal(vo.getRt7_orderAmount()));
                        repay.setRepayChannel("hlb");
                        repay.setRepayTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repay.setRepayNo(vo.getRt6_serialNumber());
                        repay.setRepayType(RepayType.UNKNOW_FAIL.getCode());
                        repay.setCreateTime(DateUtils.parseDate(vo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        crudTemplate.save(repay);
                    }
                }

//                System.out.println(JSON.toJSONString(repay));
            }
        }
    }
}
