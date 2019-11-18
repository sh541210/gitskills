package com.iyin.sign.system.message;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.util.MailToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: UserRegisterMessageReceiver
 * @Description: 用户注册消息订阅
 * @Author: luwenxiong
 * @CreateDate: 2019/8/26 17:34
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/26 17:34
 * @Version: 0.0.1
 */
@Slf4j
@Component
public class UserRegisterMessageSub extends MessageSub {

    MailToolUtil mailToolUtil;

    public UserRegisterMessageSub(MailToolUtil mailToolUtil) {
        super("register");
        this.mailToolUtil = mailToolUtil;
    }

    @Override
    public void handlerMessage(String msg) {
        log.info("用户注册频道收到一条消息：{}", msg);
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        String email = map.get("email");
        log.info("email : {}", email);
        try {

            String title ="专业的电子合同及文件签署平台";
//            StringBuffer contentBuffer = new StringBuffer("尊敬的用户，您好：").append("<br/>");
//            contentBuffer.append("您的单位创建成功，请使用帐号"+email+"进行登录，若有疑问请联系管理员。").append("<br/>");
//            contentBuffer.append("<br/>");
//            contentBuffer.append("此为系统邮件请勿回复，如非本人操作，请忽略 ");

            String br = "<br/>";
            String cId = "headImage";
            String content ="<img src='cid:" + cId + "'/>" + br +"尊敬的用户，您好："+br+br+"您的单位创建成功，请使用帐号"+email+"进行登录，若有疑问请联系管理员。"+br+br+"此为系统邮件请勿回复，如非本人操作，请忽略 ";
            mailToolUtil.sendMail(email,title,content);
            log.info(email+"注册成功,成功发送注册邮件提醒:{}",content);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(email+"监听器接受消息，邮件发送失败,异常信息:{}",e.getStackTrace());
        }

    }
}
