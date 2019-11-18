package com.iyin.sign.system.service;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.utils.BeanAdaptUtil;
import com.iyin.sign.system.common.utils.TokenUtil;
import com.iyin.sign.system.dto.req.Constant;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.message.MessageInfo;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.vo.req.CreateEnterpriseStepOneReqVO;
import com.iyin.sign.system.vo.resp.TokenRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisService
 * @Description:  redis操作
 * @Author: luwenxiong
 * @CreateDate: 2018/8/18 17:40
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/8/18 17:40
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class RedisService {

    public static final String BEARER_TYPE = "Bearer";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 一天有多少分钟，默认时间是一天
     */
    private static final long MINUTES_OF_ONE_DAY = 24 * 60L;


    /**
     * 将 key，value 存放到redis数据库中，默认设置过期时间为一天
     *
     * @param key
     * @param value
     */
    public<T> void set(String key, T value) {
        set(key, value, MINUTES_OF_ONE_DAY);
    }

    public<T> void setEn(String key, CreateEnterpriseStepOneReqVO value) {
        set(key, JSON.toJSONString(value), MINUTES_OF_ONE_DAY);
    }

    /**
     * 将 key，value 存放到redis数据库中，设置过期时间单位是分钟
     *
     * @param key
     * @param value
     * @param expireTime 单位是分钟
     */
    public<T> void set(String key, T value, long expireTime) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 将 key，value 存放到redis数据库中，不设置过期时间
     *
     * @param key
     * @param value
     */
    public<T> void setDefault(String key, T value) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * 判断 key 是否在 redis 数据库中
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * 获取 key 对应的字符串
     *
     * @param key
     * @return
     */
    public<T> T get(String key) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 获取 key 对应的字符串
     *
     * @param key
     * @return
     */
    public CreateEnterpriseStepOneReqVO getEn(String key) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        String json=String.valueOf(valueOperations.get(key));
        CreateEnterpriseStepOneReqVO en=JSON.parseObject(json,CreateEnterpriseStepOneReqVO.class);
        return en;
    }

    /**
     * 获得 key 对应的键值，并更新缓存时间，时间长度为默认值
     *
     * @param key
     * @return
     */
    public<T> T getAndUpdateTime(String key) {
        T result = get(key);
        if (result != null) {
            set(key, result);
        }
        return result;
    }

    /**
     * 获得 key 对应的键值，并更新缓存时间
     *
     * @param key
     * @return
     */
    public<T> T getAndUpdateTime(String key, long expireTime) {
        T result = get(key);
        if (result != null) {
            set(key, result, expireTime);
        }
        return result;
    }

    /**
     * 删除 key 对应的 value
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * @Description Hash写入数据 带过期时间 毫秒
     * @Author: wdf
     * @CreateDate: 2018/12/24 15:43
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/24 15:43
     * @Version: 0.0.1
     * @param key, param, expireTime
     * @return void
     */
    public Boolean putHashAllExpire(String key, Map<String,Object> param, long expireTime){
        HashOperations<String,Object,Object> opsForHash=redisTemplate.opsForHash();
        opsForHash.putAll(key,param);
        return redisTemplate.expire(key,expireTime,TimeUnit.MINUTES);
    }

    /**
     * @Description 获取redis hashall
     * @Author: wdf
     * @CreateDate: 2018/12/24 11:08
     * @UpdateUser: wdf
     * @UpdateDate: 2018/12/24 11:08
     * @Version: 0.0.1
     * @param key
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String,Object> getHashAll(String key){
        HashOperations<String,String,Object> opsForHash=redisTemplate.opsForHash();
        return opsForHash.entries(key);
    }

    /**
     * @Description redis中删除token
     * @Author: wdf
     * @CreateDate: 2019/1/24 16:13
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/24 16:13
     * @Version: 0.0.1
     * @param app
     * @return void
     */
    public void delToken(SysSignApplication app){
        String token=get(Constant.SIGN_APP_TOKEN+app.getUserAppId());
        if(StringUtils.isNotBlank(token)){
            delete(Constant.SIGN_APP_TOKEN_INFO +token);
            delete(Constant.SIGN_APP_INFO +token);
            delete(Constant.SIGN_APP_TOKEN+app.getUserAppId());
        }
    }

    /**
     * @Description redis中创建token
     * @Author: wdf
     * @CreateDate: 2019/1/24 16:12
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/24 16:12
     * @Version: 0.0.1
     * @param app
     * @return com.iyin.sign.system.vo.resp.TokenRespVO
     */
    public TokenRespVO newToken(SysSignApplication app){
        long time=System.currentTimeMillis();
        TokenRespVO tokenRespVO = new TokenRespVO();
        tokenRespVO.setExpires_in(Constant.ACCESS_TOKEN_VALIDITY_SECONDS + "");
        tokenRespVO.setToken_type(BEARER_TYPE);
        try {
            long time2=System.currentTimeMillis();
            tokenRespVO.setAccess_token(TokenUtil.getOAuthToken(app.getUserAppSceret(),app.getUserAppId()));
            log.info("getOAuthToken1 total:"+(System.currentTimeMillis()-time2));
            tokenRespVO.setRefresh_token(TokenUtil.getOAuthToken(app.getUserAppSceret(),app.getUserAppId()));
            log.info("getOAuthToken2 total:"+(System.currentTimeMillis()-time2));
        } catch (UnsupportedEncodingException e){
            log.error("createToken UnsupportedEncodingException");
        }
        set(Constant.SIGN_APP_TOKEN+app.getUserAppId(),tokenRespVO.getAccess_token(),Constant.ACCESS_TOKEN_VALIDITY_SECONDS/1000/60);
        Map<String, Object> tokenMap = BeanAdaptUtil.obj2Map2(tokenRespVO);
        tokenMap.put("expires",System.currentTimeMillis()+"");
        Boolean flag = putHashAllExpire(Constant.SIGN_APP_TOKEN_INFO + tokenRespVO.getAccess_token(), tokenMap, Constant.ACCESS_TOKEN_VALIDITY_SECONDS/1000/60);
        if (!flag) {
            throw new BusinessException(ErrorCode.REQUEST_10004);
        }

        Map<String, Object> appInfoMap = BeanAdaptUtil.obj2Map3(app);
        flag = putHashAllExpire(Constant.SIGN_APP_INFO + tokenRespVO.getAccess_token(), appInfoMap, Constant.ACCESS_TOKEN_VALIDITY_SECONDS/1000/60);
        appInfoMap.put("expires",(System.currentTimeMillis() / 1000 / 60)+"");
        Boolean refreshflag = putHashAllExpire(Constant.SIGN_REFRESH_APP_INFO + tokenRespVO.getRefresh_token(), appInfoMap, Constant.REFRESH_TOKEN_VALIDITY_SECONDS*1L);
        if (!flag && !refreshflag) {
            throw new BusinessException(ErrorCode.REQUEST_10004);
        }
        log.info("newToken total:"+(System.currentTimeMillis()-time));
        return tokenRespVO;
    }

    /**
     * @Description redis中创建refreshToken
     * @Version: 0.0.1
     * @param app
     * @return com.iyin.sign.system.vo.resp.TokenRespVO
     */
    public TokenRespVO refreshToken(SysSignApplication app){
        return newToken(app);
    }

    /***
     * @Description 获取已经存在的token
     * @Author: wdf
     * @CreateDate: 2019/1/24 16:12
     * @UpdateUser: wdf
     * @UpdateDate: 2019/1/24 16:12
     * @Version: 0.0.1
     * @param app
     * @return com.iyin.sign.system.vo.resp.TokenRespVO
     */
    public TokenRespVO getToken(SysSignApplication app){
        long time=System.currentTimeMillis();
        TokenRespVO tokenRespVO;
        String token=get(Constant.SIGN_APP_TOKEN+app.getUserAppId());
        if(StringUtils.isBlank(token)){
            delToken(app);
            tokenRespVO = newToken(app);
        }else{
            Map<String,Object> map=getHashAll(Constant.SIGN_APP_TOKEN_INFO + token);
            if(null==map||map.size()==0){
                tokenRespVO = newToken(app);
            }else {
                tokenRespVO = (TokenRespVO) BeanAdaptUtil.map2Obj2(map, TokenRespVO.class);
                if(StringUtils.isBlank(tokenRespVO.getAccess_token())){
                    delToken(app);
                    tokenRespVO = newToken(app);
                }
                long expires = 0L;
                if(null!=map.get("expires")){
                    expires = Long.parseLong(map.get("expires").toString());
                    tokenRespVO.setExpires_in(String.valueOf(Constant.ACCESS_TOKEN_VALIDITY_SECONDS-Integer.parseInt(String.valueOf(System.currentTimeMillis()-expires))));
                }else {
                    tokenRespVO = newToken(app);
                }
                log.info("map Token total:" + (System.currentTimeMillis() - time));
            }
        }
        log.info("getToken total:"+(System.currentTimeMillis()-time));
        return tokenRespVO;
    }

    /**
     * redis作为消息中间件发布消息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/26
     * @UpdateUser:
     * @UpdateDate:  2019/8/26
     * @Version:     0.0.1
     * @return
     * @throws
     */
    public void sendMessage(MessageInfo messageInfo){
        String msg = JSON.toJSONString(messageInfo);
        log.info("发送消息 : {}", msg);
        String channel = messageInfo.getChannel();
        if(!StringUtils.isEmpty(channel)){
            redisTemplate.convertAndSend(channel,msg);
        }
    }
}
