package com.nyd.batch.service;

import com.nyd.batch.service.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;

/**
 * Cong Yuxiang
 * 2018/1/2
 **/
public class App {
    public static void main(String[] args) {
//        ApplicationContext context =
//                new ClassPathXmlApplicationContext("classpath:com/nyd/batch/configs/service/xml/nyd-batch-mail.xml");
//
//        CuimiMail mm = (CuimiMail) context.getBean("mailMail");
//        mm.sendMail("Yiibai", "This is text content");
//        List<CuimiInfo> result = new ArrayList<>();
//        CuimiInfo info = new CuimiInfo();
//        info.setSerialNo("123");
//        result.add(info);
//
//        File file = new File("E:\\催收文件");
//        if(!file.exists()){
//            file.mkdirs();
//        }
//        File file1 = new File("E:\\催收文件\\催米.xlsx");
//        file1.delete();
//        if(!file1.exists()){
//            try {
//                file1.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            FileOutputStream outputStream = new FileOutputStream(file1);
//            ExcelKit.$Builder(CuimiInfo.class).toExcel(result,"催米",outputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        c.getTime();
        System.out.println(DateUtil.getDay(DateUtils.addDays(c.getTime(),-1),DateUtils.addDays(c.getTime(),-2)));
    }
}
