package com.iyin.sign.system.service;

import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: SignSysUserDataLimitServiceTest
 * @Description: SignSysUserDataLimitServiceTest
 * @Author: luwenxiong
 * @CreateDate: 2019/8/21 14:42
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/21 14:42
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
@Slf4j
public class SignSysUserDataLimitServiceTest {

    @Autowired
    SignSysUserDataLimitService signSysUserDataLimitService;

    @Test
    public void checkResouceDataLimit(){
        String ownerId ="613744886390194176"; //数据权限 1 自己
        String userId ="613745071686156288";//数据权限 2 本级及子级
        Boolean res = signSysUserDataLimitService.checkResouceDataLimit(userId,ownerId);
        log.info("res:{}",res);
        Assert.assertTrue("正确",res);
    }

    @Test
    public void getPowerScopeUserIds(){
        String userId ="613745071686156288";//数据权限 2 本级及子级
        List<String> ids = signSysUserDataLimitService.getPowerScopeUserIds(userId);
        log.info("ids:{}",ids);
    }
}
