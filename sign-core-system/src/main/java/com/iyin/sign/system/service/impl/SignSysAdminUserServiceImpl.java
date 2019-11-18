package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysAdminUser;
import com.iyin.sign.system.entity.SignSysDefaultConfig;
import com.iyin.sign.system.entity.SignSysEnterpriseInfo;
import com.iyin.sign.system.mapper.SignSysAdminUserMapper;
import com.iyin.sign.system.mapper.SignSysDefaultConfigMapper;
import com.iyin.sign.system.mapper.SignSysEnterpriseInfoMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.RedisService;
import com.iyin.sign.system.service.SignSysAdminUserService;
import com.iyin.sign.system.service.VerifyLicense;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.TokenSettings;
import com.iyin.sign.system.vo.req.AdminUserLoginReqVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserBaseInfoRespVO;
import com.iyin.sign.system.vo.resp.SignSysAdminUserLoginRespVO;
import com.iyin.sign.system.vo.resp.SignSysDefaultConfigInfoResp;
import com.iyin.sign.system.vo.resp.UserLicenceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SignSysAdminUserServiceImpl
 * @Description:
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:32
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:32
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class SignSysAdminUserServiceImpl extends ServiceImpl<SignSysAdminUserMapper,SignSysAdminUser> implements SignSysAdminUserService {

    @Autowired
    SignSysAdminUserMapper signSysAdminUserMapper;

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    SignSysDefaultConfigMapper signSysDefaultConfigMapper;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    RedisService redisService;

    @Autowired
    TokenSettings settings;

    @Autowired
    private VerifyLicense verifyLicense;

    @Value("${localFilePath.auth}")
    public String authUrl;

    /**
     * 常用字符串
     */
    private static final String VERSIONINFO="versionInfo";
    private static final String SERIALNUMBER="serialNumber";
    private static final String ISEFFECTIVE="isEffective";

    @Override
    public IyinResult<SignSysAdminUserLoginRespVO> loginByUserName(AdminUserLoginReqVO reqVO, HttpServletRequest request) {

        Map<String, Object> extra=verifyLicense.ckLicence(authUrl+ VerifyLicense.LIC_NAME);
        log.info("loginByUserNameAndPwd extra："+extra.toString());

        String userName = reqVO.getUserName();
        String password = reqVO.getPassword();
        String valiCode = reqVO.getValicode();
        String sessionId = request.getSession().getId();

        String captcha = RedisKeyConstant.USER_LOGIN_VALICODE_KEY_PRE+sessionId;
        String cacheCaptcha = redisService.get(captcha);
        if(cacheCaptcha == null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1053);
        }

        if(!cacheCaptcha.equals(valiCode)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1054);
        }

        QueryWrapper<SignSysAdminUser> qw = new QueryWrapper<SignSysAdminUser>()
                .eq("user_name", userName)
                .eq("is_deleted", 0);
        SignSysAdminUser signSysAdminUser = signSysAdminUserMapper.selectOne(qw);
        if(signSysAdminUser ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1001);
        }
        if(!BCrypt.checkpw(password,signSysAdminUser.getPassword())){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1002);
        }

        //删除验证码缓存key
        redisService.delete(captcha);

        //创建session_token
        String sessionToken = jwtUtils.createSessionToken(signSysAdminUser.getId());
        redisService.set(RedisKeyConstant.ADMIN_USER_SESSION_TOKEN_PRE+signSysAdminUser.getId(),sessionToken,settings.getTokenExpirationTime());
        SignSysAdminUserLoginRespVO signSysAdminUserLoginRespVO = new SignSysAdminUserLoginRespVO();
        BeanUtils.copyProperties(signSysAdminUser,signSysAdminUserLoginRespVO);
        signSysAdminUserLoginRespVO.setSessionToken(sessionToken);

        String powerLevel = signSysAdminUser.getPowerLevel();
        String roleName = "超级管理员";
        if(!StringUtils.isEmpty(powerLevel) && "01".equals(powerLevel)){
            roleName="单位管理员";
        }else {
            roleName = "超级管理员";
            powerLevel="01";
        }
        signSysAdminUserLoginRespVO.setRole(roleName);
        signSysAdminUserLoginRespVO.setPowerLevel(powerLevel);

        //所属单位信息
        SignSysEnterpriseInfo belongEnterprise =null;
        String enterpriseId = signSysAdminUser.getEnterpriseId();
        if(!StringUtils.isEmpty(enterpriseId)){
            belongEnterprise = signSysEnterpriseInfoMapper.selectById(enterpriseId);
        }else {
           belongEnterprise = signSysEnterpriseInfoMapper.selectAdminBelongEnterprise();
        }
        if(belongEnterprise!=null){
            signSysAdminUserLoginRespVO.setEnterpriseId(belongEnterprise.getId());
            signSysAdminUserLoginRespVO.setEnterpriseName(belongEnterprise.getChineseName());
        }


        //前台logourl
        SignSysDefaultConfigInfoResp configInfoResp = new SignSysDefaultConfigInfoResp();
        SignSysDefaultConfig signSysDefaultConfig =  signSysDefaultConfigMapper.selectLastConfig();
        if(signSysDefaultConfig!=null){
            BeanUtils.copyProperties(signSysDefaultConfig,configInfoResp);
            signSysAdminUserLoginRespVO.setConfigInfoResp(configInfoResp);
        }
        //licence info
        UserLicenceInfo userLicenceInfo=new UserLicenceInfo();
        userLicenceInfo.setVersionInfo((String)extra.get(VERSIONINFO));
        userLicenceInfo.setSerialNumber((String)extra.get(SERIALNUMBER));
        userLicenceInfo.setIsEffective((Integer) extra.get(ISEFFECTIVE));
        userLicenceInfo.setUserName((String)extra.get("userName"));
        userLicenceInfo.setDateTime((String)extra.get("dateTime"));
        userLicenceInfo.setSysType((String)extra.get("sysType"));
        userLicenceInfo.setUserEn((String)extra.get("userEn"));
        userLicenceInfo.setUserMac((String)extra.get("userMac"));
        signSysAdminUserLoginRespVO.setUserLicenceInfo(userLicenceInfo);
        return IyinResult.getIyinResult(signSysAdminUserLoginRespVO);
    }

    @Override
    public IyinResult<Boolean> loginOut(String id) {
        String sessionToken = redisService.get(RedisKeyConstant.ADMIN_USER_SESSION_TOKEN_PRE+id);
        if(sessionToken!=null){
            redisService.delete(RedisKeyConstant.ADMIN_USER_SESSION_TOKEN_PRE+id);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<SignSysAdminUserLoginRespVO> queryAccountInfoById(String id) {
        SignSysAdminUser signSysAdminUser = signSysAdminUserMapper.selectById(id);
        if(signSysAdminUser ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1001);
        }
        //判断是单位管理员还是超级管理员
        String powerLevel = signSysAdminUser.getPowerLevel();
        String role="超级管理员";
        if("01".equals(powerLevel)){
            role ="单位管理员";
        }
        SignSysAdminUserLoginRespVO signSysAdminUserLoginRespVO = new SignSysAdminUserLoginRespVO();
        BeanUtils.copyProperties(signSysAdminUser,signSysAdminUserLoginRespVO);
        signSysAdminUserLoginRespVO.setRole(role);
        return IyinResult.getIyinResult(signSysAdminUserLoginRespVO);
    }

    @Override
    public IyinResult<List<SignSysAdminUserBaseInfoRespVO>> queryListAdminUser() {
        QueryWrapper<SignSysAdminUser> queryWrapper = new QueryWrapper<SignSysAdminUser>()
                .eq("is_deleted",0);
        List<SignSysAdminUser> signSysAdminUserList = signSysAdminUserMapper.selectList(queryWrapper);

        List<SignSysAdminUserBaseInfoRespVO> respVOS = new ArrayList<SignSysAdminUserBaseInfoRespVO>();
        for(SignSysAdminUser signSysAdminUser:signSysAdminUserList){
            SignSysAdminUserBaseInfoRespVO respVO = new SignSysAdminUserBaseInfoRespVO();
            respVO.setUserId(signSysAdminUser.getId());
            respVO.setUserName(signSysAdminUser.getUserName());
            respVO.setPhone(signSysAdminUser.getRemark());
            respVO.setCreateTime(signSysAdminUser.getCreateDate());
            respVOS.add(respVO);
        }
        return IyinResult.getIyinResult(respVOS);
    }

    @Override
    public Boolean checkSuperAdmin(String userId) {
        QueryWrapper<SignSysAdminUser> queryWrapper = new QueryWrapper<SignSysAdminUser>()
                .eq("is_deleted",0)
                .eq("id",userId);
        SignSysAdminUser signSysAdminUser = signSysAdminUserMapper.selectOne(queryWrapper);

        if(signSysAdminUser!=null && ("01").equals(signSysAdminUser.getPowerLevel())){
            return false;
        }
        return true;
    }
}
