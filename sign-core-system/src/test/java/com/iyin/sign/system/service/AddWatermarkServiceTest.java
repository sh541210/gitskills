package com.iyin.sign.system.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.feign.AddWatermarkService;
import com.iyin.sign.system.vo.req.AddWatermarkReqVO;
import com.iyin.sign.system.vo.resp.AddWatermarkRespVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: AddWatermarkServiceTest
 * @Description: AddWatermarkServiceTest
 * @Author: luwenxiong
 * @CreateDate: 2019/9/2 17:24
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/2 17:24
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AddWatermarkServiceTest {

    @Autowired
    AddWatermarkService addWatermarkService;

    @Test
    public void testAddWatermark(){

        AddWatermarkReqVO addWatermarkReqVO = new AddWatermarkReqVO();
        addWatermarkReqVO.setBussinessID("01");
        addWatermarkReqVO.setReqType("addWatermark");
        addWatermarkReqVO.setSrcFileID("group2/M00/0C/2B/wKgz5l1mGoGATZ4qAAE4lQS93Qs954.png");
        addWatermarkReqVO.setText("hello");
        addWatermarkReqVO.setAlgFlag("1");
        String addWatermarkRespVOIyinResult = addWatermarkService.testFench(addWatermarkReqVO);
        AddWatermarkRespVO respVO =  JSON.parseObject(addWatermarkRespVOIyinResult,AddWatermarkRespVO.class);
        log.info("res:{}",respVO.toString());
    }
}
