package com.iyin.sign.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.vo.req.UserListReqVO;
import com.iyin.sign.system.vo.resp.UserDetailRespVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SignSysUserInfoMapper
 * @Description: SignSysAdminUserMapper
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:36
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:36
 * @Version: 0.0.1
 */
public interface SignSysUserInfoMapper extends BaseMapper<SignSysUserInfo> {

    /**
    　　* @description: 通过NodeId获取用户信息集合
    　　* @param iyinPage
     　 * @param reqVO
    　　* @return List<UserDetailRespVO>
    　　* @throws
    　　* @author tangdefan
    　　* @date 2019/8/19 17:06
    　　*/
    List<UserDetailRespVO> pageListUserInfoByNodeId(IyinPage<UserDetailRespVO> iyinPage, @Param(value = "reqVO") UserListReqVO reqVO);

    /**
     * 查询所选节点下的用户列表
     *
     * @Author: yml
     * @CreateDate: 2019/9/2
     * @UpdateUser: yml
     * @UpdateDate: 2019/9/2
     * @Version: 0.0.1
     * @param iyinPage
     * @param reqVO
     * @return : java.util.List<com.iyin.sign.system.vo.resp.UserDetailRespVO>
     */
    List<UserDetailRespVO> pageListUserInfoByNodeId2(IyinPage<UserDetailRespVO> iyinPage, @Param(value = "reqVO")  UserListReqVO reqVO);
}
