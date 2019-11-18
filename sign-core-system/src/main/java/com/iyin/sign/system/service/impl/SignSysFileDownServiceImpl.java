package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.AppEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.entity.SignSysFileDown;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.mapper.SignSysFileDownMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.InMemoryMultipartFile;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.service.IAuthTokenService;
import com.iyin.sign.system.service.IFileService;
import com.iyin.sign.system.service.ISignSysFileDownService;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 文件下载记录表 服务实现类
 * </p>
 *
 * @author wdf
 * @since 2019-08-08
 */
@Service
@Slf4j
public class SignSysFileDownServiceImpl extends ServiceImpl<SignSysFileDownMapper, SignSysFileDown> implements ISignSysFileDownService {

    private static final String API_TOKEN = "api_token";
    private static final String SESSION_TOKEN = "session_token";

    @Resource
    private SignSysFileDownMapper signSysFileDownMapper;
    private final JWTUtils jwtUtils;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    private final IFileService fileService;
    private final FileResourceService fileResourceService;
    @Resource
    private FileResourceMapper fileResourceMapper;
    private final IAuthTokenService authTokenService;

    @Autowired
    public SignSysFileDownServiceImpl(JWTUtils jwtUtils, IFileService fileService, FileResourceService fileResourceService, IAuthTokenService authTokenService) {
        this.jwtUtils = jwtUtils;
        this.fileService = fileService;
        this.fileResourceService = fileResourceService;
        this.authTokenService = authTokenService;
    }

    @Override
    public InMemoryMultipartFile addFileDown(String fileCode, HttpServletRequest request) {
        String id=getUser(request);
        SignSysFileDown signSysFileDown=new SignSysFileDown();
        signSysFileDown.setId(String.valueOf(SnowflakeIdWorker.getId()));
        signSysFileDown.setFileCode(fileCode);
        SignSysUserInfo signSysUserInfo=signSysUserInfoMapper.selectById(id);
        signSysFileDown.setDownUser(id);
        signSysFileDown.setGmtCreate(new Date());
        signSysFileDown.setUserChannel(AppEnum.USERAPI.getCode());
        signSysFileDown.setUserType("01".equals(signSysUserInfo.getUserType())?1:2);
        signSysFileDown.setUserEnterprise(signSysUserInfo.getEnterpriseOrPersonalId());
        InMemoryMultipartFile in=fileService.fetch(fileCode);
        if(null!=in){
            FileResource res = fileResourceService.findByFileCode(fileCode);
            FileResource resource = new FileResource();
            resource.setFileCode(fileCode);
            resource.setDownCount(res.getDownCount() + 1);
            fileResourceMapper.updateFile(resource);
            signSysFileDownMapper.insert(signSysFileDown);
        }
        return in;
    }

    private String getUser(HttpServletRequest request) {
        log.info("SignSysFileDownServiceImpl getUser");
        String userId="";
        String token = request.getHeader(API_TOKEN);
        String sessionToken = request.getHeader(SESSION_TOKEN);
        if (StringUtils.isBlank(token) && StringUtils.isBlank(sessionToken)) {
            throw new BusinessException(ErrorCode.REQUEST_40434);
        }
        if(StringUtils.isNotBlank(sessionToken)){
            log.info("SignSysFileDownServiceImpl ck sessionToken");
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(token);
            userId = sysSignApplication.getUserId();
        }
        return userId;
    }
}
