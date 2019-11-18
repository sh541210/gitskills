package com.iyin.sign.system.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysNodeInfoService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.SaveNodeReqVO;
import com.iyin.sign.system.vo.resp.NodeTreeRespVO;
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

import javax.validation.constraints.AssertTrue;

/**
 * @ClassName: SignSysEnterpriseInfoMapperTest
 * @Description: SignSysEnterpriseInfoMapperTest
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 14:22
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 14:22
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Rollback(value = false)
@Transactional
@Slf4j
@AutoConfigureMockMvc
public class SignSysEnterpriseInfoMapperTest {

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    SignSysNodeInfoService signSysNodeInfoService;

    @Test
    public void insert(){
        SignSysEnterpriseInfo signSysEnterpriseInfo = new SignSysEnterpriseInfo();
        signSysEnterpriseInfo.setId(SnowflakeIdWorker.getIdToStr());
        signSysEnterpriseInfo.setChineseName("深圳市安印科技有限公司");
        signSysEnterpriseInfo.setCreditCode("421083198805414266");
        signSysEnterpriseInfo.setLegalName("杨瑞芳");

        int count = signSysEnterpriseInfoMapper.insert(signSysEnterpriseInfo);
        Assert.assertTrue("正确",count ==1);
    }

    @Test
    public void getTree(){
        IyinResult<NodeTreeRespVO> nodeTreeRespVO = signSysNodeInfoService.getNodeTree("593775198575001600");
        String jsonStr = JSON.toJSONString(nodeTreeRespVO.getData());
        log.info("nodeTreeRespVO="+jsonStr);
    }

    @Test
    public void delNode(){
        String delNode ="600643951271084046";
        IyinResult<Boolean> res = signSysNodeInfoService.delNode(delNode);
        Assert.assertTrue("正确",res.getData());
    }

    @Test
    public void addNode(){
        SaveNodeReqVO saveNodeReqVO = new SaveNodeReqVO();
        saveNodeReqVO.setParentNodeId("600643951271084047");
        saveNodeReqVO.setNodeName("创业-1-1-1");
        IyinResult<String> res = signSysNodeInfoService.addNode(saveNodeReqVO);
        Assert.assertTrue("正确",res.getData()!=null);
        log.info("节点ID:{}",res.getData());
    }
}
