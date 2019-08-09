package com.nyd.batch.service.mail;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/1/2
 **/
public class CuimiMail {
    private JavaMailSender mailSender;

    private SimpleMailMessage simpleMailMessage;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void sendMail(){

        MimeMessage message = mailSender.createMimeMessage();
        String[] toMail = {"yuxiang.cong@creativearts.cn","jiexuan.zhu@creativearts.cn","jiawei.cheng@creativearts.cn","cuishou@creativearts.cn"};
        String fileName1 = "/data/cuimi/侬要贷催收单M1" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx";
//        String fileName2 = "/data/cuimi/侬要贷催收单M1B" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx";
        String fileName3 = "/data/cuimi/侬要贷催收单M2" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx";
        String fileName4 = "/data/cuimi/侬要贷催收单M3" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx";

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(toMail);
            helper.setSubject("催收单"+ DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
            helper.setText(simpleMailMessage.getText());

            FileSystemResource file1 = new FileSystemResource(fileName1);
            if(file1.exists())
                helper.addAttachment(file1.getFilename(), file1);

//            FileSystemResource file2 = new FileSystemResource(fileName2);
//            if(file2.exists())
//                helper.addAttachment(file2.getFilename(), file2);

            FileSystemResource file3 = new FileSystemResource(fileName3);
            if(file3.exists())
                helper.addAttachment(file3.getFilename(), file3);

            FileSystemResource file4 = new FileSystemResource(fileName4);
            if(file4.exists())
                helper.addAttachment(file4.getFilename(), file4);

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }
}
