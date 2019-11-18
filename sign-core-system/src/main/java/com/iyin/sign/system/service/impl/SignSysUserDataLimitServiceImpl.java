package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.common.enums.DataLimitTypeEnum;
import com.iyin.sign.system.entity.SignSysUserDataLimit;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.SignSysUserDataLimitMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.SignSysNodeInfoService;
import com.iyin.sign.system.service.SignSysUserDataLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SignSysUserDataLimitServiceImpl
 * @Description: SignSysUserDataLimitServiceImpl
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:44
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:44
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysUserDataLimitServiceImpl extends ServiceImpl<SignSysUserDataLimitMapper,SignSysUserDataLimit> implements SignSysUserDataLimitService {

    @Autowired
    SignSysUserDataLimitMapper signSysUserDataLimitMapper;

    @Autowired
    SignSysUserInfoMapper signSysUserInfoMapper;

    @Autowired
    SignSysNodeInfoService signSysNodeInfoService;


    @Override
    public Boolean checkResouceDataLimit(String ownerUserId, String userId) {

        QueryWrapper<SignSysUserDataLimit> queryWrapper = new QueryWrapper<SignSysUserDataLimit>()
                .eq("user_id",userId);
        SignSysUserDataLimit signSysUserDataLimit = signSysUserDataLimitMapper.selectOne(queryWrapper);

        //数据权限级别默认是自己
        String limitType=DataLimitTypeEnum.MYSELF_DATA_LIMIT.getCode();
        if(signSysUserDataLimit!=null){
            limitType = signSysUserDataLimit.getLimitType();
        }

        /**
         * 0:全部----------只要属于同一单位下的用户，资源就共享
         * 1：自己---------只能查看自己创建的资源
         * 2：本级及子级---能查询所属节点及其子节点下所有用户创建的资源
         */
        SignSysUserInfo ownerUser = signSysUserInfoMapper.selectById(ownerUserId);
        SignSysUserInfo userInfo = signSysUserInfoMapper.selectById(userId);
        String ownerEnterpriseId = ownerUser==null?null:ownerUser.getEnterpriseOrPersonalId();
        String userEnterpriseId =userInfo==null?null:userInfo.getEnterpriseOrPersonalId();
        Boolean res=true;
        if(limitType.equals(DataLimitTypeEnum.ALL_DATA_LIMIT.getCode())){
            if(ownerEnterpriseId!=null && userEnterpriseId!=null && !ownerEnterpriseId.equals(userEnterpriseId)){
               return false;
            }
        }
        if(limitType.equals(DataLimitTypeEnum.MYSELF_DATA_LIMIT.getCode())){
            if(!ownerUserId.equals(userId)){
                return false;
            }
        }
        if(limitType.equals(DataLimitTypeEnum.MYSELF_UNIT_SON_DATA_LIMIT.getCode())){
            if(ownerEnterpriseId!=null && userEnterpriseId!=null && !ownerEnterpriseId.equals(userEnterpriseId)){
                return false;
            }
            String ownerNodeId = ownerUser ==null?null:ownerUser.getNodeId();
            String userNodeId = userInfo==null?null:userInfo.getNodeId();
            List<String> nodes = signSysNodeInfoService.getNodeAndSonNodeIdByUserId(userId);
            if(!(nodes.contains(ownerNodeId) && nodes.contains(userNodeId))){
                return false;
            }
        }
        return res;
    }

    @Override
    public List<String> getPowerScopeUserIds(String userId) {
        SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(userId);
        if(signSysUserInfo ==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1004);
        }
        QueryWrapper<SignSysUserDataLimit> queryWrapper = new QueryWrapper<SignSysUserDataLimit>()
                .eq("user_id",userId);
        SignSysUserDataLimit signSysUserDataLimit = signSysUserDataLimitMapper.selectOne(queryWrapper);

        List<String> ids = new ArrayList<>();
        //数据权限级别默认是自己
        String limitType=DataLimitTypeEnum.MYSELF_DATA_LIMIT.getCode();
        if(signSysUserDataLimit!=null){
            limitType = signSysUserDataLimit.getLimitType();
        }

        if(limitType.equals(DataLimitTypeEnum.ALL_DATA_LIMIT.getCode())){
            //数据权限为全部，查询该单位下的全部用户
            ids.clear();
            QueryWrapper<SignSysUserInfo> queryWrapper1 = new QueryWrapper<SignSysUserInfo>()
                    .eq("enterprise_or_personal_id",signSysUserInfo.getEnterpriseOrPersonalId());
            List<SignSysUserInfo> userInfos =  signSysUserInfoMapper.selectList(queryWrapper1);
            if(userInfos!=null && !userInfos.isEmpty()){
                for(SignSysUserInfo userInfo :userInfos){
                    ids.add(userInfo.getId());
                }
            }
        }
        if(limitType.equals(DataLimitTypeEnum.MYSELF_DATA_LIMIT.getCode())){
            //数据权限为自己，自返回自己id
            ids.clear();
            ids.add(userId);
        }
        if(limitType.equals(DataLimitTypeEnum.MYSELF_UNIT_SON_DATA_LIMIT.getCode())){
            //数据权限为本级及子级，返回 当前及其子节点下所有用户ID
            ids.clear();
            List<String> nodes = signSysNodeInfoService.getNodeAndSonNodeIdByUserId(userId);
            QueryWrapper<SignSysUserInfo> queryWrapper2 = new QueryWrapper<SignSysUserInfo>()
                    .in("node_id",nodes);
            List<SignSysUserInfo> userInfos =  signSysUserInfoMapper.selectList(queryWrapper2);
            if(userInfos!=null && !userInfos.isEmpty()){
                for(SignSysUserInfo userInfo :userInfos){
                    ids.add(userInfo.getId());
                }
            }
        }
        return ids;
    }
}
