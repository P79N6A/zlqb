package com.nyd.capital.ws.utils;

import net.sf.json.util.JSONTokener;

/**
 * Cong Yuxiang
 * 2017/11/11
 **/
public class UnicodeUtils {
    public static String unicode2String(String unicode){
        return new JSONTokener(unicode).nextValue().toString();
    }


    public static void main(String[] args)throws Exception{
//        (sss).nextValue().toString();
        System.out.println(new JSONTokener("[{\"state\":\"success\",\"shddh\":\"1000001912306736\",\"errorcode\":\"\",\"pay_time\":\"2017-11-11 16:10:21\",\"contract_link\":\"https%3A%2F%2Fwww.bestsign.cn%2Fopenpage%2FcontractDownloadMobile.page%3Fmid%3D45b4074c9c7a4ae7ba29547753331413%26sign%3DE9GMYkfXXKuHPp1zVqjtR7h8KRvwNGxOQGt0GkhtEx5CEejdB9B%252FaSe%252BfggSWQqKCngwrJxDvGjm5V9CnWktNTHp7sFsmKjqYLvzKR5elStqZOXhDM9waJ07S%252BHQW%252FuIxNZVdBfIezu8HOfg3Mqkqomt7qH7ImCDm39h1iNEDAw%253D%26fsdid%3D15103878131611XWL2%26status%3D3\",\"service_cost\":\"293\",\"bank_rate\":\"0.0550\",\"sign\":\"46547a3f87fe490c8eba43bfe200e468\",\"bank\":\"\\u5927\\u5174\\u5b89\\u5cad\\u519c\\u6751\\u5546\\u4e1a\\u94f6\\u884c\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8\"}]").nextValue().toString());
    }
}
