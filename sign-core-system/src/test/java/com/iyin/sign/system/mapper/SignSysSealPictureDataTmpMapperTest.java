package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysSealPictureDataTmp;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
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
 * @ClassName: SignSysSealPictureDataTmpMapperTest
 * @Description: SignSysSealPictureDataTmpMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 12:16
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 12:16
 * @Version: 0.0.1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
public class SignSysSealPictureDataTmpMapperTest {

    @Autowired
    SignSysSealPictureDataTmpMapper signSysSealPictureDataTmpMapper;

    @Test
    public void testInsert(){

        SignSysSealPictureDataTmp tmp = new SignSysSealPictureDataTmp();
        tmp.setId(SnowflakeIdWorker.getIdToStr());
        tmp.setGmtCreate(new Date());
        tmp.setPictureData("dfdfsdfsdfsdfsdf");
        tmp.setPictureData64("dfdfsdfsdfsdfsdf");
        signSysSealPictureDataTmpMapper.insert(tmp);
    }
}
