package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysUkCertInfo;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName: SignSysUkCertInfoMapperTest
 * @Description: SignSysUkCertInfoMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/7/24 18:07
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/24 18:07
 * @Version: 0.0.1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
public class SignSysUkCertInfoMapperTest {

    @Autowired
    SignSysUkCertInfoMapper signSysUkCertInfoMapper;

    @Test
    public void insert(){
        SignSysUkCertInfo signSysUkCertInfo =new SignSysUkCertInfo();
        signSysUkCertInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysUkCertInfo.setCurrentStatus("0");
        signSysUkCertInfo.setGmtCreate(new Date());
        signSysUkCertInfo.setTrustId("11111111");
        signSysUkCertInfo.setOid("2222222");
        int count = signSysUkCertInfoMapper.insert(signSysUkCertInfo);
        Assert.assertTrue("正确",count==1);


    }
}
