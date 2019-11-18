package com.iyin.sign.system;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @ClassName: UserRestTest
 * @Description: 测试类
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:14
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:14
 * @Version: 0.0.1
 */
@Slf4j
public class UserRestTest {

    @Test
    public void enPassword(){
        String password = BCrypt.hashpw("OlBbQs96JGlh5B5UDs7x", BCrypt.gensalt());
        log.info("password={}",password);
    }
}
