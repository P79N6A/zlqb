package com.nyd.capital.ws.controller;

import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.service.impl.WsmFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Yuxiang Cong
 **/
@RestController
@RequestMapping("/capital/wsm")
public class WsmController {

    @Autowired
    private WsmFundService wsmFundService;


    @RequestMapping(value="/sendOrder",produces="text/plain;charset=UTF-8")
    public String sendOrder(){
        try {
//            List orderList = sendAndQueryOrder.getOrderList();
//            String result = sendAndQueryOrder.sendOrder(orderList);
//            System.out.println(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
//            return result;
           List orders = wsmFundService.generateOrdersTest();
            System.out.println(wsmFundService.sendOrder(orders));
        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }
        return "haha";
    }
    @RequestMapping(value = "/index",produces="text/plain;charset=UTF-8")
    public String index(){
        return "欢迎index";
    }

    /**
     * 回调接口返回的类型 {"bank":"大兴安岭农村商业银行股份有限公司","contract_link":"https%3A%2F%2Fwww.bestsign.cn%2Fopenpage%2FcontractDownloadMobile.page%3Fmid%3D45b4074c9c7a4ae7ba29547753331413%26sign%3DE9GMYkfXXKuHPp1zVqjtR7h8KRvwNGxOQGt0GkhtEx5CEejdB9B%252FaSe%252BfggSWQqKCngwrJxDvGjm5V9CnWktNTHp7sFsmKjqYLvzKR5elStqZOXhDM9waJ07S%252BHQW%252FuIxNZVdBfIezu8HOfg3Mqkqomt7qH7ImCDm39h1iNEDAw%253D%26fsdid%3D15103878131611XWL2%26status%3D3","shddh":"1000001912306736","sign":"46547a3f87fe490c8eba43bfe200e468","service_cost":"293","state":"success","bank_rate":"0.0550","errorcode":"","pay_time":"2017-11-11 16:10:21"}
     * @param result
     * @return
     */
    @RequestMapping(value="/callback",produces="text/plain;charset=UTF-8")
    public String callback(@RequestBody String result){
        System.out.println("回调信息");
        System.out.println(wsmFundService.saveLoanResult(result));
      return "success";
    }

    /**
     * 查询的信息 [{"state":"success","shddh":"1000001912306736","errorcode":"","pay_time":"2017-11-11 16:10:21","contract_link":"https%3A%2F%2Fwww.bestsign.cn%2Fopenpage%2FcontractDownloadMobile.page%3Fmid%3D45b4074c9c7a4ae7ba29547753331413%26sign%3DE9GMYkfXXKuHPp1zVqjtR7h8KRvwNGxOQGt0GkhtEx5CEejdB9B%252FaSe%252BfggSWQqKCngwrJxDvGjm5V9CnWktNTHp7sFsmKjqYLvzKR5elStqZOXhDM9waJ07S%252BHQW%252FuIxNZVdBfIezu8HOfg3Mqkqomt7qH7ImCDm39h1iNEDAw%253D%26fsdid%3D15103878131611XWL2%26status%3D3","service_cost":"293","bank_rate":"0.0550","sign":"46547a3f87fe490c8eba43bfe200e468","bank":"\u5927\u5174\u5b89\u5cad\u519c\u6751\u5546\u4e1a\u94f6\u884c\u80a1\u4efd\u6709\u9650\u516c\u53f8"}]
     * @param
     * @param
     * @return
     */
    @RequestMapping(value="/query/{mid}/{shddh}",produces="text/plain;charset=UTF-8")
    public String query(@PathVariable String mid, @PathVariable String shddh){
        System.out.println("查询信息query");
        try {
            List<String> list = new ArrayList<>();
            list.add(shddh);
            WsmQuery query = new WsmQuery();
            query.setMid(mid);
            query.setShddhList(list);
            String result = wsmFundService.queryOrderInfo(query);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }

    }

    public static void main(String[] args) throws Exception{
        System.out.println(URLDecoder.decode("https%3A%2F%2Fwww.bestsign.cn%2Fopenpage%2FcontractDownloadMobile.page%3Fmid%3D45b4074c9c7a4ae7ba29547753331413%26sign%3DE9GMYkfXXKuHPp1zVqjtR7h8KRvwNGxOQGt0GkhtEx5CEejdB9B%252FaSe%252BfggSWQqKCngwrJxDvGjm5V9CnWktNTHp7sFsmKjqYLvzKR5elStqZOXhDM9waJ07S%252BHQW%252FuIxNZVdBfIezu8HOfg3Mqkqomt7qH7ImCDm39h1iNEDAw%253D%26fsdid%3D15103878131611XWL2%26status%3D3","UTF-8"));
    }
}
