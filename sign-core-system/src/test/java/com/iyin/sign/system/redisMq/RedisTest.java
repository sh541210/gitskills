package com.iyin.sign.system.redisMq;

import com.iyin.sign.system.common.utils.BeanAdaptUtil;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.vo.req.SignSysContractPrintAddReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @ClassName: RedisMessageTest
 * @Description: redis消息中间件
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:51
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:51
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;


    @Test
    public void test(){
//        redisService.set("testInteger",new Integer(100));
//        System.out.println(redisService.get("testInteger")+"");


        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("testInteger",new Integer(200));
        valueOperations.set("testInteger",400);
        System.out.println(valueOperations.get("testInteger"));
        redisTemplate.delete("testInteger");

        SignSysContractPrintAddReqVO signSysContractPrintAddReqVO=new SignSysContractPrintAddReqVO();
        signSysContractPrintAddReqVO.setContractId("123");
        signSysContractPrintAddReqVO.setIsFoggy(1);
        signSysContractPrintAddReqVO.setIsGrey(1);
        signSysContractPrintAddReqVO.setPrintNum(100L);
        signSysContractPrintAddReqVO.setUserIds("123,13");
        valueOperations.set("signSysContractPrintAddReqVO",signSysContractPrintAddReqVO);
        System.out.println(valueOperations.get(signSysContractPrintAddReqVO));

        Map<String, Object> tokenMap = BeanAdaptUtil.obj2Map2(signSysContractPrintAddReqVO);
        HashOperations<String,Object,Object> opsForHash=redisTemplate.opsForHash();
        opsForHash.putAll("tokenMap",tokenMap);
        System.out.println(opsForHash.entries("tokenMap"));
        redisTemplate.delete("tokenMap");


    }
}
