package com.iyin.sign.system.service;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuTree;
import com.iyin.sign.system.vo.req.DelUserReqVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: SignSysUserInfoServiceTest
 * @Description: SignSysUserInfoServiceTest
 * @Author: luwenxiong
 * @CreateDate: 2019/8/22 10:25
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/22 10:25
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
@Slf4j
public class SignSysUserInfoServiceTest {

    @Autowired
    SignSysUserInfoService signSysUserInfoService;

    @Test
    public void getUserMenuTree(){
        String userId ="613705767622541312";
        IyinResult<List<MenuTree>> result = signSysUserInfoService.getUserMenuTree(userId);
        log.info("测试返回结果:{}",result);
    }

    @Test
    public void delUser(){
        DelUserReqVO reqVO=new DelUserReqVO();
        reqVO.setUserId("631796550237945856");
        IyinResult<Boolean> result = signSysUserInfoService.delUser(reqVO);
        log.info("测试返回结果:{}",result);
    }
}
