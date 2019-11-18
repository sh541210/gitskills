package com.iyin.sign.system.service;

import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.CreateEnterpriseStepOneReqVO;
import com.iyin.sign.system.vo.req.CreateEnterpriseStepTwoReqVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: SignSysAdminUserServiceTest
 * @Description: SignSysAdminUserServiceTest
 * @Author: luwenxiong
 * @CreateDate: 2019/8/21 18:10
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/21 18:10
 * @Version: 0.0.1
 */
@SpringBootTest
@Rollback(value = false)
@Transactional
@RunWith(SpringRunner.class)
@Slf4j
public class SignSysAdminUserServiceTest {

    @Autowired
    SignSysAdminUserService signSysAdminUserService;

    @Autowired
    SignSysEnterpriseInfoService signSysEnterpriseInfoService;


    @Test
    public void createEnterprise(){

        CreateEnterpriseStepOneReqVO createEnterpriseStepOneReqVO = new CreateEnterpriseStepOneReqVO();
        createEnterpriseStepOneReqVO.setChineseName("最NB科技有限公司");
        createEnterpriseStepOneReqVO.setCreditCode("075507550755075555");
        createEnterpriseStepOneReqVO.setLegalName("陆文雄");
        IyinResult<String> oneRes = signSysEnterpriseInfoService.createEnterpriseStepOne(createEnterpriseStepOneReqVO);
        String enterpriseId = oneRes.getData();

        CreateEnterpriseStepTwoReqVO createEnterpriseStepTwoReqVO = new CreateEnterpriseStepTwoReqVO();
        createEnterpriseStepTwoReqVO.setEnterpriseId(enterpriseId);
        createEnterpriseStepTwoReqVO.setPassword("a12345");
        createEnterpriseStepTwoReqVO.setUserName("16798374338");
        createEnterpriseStepTwoReqVO.setUserNameType("01");
        IyinResult<Boolean> twoRes = signSysEnterpriseInfoService.createEnterpriseStepTwo(createEnterpriseStepTwoReqVO);
        Boolean res = twoRes.getData();
        log.info("创建结果返回:{}",res);


    }


}
