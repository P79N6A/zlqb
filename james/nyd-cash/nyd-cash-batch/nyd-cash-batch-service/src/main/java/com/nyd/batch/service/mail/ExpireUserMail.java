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
 * 2018/1/10
 **/
public class ExpireUserMail {
    private JavaMailSender mailSender;

    private SimpleMailMessage simpleMailMessage;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
        this.simpleMailMessage = simpleMailMessage;
    }

    public void sendMail(String filename){

        MimeMessage message = mailSender.createMimeMessage();
        String[] toMail = {"yuxiang.cong@creativearts.cn","jiexuan.zhu@creativearts.cn","jiawei.cheng@creativearts.cn","cuishou@creativearts.cn"};

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(toMail);
            helper.setSubject("今天还款用户"+ DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
            helper.setText(simpleMailMessage.getText());

            FileSystemResource file = new FileSystemResource(filename);
            helper.addAttachment(file.getFilename(), file);

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }
}
