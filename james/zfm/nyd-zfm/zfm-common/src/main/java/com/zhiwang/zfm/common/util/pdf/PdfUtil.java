package com.zhiwang.zfm.common.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 生成pdf
 * @author wangcy
 *
 */
public class PdfUtil {
	
	private static final Logger logger = Logger.getLogger(PdfUtil.class);
	
	public static void generator(String savePath,String templateName,Map<String,Object> variables) throws Exception{
		// 生成html
		String htmlStr = HtmlGenerator.htmlGenerate(templateName, variables);
		OutputStream out = new FileOutputStream(savePath);
//		savePath.replace('\\', '/');.replace('\\', '/')
		PdfGenerator.generate(htmlStr, out);
	}

	
	public static void main(String[] args) {
		Map<String,Object> variables = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("proName", "周先生");
		map.put("persiod", "3");
		map.put("name", "张三");
		map.put("ic", "340222198812103512");
		map.put("date", "2016-03-21");
		
		variables.put("po", map);
		
		try {
			File file = new File("E:\\baaka\\2018\\");
			if(!file.exists()) {
				file.mkdirs();
			}
			generator("E:\\baaka\\2018\\a.pdf", "expInvAgreement.ftl", variables);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return
	 */
	private static List getDatas() {
		List list = new ArrayList();
		for(int i = 1 ;i < 49; i ++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("persiod", ""+i);
			map.put("date", "2016-06-03");
			map.put("prical", "200."+i);
			map.put("interest", "20."+i);
			map.put("amt", "220."+i);
			map.put("surAmt", "1000."+i);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 测试债权合同生成
	 */
	private static void testTransfer(){
		Map<String,Object> variables = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("contractNumber", "621462000001547841");//合同编号
		
		map.put("selRealName", "周先生");//卖家姓名
		map.put("selIc", "621462000001547847");//身份证号
		map.put("selUsername", "测试1");//卖家用户名
		map.put("buyRealName", "周女士");//买家姓名
		map.put("buyIc", "121462000001547847");//买家身份证
		map.put("buyUsername", "测试2");//买家用户名
		
		map.put("initContractNumber", "621462000001547842");//原始订单协议号(初始借款协议编号)
		map.put("loanName",  "测试3");//债务人姓名
		map.put("loanCust", "221462000001547847");//身份证号码
		map.put("initPrincipal", "220.01");//初始借款本金（人民币）
		map.put("fmtInitPrincipal", "220.02");//实际本金
		map.put("seDate", "2015-1-1");//还款起始日期
		map.put("contractpayDay", "11");//每月还款日
		map.put("proRate", "0.01");//年利率
		map.put("periods", "2015-1-1");//初始借款期限（月）
		map.put("lastPeriod", "30");//剩余还款期限（月）
		map.put("surTotalPrincipal", "10000");//剩余债权（本金）总额
		map.put("transferAmt", "1000");//拟转让债权（本金）金额
		map.put("transferPrice", "1000");//债权转让价格
		
		map.put("transferFees", "1");//手续费
		map.put("fmtTransferPrice", "1000");//原始债权价格
		
		map.put("periodsType", "周");//1.月2.周3.日
		
		variables.put("po", map);
//		try {
//			//generator("f:/transfer.pdf", "transfer.ftl", variables);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (TemplateException e) {
//			e.printStackTrace();
//		}
	}
	
}
