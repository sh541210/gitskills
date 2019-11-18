package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysSealInfo;
import com.iyin.sign.system.service.impl.SignSysSealInfoServiceImpl;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.SaveUkSealReqVO;
import com.iyin.sign.system.vo.resp.SealInfoRespVO;
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
 * @ClassName: SignSysSealInfoMapperTest
 * @Description: 电子印章测试
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 14:29
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 14:29
 * @Version: 0.0.1
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
@AutoConfigureMockMvc
public class SignSysSealInfoMapperTest {

    @Autowired
    SignSysSealInfoMapper signSysSealInfoMapper;

    @Autowired
    SignSysSealInfoServiceImpl signSysSealInfoService;


    @Test
    public void testInsert(){

//        SignSysSealInfo signSysSealInfo = new SignSysSealInfo();
//        signSysSealInfo.setId(SnowflakeIdWorker.getIdToStr());
//        signSysSealInfo.setGmtModified(new Date());
//        signSysSealInfo.setIsDeleted(0);
//        signSysSealInfo.setPictureCode("45856");
//        signSysSealInfo.setPictureData("7794564fdsfds");
//        signSysSealInfo.setPictureData64("dfdfs");
//        signSysSealInfo.setPictureUserType("01");
//        signSysSealInfo.setEnterpriseOrPersonalId("10000");
//        signSysSealInfo.setCertificateSource("01");
//        signSysSealInfo.setPictureName("测试章莫");
//        signSysSealInfo.setPictureWidth("40");
//        signSysSealInfo.setPictureHeight("40");
//        int count = signSysSealInfoMapper.insert(signSysSealInfo);
//        Assert.assertTrue("正确",count==1);

//        SaveUkSealReqVO reqVO =new SaveUkSealReqVO();
//        reqVO.setPictureUserType("01");
//        reqVO.setEnterpriseOrPersonalId("11111111");
//        reqVO.setSealCode("222222");
//        reqVO.setPictureName("炉温微信");
//        reqVO.setPictureBusinessType("02");
//        reqVO.setOid("aaaaaaa");
//        reqVO.setTrustId("bbbbbbb");
//        reqVO.setIssuer("gdCA");
//        reqVO.setValidEnd(new Date());
//        reqVO.setValidEnd(new Date());
//        signSysSealInfoService.saveUkSealInfo(reqVO);

        String pictureCode ="44030110314146";
        SealInfoRespVO sealInfoRespVO = signSysSealInfoService.querySealInfo(pictureCode).getData();
        log.info("sealInfoRespVO={}",sealInfoRespVO);

    }

}
