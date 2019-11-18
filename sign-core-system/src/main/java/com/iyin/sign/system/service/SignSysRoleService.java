package com.iyin.sign.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.vo.req.AddRoleReqVO;
import com.iyin.sign.system.vo.req.ConfigRoleMenuReqVO;
import com.iyin.sign.system.vo.req.SignSysRolePageListReqVO;
import com.iyin.sign.system.vo.req.UpdateRoleReqVO;
import com.iyin.sign.system.vo.resp.RoleMenuRespVO;
import com.iyin.sign.system.vo.resp.SignSysRoleRespVO;

import java.util.List;

/**
 * @ClassName: SignSysRoleService
 * @Description: 签章系统角色服务
 * @Author: wdf
 * @CreateDate: 2019/8/7
 * @UpdateUser: wdf
 * @UpdateDate: 2019/8/7
 * @Version: 1.0.0
 */
public interface SignSysRoleService extends IService<SignSysRole> {
    IyinResult<IyinPage<SignSysRoleRespVO>> pageListRole(SignSysRolePageListReqVO reqVO);

    IyinResult<Boolean> addRole(AddRoleReqVO reqVO);

    IyinResult<Boolean> updateRole(UpdateRoleReqVO reqVO);

    IyinResult<Boolean> delRole(String roleId);

    IyinResult<List<SignSysRoleRespVO>> getAllRole();

    IyinResult<List<RoleMenuRespVO>> getRoleMenuList(String roleId);

    IyinResult<Boolean> configRoleMenu(ConfigRoleMenuReqVO reqVO);
}
