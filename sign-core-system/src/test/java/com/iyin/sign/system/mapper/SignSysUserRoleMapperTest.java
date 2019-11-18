package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.entity.SignSysUserDataLimit;
import com.iyin.sign.system.entity.SignSysUserRole;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
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
 * @ClassName: SignSysUserRoleMapperTest
 * @Description: SignSysUserRoleMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:22
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:22
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Rollback(value = false)
@Transactional
@Slf4j
public class SignSysUserRoleMapperTest {

    @Autowired
    SignSysRoleMapper signSysRoleMapper;

    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;

    @Autowired
    SignSysUserDataLimitMapper signSysUserDataLimitMapper;


    @Test
    public void addRole(){
        SignSysRole signSysRole = new SignSysRole();
        signSysRole.setId(SnowflakeIdWorker.getIdToStr());
        signSysRole.setRoleName("超级管理员");
        signSysRole.setRoleFlag("ROLE_ADMIN");
        signSysRole.setRoleDesc("超级管理员");
        signSysRole.setGmtCreate(new Date());
        int count = signSysRoleMapper.insert(signSysRole);
        Assert.assertTrue("正确",count ==1);
    }

    @Test
    public void addUserRole(){
        SignSysUserRole signSysUserRole = new SignSysUserRole();
        signSysUserRole.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserRole.setRoleId(SnowflakeIdWorker.getIdToStr());
        signSysUserRole.setUserId(SnowflakeIdWorker.getIdToStr());
        signSysUserRole.setGmtCreate(new Date());
        int count = signSysUserRoleMapper.insert(signSysUserRole);
        Assert.assertTrue("正确",count ==1);
    }

    @Test
    public void addUserDataLimit(){
        SignSysUserDataLimit signSysUserDataLimit = new SignSysUserDataLimit();
        signSysUserDataLimit.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserDataLimit.setUserId("121212121212");
        signSysUserDataLimit.setLimitType("0");
        int count = signSysUserDataLimitMapper.insert(signSysUserDataLimit);
        Assert.assertTrue("正确",count ==1);
    }

}
