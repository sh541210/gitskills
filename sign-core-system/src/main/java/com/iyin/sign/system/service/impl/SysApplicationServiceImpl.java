package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.dto.req.SysApplicationDto;
import com.iyin.sign.system.dto.req.SysApplicationReq;
import com.iyin.sign.system.dto.req.SysSignApplicationDTO;
import com.iyin.sign.system.entity.SysSignApplication;
import com.iyin.sign.system.mapper.SysSignApplicationMapper;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.ISysApplicationService;
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
import java.util.List;
import java.util.UUID;

/**
	* @Description 应用管理service
	* @Author: wdf 
    * @CreateDate: 2019/8/16 10:26
	* @UpdateUser: wdf
    * @UpdateDate: 2019/8/16 10:26
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Service
@Slf4j
public class SysApplicationServiceImpl extends ServiceImpl<SysSignApplicationMapper, SysSignApplication> implements ISysApplicationService {

    @Resource
    private SysSignApplicationMapper sysSignApplicationMapper;
    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public IyinResult<IyinPage<SysSignApplicationDTO>> getApplicationList(Integer currPage, Integer pageSize, String applicationName,HttpServletRequest request) {
        IyinPage<SysSignApplicationDTO> page = new IyinPage<>(currPage, pageSize);
        SysSignApplicationDTO sysSignApplicationDTO=new SysSignApplicationDTO();
        if(StringUtils.isNotBlank(applicationName)){
            sysSignApplicationDTO.setApplicationName(applicationName);
        }
        sysSignApplicationDTO.setUserId(getUser(request));
        List<SysSignApplicationDTO> sysSignApplicationDTOS=sysSignApplicationMapper.getPageList(page,sysSignApplicationDTO);
        page.setRecords(sysSignApplicationDTOS);
        //返回请求结果
        IyinResult<IyinPage<SysSignApplicationDTO>> result = IyinResult.success();
        result.setData(page);
        return result;
    }

    @Override
    public SysSignApplication getApplicationById(SysApplicationDto sysApplicationDto) {
        return sysSignApplicationMapper.getApplicationById(sysApplicationDto);
    }

    @Override
    public int addApplication(SysApplicationReq sysApplicationReq, HttpServletRequest request) {
        SysSignApplication application = new SysSignApplication();
        String appId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        application.setId(String.valueOf(SnowflakeIdWorker.getId()));
        application.setApplicationName(sysApplicationReq.getApplicationName());
        application.setApplicationDesc(sysApplicationReq.getApplicationDesc());
        application.setUserAppId(appId);
        application.setUserId(getUser(request));
        application.setApplicationAbled(0);
        application.setApplicationDelete(0);
        application.setCreateTime(new Date());
        application.setModifyTime(new Date());
        String appSecret = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        application.setUserAppSceret(appSecret);
        return sysSignApplicationMapper.insert(application);
    }

    @Override
    public SysSignApplication addAppBackInfo(SysApplicationReq sysApplicationReq, HttpServletRequest request) {
        SysSignApplication application = new SysSignApplication();
        String appId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        application.setId(String.valueOf(SnowflakeIdWorker.getId()));
        application.setApplicationName(sysApplicationReq.getApplicationName());
        application.setApplicationDesc(sysApplicationReq.getApplicationDesc());
        application.setUserAppId(appId);
        application.setApplicationAbled(0);
        application.setApplicationDelete(0);
        application.setUserId(getUser(request));
        application.setCreateTime(new Date());
        application.setModifyTime(new Date());
        String appSecret = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        application.setUserAppSceret(appSecret);
        int count=sysSignApplicationMapper.insert(application);
        SysApplicationDto sysApplicationDto=new SysApplicationDto();
        SysSignApplication sysSignApplication=null;
        if(count>0){
            sysApplicationDto.setUserAppId(appId);
            sysApplicationDto.setUserAppSceret(appSecret);
            sysSignApplication=sysSignApplicationMapper.getApplicationById(sysApplicationDto);
        }
        return sysSignApplication;
    }

    @Override
    public int updateApplication(SysSignApplication sysSignApplication) {
        return sysSignApplicationMapper.updateApplication(sysSignApplication);
    }

    @Override
    public void deleteApplication(String id) {
        SysSignApplication application = new SysSignApplication();
        application.setApplicationDelete(1);
        application.setId(id);
        application.setModifyTime(new Date());
        sysSignApplicationMapper.deleteApplication(application);
    }

    @Override
    public void abledApp(String id,Integer type) {
        SysSignApplication application = new SysSignApplication();
        if(1==type){
            application.setApplicationAbled(1);
        }
        else{
            application.setApplicationAbled(0);
        }
        application.setId(id);
        application.setModifyTime(new Date());
        sysSignApplicationMapper.deleteApplication(application);
    }

    private String getUser(HttpServletRequest request) {
        log.info("SysApplicationServiceImpl");
        String sessionToken = request.getHeader("session_token");
        log.info("=======sessionToken:{}=========", sessionToken);
        String user = "";
        if (!StringUtils.isEmpty(sessionToken)) {
            Claims claims = jwtUtils.parseJWT(sessionToken);
            user = String.valueOf(claims.get("userId"));
        }
        if(StringUtils.isBlank(user)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1011);
        }
        return user;
    }
}
