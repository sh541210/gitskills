package com.iyin.sign.system.service.impl;

import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.exception.ServiceException;
import com.iyin.sign.system.dto.req.Constant;
import com.iyin.sign.system.dto.req.SysApplicationDto;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.SysSignApplicationMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.vo.req.TokenReqVO;
import com.iyin.sign.system.vo.resp.TokenRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
	* @Description token和lic校验
	* @Author: wdf 
    * @CreateDate: 2019/2/22 12:26
	* @UpdateUser: wdf
    * @UpdateDate: 2019/2/22 12:26
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Service
@Slf4j
public class AuthTokenServiceImpl implements IAuthTokenService {

    /**
     * 常用字符串
     */
    public static final String APPID="userAppId";
    public static final String APPSECRET="userAppSceret";
    public static final String USERNAME="userName";

    @Value("${systemName}")
    private String systemName;

    @Autowired
    private RedisService redisService;

    @Resource
    private SysSignApplicationMapper sysSignApplicationMapper;

    @Override
    public IyinResult<TokenRespVO> createToken(TokenReqVO tokenReqVO) {
        String appId = tokenReqVO.getClientId();
        String appSecret = tokenReqVO.getClientSecret();
        //校验参数
        if(StringUtils.isBlank(appId) ||StringUtils.isBlank(appSecret)||StringUtils.isBlank(systemName)){
            throw new BusinessException(ErrorCode.REQUEST_40306);
        }
        //校验应用授权
        SysApplicationDto sysApplicationDto=new SysApplicationDto();
        sysApplicationDto.setUserAppId(appId);
        sysApplicationDto.setUserAppSceret(appSecret);

        SysSignApplication app=sysSignApplicationMapper.getApplicationById(sysApplicationDto);
        if(null==app){
            throw new BusinessException(ErrorCode.REQUEST_40305);
        }
        if (app.getApplicationDelete() == AppEnum.DELETE.getCode()) {
            throw new BusinessException(ErrorCode.REQUEST_10015);
        }
        if (app.getApplicationAbled() == AppEnum.SUSPEND.getCode()) {
            throw new BusinessException(ErrorCode.REQUEST_10014);
        }
        TokenRespVO tokenRespVO;
        //刷新token
        if(Constant.REFRESH_TOKEN.equals(tokenReqVO.getRefreshToken())){
            //清除历史token
            redisService.delToken(app);
            tokenRespVO=redisService.refreshToken(app);
        }else{
            long timen=System.currentTimeMillis();
            //不刷新token 存在获取，不存在创建
            tokenRespVO=redisService.getToken(app);
            log.info("不刷新token total:"+(System.currentTimeMillis()-timen));
        }
        log.info("createToken total end tokenRespVO:{}",tokenRespVO);
        return IyinResult.success(tokenRespVO);
    }

    @Override
    public SysSignApplication verifyAuthToken(String token) {
        if (!redisService.exists(Constant.SIGN_APP_TOKEN_INFO + token)) {
            throw new BusinessException(ErrorCode.REQUEST_40307);
        }
        Map<String, Object> map = redisService.getHashAll(Constant.SIGN_APP_INFO + token);
        if (null != map) {
            if (null == map.get(APPID) || "".equals(map.get(APPID))) {
                throw new BusinessException(ErrorCode.REQUEST_10006);
            }
        } else {
            throw new BusinessException(ErrorCode.REQUEST_10006);
        }

        SysApplicationDto sysApplicationDto=new SysApplicationDto();
        sysApplicationDto.setUserAppId(map.get(APPID)+"");
        SysSignApplication app=sysSignApplicationMapper.getApplicationById(sysApplicationDto);
        if(null==app){
            throw new BusinessException(ErrorCode.REQUEST_40305);
        }
        //校验应用状态
        if (app.getApplicationDelete() == AppEnum.DELETE.getCode()) {
            throw new BusinessException(ErrorCode.REQUEST_10015);
        }
        if (app.getApplicationAbled() == AppEnum.SUSPEND.getCode()) {
            throw new BusinessException(ErrorCode.REQUEST_10014);
        }
        return app;
    }

    @Override
    public SysSignApplication getUserId(String appId, String appSecret) throws ServiceException {
        //校验应用授权
        SysApplicationDto sysApplicationDto=new SysApplicationDto();
        sysApplicationDto.setUserAppId(appId);
        sysApplicationDto.setUserAppSceret(appSecret);

        SysSignApplication app=sysSignApplicationMapper.getApplicationById(sysApplicationDto);
        if(null==app){
            throw new ServiceException(ErrorCode.REQUEST_40305);
        }
        return app;
    }

}
