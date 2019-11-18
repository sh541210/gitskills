package com.iyin.sign.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: SignSysEnterpriseManagerControllerTest
 * @Description: 初始化单位、账号、印章、授权信息
 * @Author: luwenxiong
 * @CreateDate: 2019/6/27 9:55
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/27 9:55
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@Rollback(value = false)
@Transactional
public class SignSysEnterpriseManagerControllerTest {


    
    /*
     * 初始化签章系统迭代1的数据
     * @Author:      luwenxiong
     * @CreateDate:  2019/6/27 
     * @UpdateUser:  
     * @UpdateDate:  2019/6/27 
     * @Version:     0.0.1
     * @return       void
     * @throws       
     */
    @Test
    public void initSignSysData(){

        /**
         * 1、初始化单位
         */

        /**
         * 2、初始化话单位下的用户
         */

        /**
         * 3、初始化单位下的印章
         */


        /**
         * 4、初始化用户的印章数据
         */



    }
}
