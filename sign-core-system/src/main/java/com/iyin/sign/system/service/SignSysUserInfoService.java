package com.iyin.sign.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.MenuTree;
import com.iyin.sign.system.vo.req.*;
import com.iyin.sign.system.vo.resp.SignSysDefaultConfigInfoResp;
import com.iyin.sign.system.vo.resp.SignSysUserInfoRespVO;
import com.iyin.sign.system.vo.resp.UserDetailRespVO;
import com.iyin.sign.system.vo.resp.UserLoginRespVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: SignSysUserInfoService
 * @Description: 签章系统用户信息服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysUserInfoService extends IService<SignSysUserInfo> {

    IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId(UserListReqVO reqVO);

    IyinResult<UserDetailRespVO> getUserDetail(String id);

    IyinResult<UserLoginRespVO> loginByUserNameAndPwd(UserNameLoginReqVO reqVO, HttpServletRequest request);

    IyinResult<Boolean> loginOut(String id);

    IyinResult<Boolean> forbidUser(ForbidUserReqVO reqVO);

    IyinResult<Boolean> aaddUser(AddUserReqVO reqVO);

    IyinResult<Boolean> updateUser(UpdateUserReqVO reqVO);

    IyinResult<Boolean> forbidEnterprise(ForbidEnterpriseReqVO reqVO);

    IyinResult<List<UserDetailRespVO>> getAllUserInfoByEnterpriseId(String enterpriseId);

    IyinResult<List<MenuTree>> getUserMenuTree(String userId);

    /**
     * 根据用户ID查询账户信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/8/27 
     * @UpdateUser:  
     * @UpdateDate:  2019/8/27 
     * @Version:     0.0.1
     * @return       
     * @throws       
     */
    IyinResult<SignSysUserInfoRespVO> queryAccountInfoById(String id);

    /**
     * 获取后台配置的默认信息
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/2
     * @UpdateUser:
     * @UpdateDate:  2019/9/2
     * @Version:     0.0.1
     * @return
     * @throws
     */
    IyinResult<SignSysDefaultConfigInfoResp> getSysDefaultConfig();

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
    IyinResult<IyinPage<UserDetailRespVO>> pageListUserInfoByNodeId2(UserListReqVO reqVO);

    /**
     * 删除用户
     * @Author:      luwenxiong
     * @CreateDate:  2019/9/4
     * @UpdateUser:
     * @UpdateDate:  2019/9/4
     * @Version:     0.0.1
     * @return
     * @throws
     */
    IyinResult<Boolean> delUser(DelUserReqVO reqVO);

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
    IyinResult<Boolean> bindingPhone(String phone, HttpServletRequest request);
}
