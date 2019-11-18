package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysDefaultConfig;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName: SignSysDefaultConfigMapperTest
 * @Description: SignSysDefaultConfigMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/8/5 18:25
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/5 18:25
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@Rollback(value = false)
@Transactional
public class SignSysDefaultConfigMapperTest {

    @Autowired
    SignSysDefaultConfigMapper signSysDefaultConfigMapper;

    @Test
    public void insert(){

        SignSysDefaultConfig signSysDefaultConfig = new SignSysDefaultConfig();
        signSysDefaultConfig.setId(SnowflakeIdWorker.getIdToStr());
        signSysDefaultConfig.setApiToken("anyin");
        signSysDefaultConfig.setLogoUrl("http://wwww.baidu.com");
        signSysDefaultConfig.setGmtCreate(new Date());
        signSysDefaultConfig.setSysName("安印签章系统");
        signSysDefaultConfig.setTimeStamp("http://www.timestamp.com");

        int count = signSysDefaultConfigMapper.insert(signSysDefaultConfig);
        Assert.assertTrue("正确",count==1);
    }
}
