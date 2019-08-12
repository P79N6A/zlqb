package com.zhiwang.zfm.common.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;


public class PdfGenerator {
	
	private static final Logger LOGGER = Logger.getLogger(PdfGenerator.class);
	
	public static void generate(String htmlStr, OutputStream out) throws Exception {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(htmlStr.getBytes("UTF-8")));
			ITextRenderer renderer = new ITextRenderer();
			
			renderer.getFontResolver().addFont(PdfGenerator.class.getResource("") + "simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocument(doc, null);
			renderer.layout();
			renderer.createPDF(out);
			out.close();
		} catch (Exception e) {
			LOGGER.error("生成pdf失败",e);
			throw new Exception(e);
		}
	}

}
