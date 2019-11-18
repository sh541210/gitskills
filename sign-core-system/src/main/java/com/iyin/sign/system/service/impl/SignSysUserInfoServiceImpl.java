package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.UserSourceEnum;
import com.iyin.sign.system.common.enums.UserTypeEnum;
import com.iyin.sign.system.entity.*;
import com.iyin.sign.system.mapper.*;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuTree;
import com.iyin.sign.system.model.RedisKeyConstant;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.*;
import com.iyin.sign.system.util.*;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: SignSysUserInfoServiceImpl
 * @Description: 用户信息service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:40
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:40
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class SignSysUserInfoServiceImpl extends ServiceImpl<SignSysUserInfoMapper , SignSysUserInfo> implements SignSysUserInfoService {

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysNodeInfoService signSysNodeInfoService;

    @Autowired
    SignSysUserDataLimitMapper signSysUserDataLimitMapper;

    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;

    @Autowired
    SignSysRoleMapper signSysRoleMapper;

    @Autowired
    SignSysSealUserMapper signSysSealUserMapper;

    @Autowired
    SignSysSealInfoMapper signSysSealInfoMapper;

    @Autowired
    SignSysEnterpriseInfoMapper signSysEnterpriseInfoMapper;

    @Autowired
    SignSysDefaultConfigMapper signSysDefaultConfigMapper;

    @Autowired
    SignSysRoleMenuMapper signSysRoleMenuMapper;

    @Autowired
    ISignSysMenuMapper signSysMenuMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    TokenSettings settings;

    @Autowired
    private VerifyLicense verifyLicense;
    @Autowired
    private IAuthTokenService authTokenService;

    @Value("${localFilePath.auth}")
    public String authUrl;

    /**
     * 常用字符串
     */
    private static final String VERSIONINFO="versionInfo";
    private static final String SERIALNUMBER="serialNumber";
    private static final String ISEFFECTIVE="isEffective";


    /**
     * 允许密码错误的最大次数
     */
    private static  final Integer PASSWORD_MAX_ERROR_NUM =10;

    /**
     * 统计最近xx分钟内密码错误次数
     */
    private static final Integer PASSWORD_ERROR_COUNT_TIME=60;



    @Override
    public IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId(UserListReqVO reqVO) {
        IyinPage<UserDetailRespVO> iyinPage = new IyinPage<UserDetailRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<UserDetailRespVO> userDetailRespVOList = signSysUserInfoMapper.pageListUserInfoByNodeId(iyinPage,reqVO);
        iyinPage.setRecords(userDetailRespVOList);
        return IyinResult.getIyinResult(iyinPage);
    }

    /**
     * 查看用户详情
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<UserDetailRespVO> getUserDetail(String id) {
        SignSysUserInfo signSysUserInfo =  signSysUserInfoMapper.selectById(id);
        if(signSysUserInfo == null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1004);
        }
        UserDetailRespVO userDetailRespVO = new UserDetailRespVO();
        BeanUtils.copyProperties(signSysUserInfo,userDetailRespVO);

        //获取节点名称
        String nodeId = signSysUserInfo.getNodeId();
        SignSysNodeInfo signSysNodeInfo = signSysNodeInfoService.getById(nodeId);
        if(signSysNodeInfo!=null){
            userDetailRespVO.setNodeName(signSysNodeInfo.getNodeName());
        }

        //查询用户角色
        List<UserRoleRespVO> userRoleRespVOS = new ArrayList<UserRoleRespVO>();
        QueryWrapper<SignSysUserRole> userRoleWrapper =new QueryWrapper<SignSysUserRole>()
                .eq("user_id",id);
        List<SignSysUserRole> userRoles = signSysUserRoleMapper.selectList(userRoleWrapper);
        for(SignSysUserRole userRole:userRoles){
            SignSysRole signSysRole = signSysRoleMapper.selectById(userRole.getRoleId());
            if(signSysRole!=null){
                UserRoleRespVO  userRoleRespVO = new UserRoleRespVO();
                userRoleRespVO.setId(userRole.getId());
                userRoleRespVO.setRoleId(userRole.getRoleId());
                userRoleRespVO.setUserId(userRole.getUserId());
                userRoleRespVO.setRoleName(signSysRole.getRoleName());
                userRoleRespVOS.add(userRoleRespVO);
            }
        }
        userDetailRespVO.setUserRoleRespVOS(userRoleRespVOS);

        //获取用户的印章数据
        List<UserSealRespVO> userSealRespVOS = new ArrayList<UserSealRespVO>();
        QueryWrapper<SignSysSealUser> sealQueryWrapper = new QueryWrapper<SignSysSealUser>()
                .eq("user_id",id);
        List<SignSysSealUser> signSysSealUsers = signSysSealUserMapper.selectList(sealQueryWrapper);
        for(SignSysSealUser signSysSealUser :signSysSealUsers){
            SignSysSealInfo signSysSealInfo =  signSysSealInfoMapper.selectById(signSysSealUser.getSealId());
            if(signSysSealInfo!=null){
                UserSealRespVO userSealRespVO = new UserSealRespVO();
                BeanUtils.copyProperties(signSysSealInfo,userSealRespVO);
                userSealRespVO.setSealCode(signSysSealInfo.getPictureCode());
                userSealRespVO.setSealName(signSysSealInfo.getPictureName());
                userSealRespVO.setSealId(signSysSealUser.getSealId());
                userSealRespVOS.add(userSealRespVO);
            }
        }
        userDetailRespVO.setUserSealRespVOS(userSealRespVOS);


        //获取用户数据权限范围
        QueryWrapper<SignSysUserDataLimit> dataLimitQueryWrapper = new QueryWrapper<SignSysUserDataLimit>()
                .eq("user_id",id);
        SignSysUserDataLimit signSysUserDataLimit = signSysUserDataLimitMapper.selectOne(dataLimitQueryWrapper);
        if(signSysUserDataLimit!=null){
            userDetailRespVO.setDataLimitType(signSysUserDataLimit.getLimitType());
        }
        return IyinResult.getIyinResult(userDetailRespVO);
    }

    /**
     * 根据用户名和密码登录
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<UserLoginRespVO> loginByUserNameAndPwd(UserNameLoginReqVO reqVO, HttpServletRequest request) {
        String userName = reqVO.getUserName();
        String password = reqVO.getPassword();
        String valicode = reqVO.getValicode();
        String sessionId = request.getSession().getId();
        Map<String, Object> extra=verifyLicense.ckLicence(authUrl+VerifyLicense.LIC_NAME);
        log.info("loginByUserNameAndPwd extra："+extra.toString());
        String captcha = RedisKeyConstant.USER_LOGIN_VALICODE_KEY_PRE+sessionId;
        if(!StringUtils.isEmpty(valicode)){
            String cacheCaptcha = redisService.get(captcha);
            if(cacheCaptcha == null){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1053);
            }

            if(!cacheCaptcha.equals(valicode)){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1054);
            }
        }
        String passwordInputErrorTimesKey = RedisKeyConstant.PWORD_INPUT_ERROR_PRE + reqVO.getUserName();
        String retryCount = redisService.get(passwordInputErrorTimesKey);
        retryCount = retryCount ==null?"0":retryCount;
        AtomicInteger tempCount = new AtomicInteger(Integer.valueOf(retryCount));
        UserLoginRespVO userLoginRespVO = new UserLoginRespVO();
        userLoginRespVO.setPasswordErrorTimes(tempCount.intValue());

        //检查密码错误是否已达上限
        if(tempCount.get()>=PASSWORD_MAX_ERROR_NUM){
            return IyinResult.getIyinResult(SignSysResponseCode.SIGN_SYS_ERRORE_1007,userLoginRespVO);
        }
        QueryWrapper<SignSysUserInfo> queryWrapper = new QueryWrapper<SignSysUserInfo>().eq("user_name",userName);
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectOne(queryWrapper);
        if(signSysUserInfo ==null){
            return IyinResult.getIyinResult(SignSysResponseCode.SIGN_SYS_ERRORE_1005,userLoginRespVO);
        }
        if(1==signSysUserInfo.getIsDeleted()){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1064);
        }
        if(!BCrypt.checkpw(password,signSysUserInfo.getPassword())){
            //密码错误
            tempCount.incrementAndGet();
            redisService.set(passwordInputErrorTimesKey, String.valueOf(tempCount.intValue()), PASSWORD_ERROR_COUNT_TIME);
            userLoginRespVO.setPasswordErrorTimes(tempCount.intValue());
            return IyinResult.getIyinResult(SignSysResponseCode.SIGN_SYS_ERRORE_1005,userLoginRespVO);
        }


        if(1==signSysUserInfo.getIsForbid()){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1006);
        }

        //判断用户所在的单位是否被禁用
        String enterpriseId = signSysUserInfo.getEnterpriseOrPersonalId();
        SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectById(enterpriseId);
        if(signSysEnterpriseInfo!=null &&  1==signSysEnterpriseInfo.getIsDeleted() ){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1061);
        }

        //用户名和密码正确的情况下，清除缓存记录的密码错误次数
        redisService.delete(passwordInputErrorTimesKey);

        //删除验证码缓存key
        redisService.delete(captcha);

        //生成access_token
        String userId = signSysUserInfo.getId();
        String sessionToken = jwtUtils.createSessionToken(userId);
        redisService.set(RedisKeyConstant.USER_SESSION_TOKEN_PRE+userId,sessionToken,settings.getTokenExpirationTime());
        BeanUtils.copyProperties(signSysUserInfo,userLoginRespVO);
        userLoginRespVO.setSessionToken(sessionToken);
        userLoginRespVO.setPasswordErrorTimes(0);
        userLoginRespVO.setEnterpriseId(signSysUserInfo.getEnterpriseOrPersonalId());

        //前台logourl
        SignSysDefaultConfigInfoResp configInfoResp = new SignSysDefaultConfigInfoResp();
        SignSysDefaultConfig signSysDefaultConfig =  signSysDefaultConfigMapper.selectLastConfig();
        if(signSysDefaultConfig!=null){
            BeanUtils.copyProperties(signSysDefaultConfig,configInfoResp);
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

        //返回用户角色信息
        QueryWrapper<SignSysUserRole> queryWrapper1 = new QueryWrapper<SignSysUserRole>()
                .eq("user_id",userId);
       SignSysUserRole signSysUserRole = signSysUserRoleMapper.selectOne(queryWrapper1);
       SignSysRole signSysRole = new SignSysRole();
       if(signSysUserRole!=null){
           signSysRole = signSysRoleMapper.selectById(signSysUserRole.getRoleId());
       }
        userLoginRespVO.setRoleInfo(signSysRole);
        userLoginRespVO.setUserLicenceInfo(userLicenceInfo);
        userLoginRespVO.setConfigInfoResp(configInfoResp);
        return IyinResult.getIyinResult(userLoginRespVO);
    }

    /**
     * 用户退出
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<Boolean> loginOut(String id) {
        String sessionToken = redisService.get(RedisKeyConstant.USER_SESSION_TOKEN_PRE+id);
        if(sessionToken!=null){
            redisService.delete(RedisKeyConstant.USER_SESSION_TOKEN_PRE+id);
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 禁用用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<Boolean> forbidUser(ForbidUserReqVO reqVO) {

        String userId = reqVO.getUserId();
        Integer forbid = reqVO.getForbid();

        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        if(signSysUserInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        signSysUserInfo.setIsForbid(forbid);
        signSysUserInfo.setGmtModified(new Date());
        int count = signSysUserInfoMapper.updateById(signSysUserInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1040);
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 增加用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    @Transactional
    public IyinResult<Boolean> aaddUser(AddUserReqVO reqVO) {

        String userName = reqVO.getUserName();
        String userNickName = reqVO.getUserNickName();
        String nodeId = reqVO.getNodeId();
        String password = reqVO.getPassword();
        String enterpriseId = reqVO.getEnterpriseId();
        String roleId = reqVO.getRoleId();
        String dataLimitType = reqVO.getDataLimitType();
        boolean isMobile =ParamValidateLegalUtil.isMobile(userName);
        boolean isEmail = ParamValidateLegalUtil.isEmail(userName);
        if(!isMobile && !isEmail){
            throw new BusinessException("1023","手机号或邮箱格式不正确");
        }
        //判断账户名是否被占用了
        QueryWrapper<SignSysUserInfo> queryWrapperUser = new QueryWrapper<SignSysUserInfo>().eq("user_name",userName);
        SignSysUserInfo dbSignSysUser = signSysUserInfoMapper.selectOne(queryWrapperUser);
        if(dbSignSysUser!=null && dbSignSysUser.getIsDeleted() ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1025);
        }
        //保存账户数据
        if(dbSignSysUser ==null){
            String userId = SnowflakeIdWorker.getIdToStr();
            SignSysUserInfo signSysUserInfo = new SignSysUserInfo();
            signSysUserInfo.setId(userId);
            signSysUserInfo.setEnterpriseOrPersonalId(enterpriseId);
            signSysUserInfo.setUserType(UserTypeEnum.ENTERPRISE_USER.getCode());
            signSysUserInfo.setFirstCreate("02");
            signSysUserInfo.setUserName(userName);
            signSysUserInfo.setUserNickName(userNickName);
            signSysUserInfo.setNodeId(nodeId);
            signSysUserInfo.setLoginType(isMobile?"01":"02");
            signSysUserInfo.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
            signSysUserInfo.setGmtCreate(new Date());
            signSysUserInfo.setSource(UserSourceEnum.ADMIN_CREATE.getCode());
            int  count =signSysUserInfoMapper.insert(signSysUserInfo);
            if(count ==0){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1028);
            }

            //保存用户角色
            SignSysUserRole signSysUserRole = new SignSysUserRole();
            signSysUserRole.setId(SnowflakeIdWorker.getIdToStr());
            signSysUserRole.setUserId(userId);
            signSysUserRole.setRoleId(roleId);
            signSysUserRole.setGmtCreate(new Date());
            count = signSysUserRoleMapper.insert(signSysUserRole);
            if(count ==0){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1041);
            }

            //保存用户数据权限设置
            SignSysUserDataLimit signSysUserDataLimit = new SignSysUserDataLimit();
            signSysUserDataLimit.setId(SnowflakeIdWorker.getIdToStr());
            signSysUserDataLimit.setUserId(userId);
            signSysUserDataLimit.setLimitType(dataLimitType);
            signSysUserDataLimit.setGmtCreate(new Date());
            count = signSysUserDataLimitMapper.insert(signSysUserDataLimit);
            if(count ==0){
                throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1042);
            }
        }else {
            //更新操作
            dbSignSysUser.setEnterpriseOrPersonalId(enterpriseId);
            dbSignSysUser.setUserNickName(userNickName);
            dbSignSysUser.setNodeId(nodeId);
            dbSignSysUser.setIsDeleted(0);
            dbSignSysUser.setIsForbid(0);
            dbSignSysUser.setGmtModified(new Date());
            dbSignSysUser.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
            int  count =signSysUserInfoMapper.updateById(dbSignSysUser);
            if(count ==0){
                throw new BusinessException("1028","用户信息更新失败");
            }
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 更新用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    @Transactional
    public IyinResult<Boolean> updateUser(UpdateUserReqVO reqVO) {
        String userId = reqVO.getUserId();

        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        if(signSysUserInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        BeanUtils.copyProperties(reqVO,signSysUserInfo);

        String password = reqVO.getPassword();
        if(!StringUtils.isEmpty(password)){
            signSysUserInfo.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        }
        signSysUserInfo.setGmtModified(new Date());
        signSysUserInfoMapper.updateById(signSysUserInfo);

        //更新角色
        QueryWrapper<SignSysUserRole> delQueryWrapper =new QueryWrapper<SignSysUserRole>()
                .eq("user_id",userId);
        signSysUserRoleMapper.delete(delQueryWrapper);

        SignSysUserRole signSysUserRole = new SignSysUserRole();
        signSysUserRole.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserRole.setUserId(userId);
        signSysUserRole.setRoleId(reqVO.getRoleId());
        signSysUserRole.setGmtCreate(new Date());
        signSysUserRoleMapper.insert(signSysUserRole);

        //更新数据权限集
        QueryWrapper<SignSysUserDataLimit> delDataQueryWrapper =new QueryWrapper<SignSysUserDataLimit>()
                .eq("user_id",userId);
        signSysUserDataLimitMapper.delete(delDataQueryWrapper);
        SignSysUserDataLimit signSysUserDataLimit = new SignSysUserDataLimit();
        signSysUserDataLimit.setId(SnowflakeIdWorker.getIdToStr());
        signSysUserDataLimit.setUserId(userId);
        signSysUserDataLimit.setLimitType(reqVO.getDataLimitType());
        signSysUserDataLimit.setGmtCreate(new Date());
        signSysUserDataLimitMapper.insert(signSysUserDataLimit);
        return IyinResult.getIyinResult(true);
    }

    /**
     * 禁用单位
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<Boolean> forbidEnterprise(ForbidEnterpriseReqVO reqVO) {
        SignSysEnterpriseInfo signSysEnterpriseInfo = signSysEnterpriseInfoMapper.selectById(reqVO.getEnterpriseId());
        if(signSysEnterpriseInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        signSysEnterpriseInfo.setIsDeleted(reqVO.getForbid());
        signSysEnterpriseInfo.setGmtModified(new Date());
        int count = signSysEnterpriseInfoMapper.updateById(signSysEnterpriseInfo);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1046);
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 获取单位下的所有用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<List<UserDetailRespVO>> getAllUserInfoByEnterpriseId(String enterpriseId) {

        List<UserDetailRespVO> userDetailRespVOS =new ArrayList<UserDetailRespVO>();
        QueryWrapper<SignSysUserInfo> queryWrapper = new QueryWrapper<SignSysUserInfo>()
                .eq("enterprise_or_personal_id",enterpriseId)
                .eq("is_forbid",0);
        List<SignSysUserInfo> userInfoList =  signSysUserInfoMapper.selectList(queryWrapper);
        for(SignSysUserInfo signSysUserInfo:userInfoList){
            UserDetailRespVO respVO = new UserDetailRespVO();
            BeanUtils.copyProperties(signSysUserInfo,respVO);
            userDetailRespVOS.add(respVO);
        }
        return IyinResult.getIyinResult(userDetailRespVOS);
    }

    /**
     * 根据用户ID获取用户菜单树
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<List<MenuTree>> getUserMenuTree(String userId) {
        List<MenuTree> menuTrees = new ArrayList<>();
        //查询用户角色
        QueryWrapper<SignSysUserRole> queryWrapper = new QueryWrapper<SignSysUserRole>().eq("user_id",userId);
        SignSysUserRole signSysUserRole = signSysUserRoleMapper.selectOne(queryWrapper);
        if(signSysUserRole ==null){
            return IyinResult.getIyinResult(menuTrees);
        }
        String roleId = signSysUserRole.getRoleId();
        QueryWrapper<SignSysRoleMenu> queryWrapper1 = new QueryWrapper<SignSysRoleMenu>().eq("role_id",roleId);
        List<SignSysRoleMenu> signSysRoleMenus = signSysRoleMenuMapper.selectList(queryWrapper1);
        if(signSysRoleMenus == null || signSysRoleMenus.isEmpty()){
            return IyinResult.getIyinResult(menuTrees);
        }

        Set<String> ids = new HashSet<>();
        for(SignSysRoleMenu signSysRoleMenu :signSysRoleMenus){
            ids.add(signSysRoleMenu.getMenuId());
            //同时将父级结构树链的Id放入
            ids = getMenuParentIds(signSysRoleMenu.getMenuId(),ids);

        }

        QueryWrapper<SignSysMenu> queryWrapper2 = new QueryWrapper<SignSysMenu>().in("menu_id",ids).orderByDesc("sort");
        List<SignSysMenu> signSysMenus = signSysMenuMapper.selectList(queryWrapper2);

        //前台只暂时：合同管理 、文件管理、签章验证、账号管理
        //过滤掉 后台管理 、开放平台、打印机管理、系统管理、账号管理等后台菜单
        List<String> filterNodes = Arrays.asList(new String[]{"后台管理","开放平台","打印机管理","系统管理","账号管理"});

        for(SignSysMenu signSysMenu : signSysMenus){
            if(filterNodes.contains(signSysMenu.getName())){
                signSysMenus.remove(this);
            }
        }
        menuTrees = TreeUtil.buildTree(signSysMenus,"-1");
        return IyinResult.getIyinResult(menuTrees);
    }

    @Override
    public IyinResult<SignSysUserInfoRespVO> queryAccountInfoById(String id) {
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(id);
        if(signSysUserInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        //查询角色信息
        QueryWrapper<SignSysUserRole> queryWrapper = new QueryWrapper<SignSysUserRole>()
                .eq("user_id",id);
        SignSysUserRole userRole = signSysUserRoleMapper.selectOne(queryWrapper);
        UserRoleRespVO userRoleRespVO = new UserRoleRespVO();
        if(userRole!=null){
            SignSysRole signSysRole =  signSysRoleMapper.selectById(userRole.getRoleId());
            userRoleRespVO.setId(userRole.getId());
            userRoleRespVO.setRoleName(signSysRole.getRoleName());
            userRoleRespVO.setUserId(userRole.getUserId());
        }
        SignSysUserInfoRespVO signSysUserInfoRespVO = new SignSysUserInfoRespVO();
        BeanUtils.copyProperties(signSysUserInfo,signSysUserInfoRespVO);
        signSysUserInfoRespVO.setUserRoleRespVO(userRoleRespVO);
        return IyinResult.getIyinResult(signSysUserInfoRespVO);
    }

    @Override
    public IyinResult<SignSysDefaultConfigInfoResp> getSysDefaultConfig() {
        //前台logourl
        SignSysDefaultConfigInfoResp configInfoResp = new SignSysDefaultConfigInfoResp();
        SignSysDefaultConfig signSysDefaultConfig =  signSysDefaultConfigMapper.selectLastConfig();
        if(signSysDefaultConfig!=null){
            BeanUtils.copyProperties(signSysDefaultConfig,configInfoResp);
        }
        return IyinResult.getIyinResult(configInfoResp);
    }

    /**
     * 查询所选节点下的用户列表
     *
     * @Author: yml
     * @CreateDate: 2019/9/2
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/2
     * @Version: 0.0.1
     * @param reqVO
     * @return : com.iyin.sign.system.model.IyinResult<com.iyin.sign.system.model.IyinPage<com.iyin.sign.system.vo.resp.UserDetailRespVO>>
     */
    @Override
    public IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId2(UserListReqVO reqVO) {
        IyinPage<UserDetailRespVO> iyinPage = new IyinPage<UserDetailRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<UserDetailRespVO> userDetailRespVOList = signSysUserInfoMapper.pageListUserInfoByNodeId2(iyinPage,reqVO);
        iyinPage.setRecords(userDetailRespVOList);
        return IyinResult.getIyinResult(iyinPage);
    }

    /**
     * 删除用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/6 
     * @UpdateUser:  
     * @UpdateDate:  2019/9/6 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    @Override
    public IyinResult<Boolean> delUser(DelUserReqVO reqVO) {
        SignSysUserInfo signSysUserInfo = new SignSysUserInfo();
        signSysUserInfo.setId(reqVO.getUserId());
        signSysUserInfo.setIsDeleted(1);
        int count =  signSysUserInfoMapper.updateById(signSysUserInfo);
        if(count==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        return IyinResult.getIyinResult(true);
    }

    /**
     * 邮箱帐号绑定手机号
     *
     * @Author: yml
     * @CreateDate: 2019/10/24
     * @UpdateUser: yml
     * @UpdateDate: 2019/10/24
     * @Version: 0.0.1
     * @param phone
     * @param request
     * @return : com.iyin.sign.system.model.IyinResult<java.lang.Boolean>
     */
    @Override
    public IyinResult<Boolean> bindingPhone(String phone, HttpServletRequest request) {
        String userId = getUserId(request);
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        signSysUserInfo.setPhone(phone);
        signSysUserInfo.setGmtModified(new Date());
        int relt = signSysUserInfoMapper.updateById(signSysUserInfo);
        return IyinResult.success(relt > 0);
    }

    public Set<String> getMenuParentIds(String menuId,Set<String> ids){
        QueryWrapper<SignSysMenu> queryWrapper = new QueryWrapper<SignSysMenu>().eq("menu_id",menuId);
        SignSysMenu signSysMenu = signSysMenuMapper.selectOne(queryWrapper);
        if(signSysMenu ==null){
            return ids;
        }
        if("-1".equals(signSysMenu.getParentId())){
            ids.add(menuId);
            return ids;
        }
        ids.add(menuId);
        return getMenuParentIds(signSysMenu.getParentId(),ids);
    }

    /**
     * 根据session_token获取登录用户ID
     *
     * @param req
     * @return : java.lang.String
     * @Author: yml
     * @CreateDate: 2019/8/14
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/14
     * @Version: 0.0.1
     */
    private String getUserId(HttpServletRequest req) {
        String userId;
        String sessionToken = req.getHeader("session_token");
        if(!StringUtils.isEmpty(sessionToken)){
            Claims claims = jwtUtils.parseJWT(sessionToken);
            userId = String.valueOf(claims.get("userId"));
        }else{
            String apiToken = req.getHeader("api_token");
            SysSignApplication sysSignApplication = authTokenService.verifyAuthToken(apiToken);
            userId = sysSignApplication.getUserId();
        }

        return userId;
    }
}
