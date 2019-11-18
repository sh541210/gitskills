package com.iyin.sign.system.service;

import com.iyin.sign.system.common.utils.msg.SendMessageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: SendMessageUtilTest
 * @Description: SendMessageUtilTest
 * @Author: luwenxiong
 * @CreateDate: 2019/9/3 17:51
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/3 17:51
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class SendMessageUtilTest {

    @Autowired
    SendMessageUtil sendMessageUtil;

    @Test
    public void sendMessageTest(){
        String phone="13798374338";
        String text="hello word";
        sendMessageUtil.doSendSMS(phone,text);
    }
}
