package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysSealUser;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.SignSysSealUserMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysSealUserService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.BindUserSealReqVO;
import com.iyin.sign.system.vo.req.UnBindUserSealReqVO;
import com.iyin.sign.system.vo.resp.UserBaseInfoRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SignSysSealUserServiceImpl
 * @Description: 印章权限信息service
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 11:19
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 11:19
 * @Version: 0.0.1
 */
@Slf4j
@Service
public class SignSysSealUserServiceImpl extends ServiceImpl<SignSysSealUserMapper,SignSysSealUser>  implements SignSysSealUserService {

    @Autowired
    SignSysSealUserMapper signSysSealUserMapper;

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Override
    public IyinResult<Boolean> settingSealUserLink(BindUserSealReqVO reqVO) {
        List<String> userIds = reqVO.getUserIds();
        String sealId = reqVO.getSealId();

        //全量提交，先删除之前的印章下的所有成员
        QueryWrapper<SignSysSealUser> queryWrapper = new QueryWrapper<SignSysSealUser>()
                .eq("seal_id",sealId);
        signSysSealUserMapper.delete(queryWrapper);
        for(String useId:userIds){
            SignSysSealUser signSysSealUser = new SignSysSealUser();
            signSysSealUser.setId(SnowflakeIdWorker.getIdToStr());
            signSysSealUser.setGmtCreate(new Date());
            signSysSealUser.setIsDeleted(0);
            signSysSealUser.setSealId(sealId);
            signSysSealUser.setUserId(useId);
            signSysSealUserMapper.insert(signSysSealUser);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<List<UserBaseInfoRespVO>> queryListUserOfSeal(String sealId) {
        QueryWrapper<SignSysSealUser> queryWrapper = new QueryWrapper<SignSysSealUser>().eq("seal_id",sealId);
        List<SignSysSealUser> signSysSealUsers = signSysSealUserMapper.selectList(queryWrapper);
        List<String> userIds = new ArrayList<String>();
        for(SignSysSealUser signSysSealUser :signSysSealUsers){
            userIds.add(signSysSealUser.getUserId());
        }
        List<UserBaseInfoRespVO> userBaseInfoRespVOS = new ArrayList<UserBaseInfoRespVO>();
        if(userIds!=null && !userIds.isEmpty()){
            List<SignSysUserInfo> signSysUserInfos =  signSysUserInfoMapper.selectBatchIds(userIds);
            for(SignSysUserInfo signSysUserInfo :signSysUserInfos){
                UserBaseInfoRespVO userBaseInfoRespVO = new UserBaseInfoRespVO();
                BeanUtils.copyProperties(signSysUserInfo,userBaseInfoRespVO);
                userBaseInfoRespVOS.add(userBaseInfoRespVO);
            }
        }
        return IyinResult.getIyinResult(userBaseInfoRespVOS);
    }

    @Override
    public IyinResult<Boolean> unBindSealUserLink(UnBindUserSealReqVO reqVO) {

        QueryWrapper<SignSysSealUser> queryWrapper = new QueryWrapper<SignSysSealUser>()
                .eq("seal_id",reqVO.getSealId())
                .eq("user_id",reqVO.getUserId());
        SignSysSealUser dbSignSysSealUser = signSysSealUserMapper.selectOne(queryWrapper);
        if(dbSignSysSealUser!=null){
            signSysSealUserMapper.deleteById(dbSignSysSealUser.getId());
        }
        return IyinResult.getIyinResult(true);
    }
}
