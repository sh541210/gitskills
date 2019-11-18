
package com.iyin.sign.system.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
@Slf4j
public class MailToolUtil {

    @Value("${email.host}")
    String emailHost;
    @Value("${email.port}")
    int port;
    @Value("${email.address}")
    String emailAddress;
    @Value("${email.username}")
    String emailUserName;
    @Value("${email.password}")
    String emailPassword;
    @Value("${email.sender}")
    String emailSender;
    @Value("${email.title}")
    String emailTitle;


    public  boolean sendMail(String toMail, String mailTitle, String mailContent) throws Exception {
        return sendMail(toMail, mailTitle, mailContent, (String)null, (String)null, (String) null);
    }

    public  boolean sendMail(String toMail, String mailTitle, String mailContent,String imgPath, String cId ,String path) throws Exception {
            Properties props = buildProps();
            Session session = Session.getInstance(props);
            MimeMessage message = buildMimeMessage(toMail, mailTitle, session);
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(mailContent, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            //添加图片
            if(imgPath != null){
                InputStream in = this.getClass().getResourceAsStream(imgPath);
                if (in!=null){
                    ByteArrayDataSource dataSource = new ByteArrayDataSource(in, "application/x-png");
                    MimeBodyPart image = new MimeBodyPart();
                    DataHandler dh = new DataHandler(dataSource);
                    image.setDataHandler(dh);
                    image.setContentID(cId);
                    multipart.addBodyPart(image);
                    // 关联关系
                    multipart.setSubType("related");
                }
            }

            //添加附件
            if(path != null) {
                String name = path.substring(path.lastIndexOf("\\") + 1);
                DataSource source = new FileDataSource(path);
                BodyPart messageBodyPart = buildAttachedBodyPart(name, source);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart);
            message.saveChanges();
            sendEmail(session, message);
            return true;
    }

    private    BodyPart buildAttachedBodyPart(String fileName, DataSource source) throws MessagingException, UnsupportedEncodingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(MimeUtility.encodeWord(fileName));
        return messageBodyPart;
    }

    private   void sendEmail(Session session, MimeMessage message) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(emailHost, emailUserName, emailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private  MimeMessage buildMimeMessage(String toMail, String mailTitle, Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MimeUtility.encodeText(emailSender) + emailAddress));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject(mailTitle);
        return message;
    }

    private   Properties buildProps() {
        Properties props = new Properties();
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //设置邮件会话参数
        //邮箱的发送服务器地址
        props.setProperty("mail.smtp.host", emailHost);
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        //邮箱发送服务器端口,这里设置为465端口
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.auth", "true");
        return props;
    }

}
