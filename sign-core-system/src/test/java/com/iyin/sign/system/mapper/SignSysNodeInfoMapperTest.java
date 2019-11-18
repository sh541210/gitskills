package com.iyin.sign.system.mapper;

import com.iyin.sign.system.entity.SignSysNodeInfo;
import com.iyin.sign.system.service.impl.SignSysNodeInfoServiceImpl;
import com.iyin.sign.system.util.SnowflakeIdWorker;
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
 * @ClassName: SignSysNodeInfoMapperTest
 * @Description: SignSysNodeInfoMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 12:08
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 12:08
 * @Version: 0.0.1
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
public class SignSysNodeInfoMapperTest {

    @Autowired
    SignSysNodeInfoMapper signSysNodeInfoMapper;

    @Autowired
    SignSysNodeInfoServiceImpl signSysNodeInfoService;

    @Test
    public void queryById(){
        SignSysNodeInfo signSysNodeInfo =  signSysNodeInfoMapper.selectById("591606514456723458");
        Assert.assertTrue("正确", signSysNodeInfo==null);
        log.info("signSysNodeInfo={}",signSysNodeInfo);
    }

    @Test
    public void insert(){
        SignSysNodeInfo signSysNodeInfo =new SignSysNodeInfo();
        signSysNodeInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysNodeInfo.setParentNodeId("591606514456723458");
        signSysNodeInfo.setNodeName("行政部");
        signSysNodeInfo.setGmtCreate(new Date());
        int count = signSysNodeInfoMapper.insert(signSysNodeInfo);
        Assert.assertTrue("正确",count ==1);

    }

    @Test
    public void facadel(){
        String nodeId ="608982134501146624";
        //根节点ID
        String rootNodeId = signSysNodeInfoService.getRootNodeId(nodeId);
        signSysNodeInfoService.delNode(nodeId);
    }
}
