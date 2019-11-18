package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysUserInfo;
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

/**
 * @ClassName: SignSysUserInfoMapperTest
 * @Description: SignSysUserInfoMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 12:25
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 12:25
 * @Version: 0.0.1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
public class SignSysUserInfoMapperTest {

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Test
    public void insert(){
        SignSysUserInfo signSysUserInfo = new SignSysUserInfo();
        signSysUserInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserInfo.setUserType("01");
        signSysUserInfo.setEnterpriseOrPersonalId(SnowflakeIdWorker.getIdToStr());
        signSysUserInfo.setFirstCreate("01");
        signSysUserInfo.setInvalidAuthTimes(5);
        signSysUserInfo.setLoginType("01");
        signSysUserInfo.setNodeId(SnowflakeIdWorker.getIdToStr());
        signSysUserInfo.setUserName("13798374339");
        signSysUserInfo.setUserNickName("熊二");
        signSysUserInfo.setPassword("$2a$10$mfmi5fWLqf.g41FRBqavIOmzJCdIbs0Pm1UuqGYNa.dIsKcQCmSvu");
        signSysUserInfo.setIsLocked(1);
        signSysUserInfo.setIsForbid(1);

        int count = signSysUserInfoMapper.insert(signSysUserInfo);
        Assert.assertTrue("正确",count ==1);
    }
}
