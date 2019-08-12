package com.zhiwang.zfm.common.util.pdf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.zhiwang.zfm.common.util.pdf.freemarker.FreemarkerConfiguration;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 
 * @author wangcy
 *
 */
public class HtmlGenerator {
	
	public static String htmlGenerate(String template, Map<String,Object> variables) throws IOException, TemplateException {
		Configuration config = FreemarkerConfiguration.getConfiguation();        
		config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		config.setDefaultEncoding("utf-8");
		Template tp = config.getTemplate(template);
		StringWriter stringWriter = new StringWriter();  
		BufferedWriter writer = new BufferedWriter(stringWriter);  
		tp.setEncoding("UTF-8");  
		tp.process(variables, writer);  
		String htmlStr = stringWriter.toString();
		writer.flush();  
		writer.close();
		return htmlStr;
	}

}
