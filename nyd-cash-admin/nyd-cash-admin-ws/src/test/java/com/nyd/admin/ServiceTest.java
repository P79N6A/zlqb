package com.nyd.admin;

import com.nyd.admin.model.DspReportVo;
import com.nyd.admin.model.FundInfo;
import com.nyd.admin.model.power.dto.RolePowerDto;
import com.nyd.admin.service.DspReportService;
import com.nyd.admin.service.IFundService;
import com.tasfe.framework.support.model.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:nyd-admin-application.xml"})*/
//@Env(profile = "classpath:com/nyd/msg/configs/service/xml/nyd-msg-service-local-properties.xml")
public class ServiceTest {

/*    @Autowired
    IFundService fundService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    DspReportService dspReportService;

    @Test
    public void testInsert(){
        FundInfo fundInfo = new FundInfo();
        fundInfo.setFundName("datasource");
        fundInfo.setFundCode("dataCode");
       fundService.saveFundIno(fundInfo);
    }
    @Test
    public void testMongo() throws ParseException {
//        String date = "2017-12-13 09:40:00";
//        SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//        Query query = Query.query(Criteria.where("create_time").lte(format.parse(date)));
//        long count = mongoTemplate.count(query,"szr_blacklistx");
//        System.out.println(count);
         Long a =200l;
        Long b= 15l;
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println(df.format((double)a/(double)b));
//        double c= (double)a/(double)b;
//        System.out.println(c);
    }

    @Test
    public void testDsp() {
        DspReportVo vo = new DspReportVo();
        ResponseData responseData = dspReportService.queryDspSucess(vo);

        System.out.println(responseData);
    }
}*/
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
