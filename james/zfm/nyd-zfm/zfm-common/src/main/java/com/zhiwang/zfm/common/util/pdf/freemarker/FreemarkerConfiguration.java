package com.zhiwang.zfm.common.util.pdf.freemarker;

import freemarker.template.Configuration;

public class FreemarkerConfiguration {

	private static Configuration config = null;
	
	/**
	 * Static initialization.
	 * 
	 * Initialize the configuration of Freemarker.
	 */
	static{
		config = new Configuration();
		config.setClassForTemplateLoading(FreemarkerConfiguration.class, "template");
		config.setClassicCompatible(true);
	}
	
	public static Configuration getConfiguation(){
		return config;
	}
	
}
