package com.iyin.sign.system.common.utils.msg;

import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.shortmessage.ShortMessageCall;
import com.iyin.sign.system.common.utils.msg.message.MailTool;
import com.iyin.sign.system.common.utils.msg.message.SmsTool;
import com.iyin.sign.system.model.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author chenchunliang
 * @version v1.0.0
 * @Title: SendMessageUtil
 * @Description:手机消息发送工具类
 * @date 2017/6/19 10:29
 */
@Slf4j
@Service
public class SendMessageUtil {

    @Value("${sms.sn}")
    String smsSn;
    @Value("${sms.pwd}")
    String smsPwd;
    @Value("${sms.service-url}")
    String serviceUrl;

    @Value("${email.host}")
    String emailHost;
    @Value("${email.port}")
    String port;
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

    public static final String MOBILE_PHONE_REGEX = "^1[\\d]{10}";

    public static final String EMAIL_REGEX = ("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 发送短信
     *
     * @param mobilePhoneNo 手机号
     * @param text          文字内容
     * @return void
     * @Author: 刘志
     * @CreateDate: 2018/10/29 16:02
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/10/29 16:02
     * @Version: 0.0.1
     */
    @ShortMessageCall
    @Async("taskExecutor")
    public void doSendSMS(String mobilePhoneNo, String text) {
        log.info("doSendSMS start");
        if (StringUtils.isEmpty(mobilePhoneNo) || !(mobilePhoneNo.matches(MOBILE_PHONE_REGEX))) {
            throw new BusinessException(ErrorCode.PHONE_ERROR);
        }

        String encodeChart = "UTF-8";
        try {
            SmsTool msgUtil = new SmsTool(smsSn, smsPwd, serviceUrl);
            // 短信内容必须UTF-8编码
            String content = URLEncoder.encode(text, encodeChart);
            msgUtil.mdsmssend(mobilePhoneNo, content, "", "", "", "");
            log.info("api-cloud-sign 成功发送短信：phone={}, text={}", mobilePhoneNo, text);
        } catch (UnsupportedEncodingException e) {
            log.error("短信工具类初始化异常：{}", e);
            throw new BusinessException(ErrorCode.SMS_SEND_ERROR);
        }
    }

    /**
     * 发送带图片的邮件
     *
     * @param email           目标邮箱地址
     * @param text            文字内容
     * @param emailPictureUrl 图片在项目中的路径
     * @param cId             关联图片的内容Id
     * @return void
     * @Author: 刘志
     * @CreateDate: 2018/10/29 15:52
     * @UpdateUser: 刘志
     * @UpdateDate: 2018/10/29 15:52
     * @Version: 0.0.1
     */
    @Async("taskExecutor")
    public void doSendEmailWithPicture(String email, String text, String emailPictureUrl, String cId) {

        if (StringUtils.isEmpty(email) || !(email.matches(EMAIL_REGEX))) {
            throw new BusinessException(ErrorCode.EMAIL_ERROR);
        }

        try {
            MailTool mailTool = new MailTool(emailHost, port, emailAddress, emailUserName, emailPassword, emailSender);
            mailTool.sendMail(email, emailTitle, text, emailPictureUrl, cId, null);
        } catch (Exception e) {
            log.error("邮件发送失败：{}", e);
            throw new BusinessException(ErrorCode.MAIL_SEND_ERROR);
        }

    }
}
