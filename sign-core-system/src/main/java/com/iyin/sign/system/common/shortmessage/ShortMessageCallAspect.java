package com.iyin.sign.system.common.shortmessage;

import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.SignSysEnterpriseInfoMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.model.exception.TokenCheckException;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.util.DateFormatType;
import com.iyin.sign.system.util.DateUtil;
import com.iyin.sign.system.util.JWTUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: ShortMessageCallAspect
 * @Description: 短信调用切面
 * @Author: luwenxiong
 * @CreateDate: 2019/9/3 17:43
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/9/3 17:43
 * @Version: 0.0.1
 */
@Aspect
@Component
@Slf4j
public class ShortMessageCallAspect {

    @Autowired
    JWTUtils  jwtUtils;

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";
    private static final String USERID = "userId";


    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    private IAuthTokenService authTokenService;

    @Autowired
    RedisService redisService;

    @Value("${sms.callLimit}")
    private String callCountLimit;

    @Pointcut("@annotation(ShortMessageCall)")
    public void sendMessage(){

    }

    @Before("sendMessage()")
    public void doBefore(JoinPoint joinPoint){

        log.info("=====短信接口调用次数验证=====");
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 请求对象
        HttpServletRequest request = attributes.getRequest();
        String enterpriseId = null;
        try {
            String userId = ckToken(request);
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
            enterpriseId = signSysUserInfo.getEnterpriseOrPersonalId();
        } catch (TokenCheckException e) {
            log.error(e.getLocalizedMessage());
        }

        if(!StringUtils.isEmpty(enterpriseId)){
            String datepre= DateUtil.formatDate(new Date(), DateFormatType.DATE)+"_";
            String callCount = redisService.get(RedisKeyConstant.ENTERPRISE_SEND_SHORT_MESSAGE_PRE+datepre+enterpriseId);
            callCount = callCount ==null?"0":callCount;
            AtomicInteger atomicInteger=new AtomicInteger(Integer.valueOf(callCount));

            atomicInteger.incrementAndGet();
            //判断是否超过了设置的调用次数
            Integer callLimti =  Integer.parseInt(callCountLimit);
            if(atomicInteger.intValue()>callLimti){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1062);
            }
            redisService.set(RedisKeyConstant.ENTERPRISE_SEND_SHORT_MESSAGE_PRE+datepre+enterpriseId, String.valueOf(atomicInteger.intValue()));
        }
    }

    private String ckToken(HttpServletRequest request) {
        String userId;
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (org.apache.commons.lang.StringUtils.isBlank(token) && org.apache.commons.lang.StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(sessionToken)){
            log.info("com.iyin.sign.system.common.shortmessage.ShortMessageCallAspect sessionToken");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get(USERID));
        }else{
            log.info("com.iyin.sign.system.common.shortmessage.ShortMessageCallAspect apiToken");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }
}
