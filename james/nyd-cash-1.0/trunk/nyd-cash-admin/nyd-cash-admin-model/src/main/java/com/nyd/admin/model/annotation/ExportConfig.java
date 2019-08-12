package com.nyd.admin.model.annotation;

import org.apache.poi.hssf.util.HSSFColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导出项配置
 * Cong Yuxiang
 * 2017/11/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExportConfig {

	/**
	 * 表头显示名 默认为字段名
	 */
	String value() default "field";

	/**
	 * 单元格宽度 默认-1(自动计算列宽)
	 */
	short width() default -1;

	/**
	 *
	 */
	String convert() default "";
	
	
	/**
	 *
	 */
	short color() default HSSFColor.BLACK.index;
	

	/**
	 *
	 */
	String replace() default "";

	/**
	 * 设置单元格数据验证（下拉框）
	 */
	String range() default  "" ;
}
