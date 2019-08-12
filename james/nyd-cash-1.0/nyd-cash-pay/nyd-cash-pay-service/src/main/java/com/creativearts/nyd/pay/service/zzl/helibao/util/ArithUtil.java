package com.creativearts.nyd.pay.service.zzl.helibao.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 
 * 功能说明：提供一些精确的运算
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
public class ArithUtil {
	//默认除法运算精度
	private static final int DEF_DIV_SCALE =2;
	
	//用于比较double是否等于0
	private static final BigDecimal big0 =  new BigDecimal("0");
	
	/**
	 * 
	 * 功能说明：提供精确的加法运算。			
	 * panye  2015-5-13
	 * @param 
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double add(double v1,double v2){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 
	 * 功能说明：提供精确的加法运算。			
	 * panye  2015-5-13
	 * @param 
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double add(double v1,double v2,int scale){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		Double result=b1.add(b2).doubleValue();
		return round(result,scale) ;
	}
	
	/** 
	 * @Description: 提供精确的加法运算。
	 * @Author: taohui   
	 * @param v
	 * @return
	 * @CreateDate: 2019年2月21日 下午5:16:38
	 * @throws Exception 异常
	 */
	public static double add(double... v) {
		BigDecimal b = new BigDecimal(Double.toString(v[0]));
		for (int i = 1; i < v.length; i++) {
			BigDecimal b2 = new BigDecimal(Double.toString(v[i]));
			b = b.add(b2);
		}
		return b.doubleValue();
	}

	/**
	 * 求余
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divideAndRemainder(double v1,double v2){
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.divideAndRemainder(b2)[1].doubleValue();
	}
	
	
	
	/**
	 * 
	 * 功能说明：提供精确的减法运算。			
	 * panye  2015-5-13
	 * @param  v1 减数 v2 被减数
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double sub(double v1,double v2){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		return b1.subtract(b2).doubleValue();
	}
	
	
	/**
	 * 
	 * 功能说明：提供精确的乘法运算。			
	 * panye  2015-5-13
	 * @param  
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double mul(double v1,double v2){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		return b1.multiply(b2).doubleValue();
	}
	
	
	/**
	 * 
	 * 功能说明：提供精确的除法运算	当发生除不尽的情况时，精确到2数小数。	
	 * panye  2015-5-13
	 * @param  v1 被除数 v2 除数
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double div(double v1,double v2){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		return div(v1,v2,DEF_DIV_SCALE);
	}
	
	
	
	/**
	 * 
	 * 功能说明：提供（相对）精确的除法运算。当发生除不尽的情况时
	 * panye  2015-5-13
	 * @param  v1 被除数 v2 除数 scale 表示表示需要精确到小数点以后几位。
	 * @return   double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double div(double v1,double v2,int scale){
		
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return 0.0;}
		if(scale<0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
      public static String divPercentage(double v1,double v2){
		
		BigDecimal b1 = BigDecimal.valueOf(v1*100);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return "0.00%";}
		
		BigDecimal c = b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
		System.out.println(c);
		return c.toString()+"%";
	}
	
	/**
	 * 
	 * 功能说明：提供精确的小数位四舍五入处理。
	 * panye  2015-5-13
	 * @param  v被处理的数据  scale 小数点后保留几位
	 * @return  double 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static double round(double v,int scale){
		
		if(scale<0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	/**
	 * 
	 * 功能说明：截取整数位
	 * panye  2015-5-13
	 * @param  num double类型
	 * @return  int 
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static int getInteg(double num){
		String s=round(num,2)+"";
		int index=s.indexOf('.');
		String l=s.substring(0,index);
		return Integer.parseInt(l);
	}
	
	
	
	/**
	 * 
	 * 功能说明：将无转换成分			
	 * panye  2015-5-13
	 * @param 
	 * @return   String 转换后的分的字符串
	 * @throws  
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static String toPenny(double money) {
		
		String amt = mul(money, 100) + "";
		BigDecimal decimal = new BigDecimal(amt);
		amt = decimal.toPlainString();
		int index = amt.lastIndexOf(".");
		if (index > 0) {
			amt = amt.substring(0, index);
		}
		return amt;
	}
	
	
	/**
	 * 	
	 * 功能说明：将DOUBLE转换成字符串		
	 * panye  2015-5-13
	 * @param 
	 * @return   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：
	 * 修改人：panye
	 * 修改内容：
	 * 修改注意点：
	 */
	public static  String formatDouble(double data){
		DecimalFormat df=new DecimalFormat("0.00");
		return df.format(data);
	}
	
		
	/**
	 * 功能说明：四舍五入
	 * yuanhao  2015-6-19
	 * @param
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static double getDouble(double amt){
		BigDecimal b = new BigDecimal(amt); 
		return  b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static Double fmtAmt(double amt){
		
		String str = amt + "";
		int i = str.lastIndexOf(".");
		if(i == -1) {
			str = str + ".00";
		}
		int x = str.substring(str.lastIndexOf(".")+1, str.length()).length();
		if(x >= 2) {
			return Double.parseDouble(str.substring(0, str.lastIndexOf(".") + 3));
		}
		
		if(x == 1) {
			
			return Double.parseDouble(str.substring(0, str.lastIndexOf(".") + 2));
		}
		
		return ArithUtil.getDouble(amt);
	}
	
	public static double noRounding(double amt){
		 DecimalFormat formater = new DecimalFormat();
       formater.setMaximumFractionDigits(2);
       formater.setGroupingSize(0);
       formater.setRoundingMode(RoundingMode.FLOOR);
     
       return Double.parseDouble(formater.format(amt));
	}
	
    public static String divForBigDecimal(BigDecimal b1,BigDecimal b2,int scale){
		
		if(b1.compareTo(big0) == 0 && b2.compareTo(big0) == 0){return "0.00";}
		if(scale<0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).toString();
	}
	
}