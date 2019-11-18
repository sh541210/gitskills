package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.FileResource;
import com.iyin.sign.system.entity.SignSysCompactInfo;
import com.iyin.sign.system.entity.SignSysPrintAuthUser;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.mapper.ISignSysCompactInfoMapper;
import com.iyin.sign.system.mapper.SignSysPrintAuthUserMapper;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.FileResourceService;
import com.iyin.sign.system.service.ISignSysPrintAuthUserService;
import com.iyin.sign.system.util.JWTUtils;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.SignSysContractPrintAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserAddReqVO;
import com.iyin.sign.system.vo.req.SignSysPrintAuthUserDelReqVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserListRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintAuthUserRespVO;
import com.iyin.sign.system.vo.resp.SignSysPrintInfoRespVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 打印分配表 服务实现类
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@Service
@Slf4j
public class SignSysPrintAuthUserServiceImpl extends ServiceImpl<SignSysPrintAuthUserMapper, SignSysPrintAuthUser> implements ISignSysPrintAuthUserService {

    @Resource
    private SignSysPrintAuthUserMapper signSysPrintAuthUserMapper;
    @Resource
    private FileResourceMapper fileResourceMapper;
    @Autowired
    private FileResourceService fileResourceService;

    @Resource
    private ISignSysCompactInfoMapper signSysCompactInfoMapper;

    private final JWTUtils jwtUtils;

    @Autowired
    public SignSysPrintAuthUserServiceImpl(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Boolean addPrintAuthUser(SignSysPrintAuthUserAddReqVO signSysPrintAuthUserAddReqVO, HttpServletRequest request) {
        String[] userIds=signSysPrintAuthUserAddReqVO.getUserIds().split(",");
        List<SignSysPrintAuthUser> list=new ArrayList<>();
        String user = getUser(request);
        boolean flag=false;
        for(String userId:userIds){
            if(StringUtils.isNotBlank(userId)) {
                SignSysPrintAuthUser signSysPrintAuthUser = new SignSysPrintAuthUser();
                BeanUtils.copyProperties(signSysPrintAuthUserAddReqVO, signSysPrintAuthUser);
                signSysPrintAuthUser.setId(String.valueOf(SnowflakeIdWorker.getId()));
                signSysPrintAuthUser.setUserId(userId);
                signSysPrintAuthUser.setGmtCreate(new Date());
                signSysPrintAuthUser.setCreateUser(user);
                list.add(signSysPrintAuthUser);
                SignSysPrintAuthUser del=new SignSysPrintAuthUser();
                del.setUserId(userId);
                del.setFileCode(signSysPrintAuthUserAddReqVO.getFileCode());
                signSysPrintAuthUserMapper.delete(new QueryWrapper<>(del));
            }
        }
        //add
        if(!list.isEmpty()) {
            flag = this.saveBatch(list);
        }

        FileResource fileResource =new FileResource();
        fileResource.setPrintDisNum(signSysPrintAuthUserAddReqVO.getPrintNum());
        fileResource.setFileCode(signSysPrintAuthUserAddReqVO.getFileCode());
        fileResourceMapper.updateFile(fileResource);
        return flag;
    }

    @Override
    public SignSysPrintAuthUserListRespVO getPrintAuthList(String fileCode) {
        SignSysPrintAuthUserListRespVO signSysPrintAuthUserListRespVO=new SignSysPrintAuthUserListRespVO();
        List<SignSysPrintAuthUserRespVO> list=signSysPrintAuthUserMapper.getPrintAuthList(fileCode);
        signSysPrintAuthUserListRespVO.setList(list);
        FileResource fileResource=fileResourceService.findByFileCode(fileCode);
        signSysPrintAuthUserListRespVO.setPrintDisNum(fileResource.getPrintDisNum());
        return signSysPrintAuthUserListRespVO;
    }

    @Override
    public int delPrintAuthUser(SignSysPrintAuthUserDelReqVO signSysPrintAuthUserDelReqVO, HttpServletRequest request) {
        String[] userIds=signSysPrintAuthUserDelReqVO.getUserIds().split(",");
        getUser(request);
        int flag=0;
        for(String userId:userIds){
            if(StringUtils.isNotBlank(userId)) {
                SignSysPrintAuthUser del=new SignSysPrintAuthUser();
                del.setUserId(userId);
                del.setFileCode(signSysPrintAuthUserDelReqVO.getFileCode());
                int num=signSysPrintAuthUserMapper.delete(new QueryWrapper<>(del));
                if(num>0){
                    flag++;
                }
            }
        }
        return flag;
    }


    /**
     * 获取已分配对象与次数
     *
     * @Author: yml
     * @CreateDate: 2019/9/3
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/3
     * @Version: 0.0.1
     * @param fileCode
     * @return : com.iyin.sign.system.vo.resp.SignSysPrintInfoRespVO
     */
    @Override
    public SignSysPrintInfoRespVO getPrintAuthList2(String fileCode) {
        List<SignSysPrintAuthUserRespVO> printAuthList = signSysPrintAuthUserMapper.getPrintAuthList(fileCode);
        SignSysPrintInfoRespVO signSysPrintInfoRespVO = new SignSysPrintInfoRespVO();
        signSysPrintInfoRespVO.setSignSysPrintAuthUserRespVOS(printAuthList);
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper.selectById(fileCode);
        if (null != signSysCompactInfo) {
            signSysPrintInfoRespVO.setPrintNum(signSysCompactInfo.getPrintNum());
        }
        List<SignSysPrintAuthUser> signSysPrintAuthUsers = signSysPrintAuthUserMapper.selectList(
                Wrappers.<SignSysPrintAuthUser>lambdaQuery().eq(SignSysPrintAuthUser::getFileCode, fileCode));
        if (!CollectionUtils.isEmpty(signSysPrintAuthUsers)) {
            signSysPrintInfoRespVO.setIsFoggy(signSysPrintAuthUsers.get(0).getIsFoggy());
            signSysPrintInfoRespVO.setIsGrey(signSysPrintAuthUsers.get(0).getIsGrey());
        }
        return signSysPrintInfoRespVO;
    }

    /**
     * 合同添加打印分配
     *
     * @Author: yml
     * @CreateDate: 2019/9/4
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/4
     * @Version: 0.0.1
     * @param contractPrintAddReqVO
     * @param request
     * @return : java.lang.Boolean
     */
    @Override
    public Boolean addPrintAuthUser2(SignSysContractPrintAddReqVO contractPrintAddReqVO, HttpServletRequest request) {
        String[] userIds=contractPrintAddReqVO.getUserIds().split(",");
        List<SignSysPrintAuthUser> list=new ArrayList<>();
        String user = getUser(request);
        boolean flag=false;
        for(String userId:userIds){
            if(StringUtils.isNotBlank(userId)) {
                SignSysPrintAuthUser signSysPrintAuthUser = new SignSysPrintAuthUser();
                BeanUtils.copyProperties(contractPrintAddReqVO, signSysPrintAuthUser);
                signSysPrintAuthUser.setId(String.valueOf(SnowflakeIdWorker.getId()));
                signSysPrintAuthUser.setUserId(userId);
                signSysPrintAuthUser.setGmtCreate(new Date());
                signSysPrintAuthUser.setCreateUser(user);
                signSysPrintAuthUser.setFileCode(contractPrintAddReqVO.getContractId());
                list.add(signSysPrintAuthUser);
                SignSysPrintAuthUser del = new SignSysPrintAuthUser();
                del.setUserId(userId);
                del.setFileCode(contractPrintAddReqVO.getContractId());
                signSysPrintAuthUserMapper.delete(new QueryWrapper<>(del));
            }
        }
        //add
        if(!list.isEmpty()) {
            flag = this.saveBatch(list);
        }
        SignSysCompactInfo signSysCompactInfo = signSysCompactInfoMapper
                .selectById(contractPrintAddReqVO.getContractId());
        signSysCompactInfo.setPrintNum(contractPrintAddReqVO.getPrintNum().toString());
        signSysCompactInfoMapper.updateById(signSysCompactInfo);
        return flag;
    }

    private String getUser(HttpServletRequest request) {
        log.info("SignSysPrintAuthUserServiceImpl");
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
