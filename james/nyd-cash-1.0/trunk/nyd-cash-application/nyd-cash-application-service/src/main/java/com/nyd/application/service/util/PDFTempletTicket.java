/**
 * @Title: PDFTempletTicket.java
 * @Package: org.csun.ns.util
 * @Description: TODO
 * @Author: chisj chisj@foxmail.com
 * @Date: 2016年4月27日上午11:29:52
 * @Version V1.0
 */
package com.nyd.application.service.util;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @ClassName: PDFTempletTicket
 * @Description: TODO
 * @Author: chisj chisj@foxmail.com
 * @Date: 2016年4月27日上午11:29:52
 */
@Component
public class PDFTempletTicket {

    @Autowired
    private AppProperties appProperties;
    private String templatePdfPath;
    private Ticket ticket;


    public PDFTempletTicket() {
        super();
    }

    public PDFTempletTicket(String templatePdfPath, Ticket ticket) {
        this.templatePdfPath = templatePdfPath;
        this.ticket = ticket;
    }

    public byte[] templetTicket() throws Exception {

        PdfReader reader = new PdfReader(templatePdfPath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper ps = new PdfStamper(reader, bos);

        /*使用中文字体 */
        BaseFont bfComic = BaseFont.createFont(appProperties.getPdfFontInfo(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
        fontList.add(bfComic);

        AcroFields s = ps.getAcroFields();
        s.setSubstitutionFonts(fontList);

        Field[] declaredFields = ticket.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field key = declaredFields[i];
            key.setAccessible(true);
            String value = (String) key.get(ticket);
            s.setField(key.getName(), value);
        }

        ps.setFormFlattening(true);
        ps.close();

        return bos.toByteArray();
    }

    /**
     * @return the templatePdfPath
     */
    public String getTemplatePdfPath() {
        return templatePdfPath;
    }

    /**
     * @param templatePdfPath the templatePdfPathto set
     */
    public void setTemplatePdfPath(String templatePdfPath) {
        this.templatePdfPath = templatePdfPath;
    }


    /**
     * @return the ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}