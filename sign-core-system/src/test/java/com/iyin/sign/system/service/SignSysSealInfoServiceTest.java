package com.iyin.sign.system.service;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.GeneratePicReqVO;
import com.iyin.sign.system.vo.resp.SealDefinedUploadRespVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: SignSysSealInfoServiceTest
 * @Description: SignSysSealInfoServiceTest
 * @Author: luwenxiong
 * @CreateDate: 2019/8/30 17:14
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/30 17:14
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@Rollback
public class SignSysSealInfoServiceTest {

    @Autowired
    SignSysSealInfoService signSysSealInfoService;

    @Test
    public void  definedCreateSealTest(){

        GeneratePicReqVO reqVO =new GeneratePicReqVO();
        reqVO.setEnterpriseName("搞死人科技有限公司");
        reqVO.setSealName("科技印章");
        reqVO.setCenterDesign(1);

        IyinResult<SealDefinedUploadRespVO> result = signSysSealInfoService.definedCreateSeal(reqVO);

    }
}
