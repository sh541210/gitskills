package com.iyin.sign.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iyin.sign.system.entity.SignSysRole;
import com.iyin.sign.system.entity.SignSysRoleMenu;
import com.iyin.sign.system.entity.SignSysUserRole;
import com.iyin.sign.system.mapper.SignSysRoleMapper;
import com.iyin.sign.system.mapper.SignSysRoleMenuMapper;
import com.iyin.sign.system.mapper.SignSysUserRoleMapper;
import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.SignSysResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.service.SignSysRoleService;
import com.iyin.sign.system.util.SnowflakeIdWorker;
import com.iyin.sign.system.vo.req.AddRoleReqVO;
import com.iyin.sign.system.vo.req.ConfigRoleMenuReqVO;
import com.iyin.sign.system.vo.req.SignSysRolePageListReqVO;
import com.iyin.sign.system.vo.req.UpdateRoleReqVO;
import com.iyin.sign.system.vo.resp.RoleMenuRespVO;
import com.iyin.sign.system.vo.resp.SignSysRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SignSysRoleServiceImpl
 * @Description: 角色服务
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:11
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:11
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class SignSysRoleServiceImpl extends ServiceImpl<SignSysRoleMapper,SignSysRole> implements SignSysRoleService {

    @Autowired
    SignSysRoleMapper signSysRoleMapper;

    @Autowired
    SignSysUserRoleMapper signSysUserRoleMapper;

    @Autowired
    SignSysRoleMenuMapper signSysRoleMenuMapper;

    @Override
    public IyinResult<IyinPage<SignSysRoleRespVO>> pageListRole(SignSysRolePageListReqVO reqVO) {
        IyinPage<SignSysRoleRespVO> iyinPage = new IyinPage<SignSysRoleRespVO>(reqVO.getCurrentPage(),reqVO.getPageSize());
        List<SignSysRoleRespVO> signSysRoleRespVOList =  signSysRoleMapper.pageListRole(iyinPage,reqVO);
        iyinPage.setRecords(signSysRoleRespVOList);
        return IyinResult.getIyinResult(iyinPage);
    }

    @Override
    public IyinResult<Boolean> addRole(AddRoleReqVO reqVO) {
        String roleName = reqVO.getRoleName();
        QueryWrapper<SignSysRole> queryWrapper = new QueryWrapper<SignSysRole>()
                .eq("role_name",roleName);
        SignSysRole signSysRole = signSysRoleMapper.selectOne(queryWrapper);
        if(signSysRole!=null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1043);
        }

        signSysRole = new SignSysRole();
        BeanUtils.copyProperties(reqVO,signSysRole);
        signSysRole.setId(SnowflakeIdWorker.getIdToStr());
        signSysRole.setGmtCreate(new Date());
        int count = signSysRoleMapper.insert(signSysRole);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1044);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<Boolean> updateRole(UpdateRoleReqVO reqVO) {
        String roleId = reqVO.getRoleId();

        SignSysRole signSysRole = signSysRoleMapper.selectById(roleId);
        if(signSysRole==null){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1000);
        }
        QueryWrapper<SignSysRole> queryWrapper =new QueryWrapper<SignSysRole>()
                .eq("role_name",reqVO.getRoleName());
        SignSysRole signSysRoleOne = signSysRoleMapper.selectOne(queryWrapper);
        if(signSysRoleOne!=null && !signSysRoleOne.getId().equals(roleId)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1043);
        }
        BeanUtils.copyProperties(reqVO,signSysRole);
        signSysRole.setGmtModified(new Date());
        int count = signSysRoleMapper.updateById(signSysRole);
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1045);
        }
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<Boolean> delRole(String roleId) {

        //删除所有已经分配出去的权限数据
        QueryWrapper<SignSysUserRole> queryWrapper = new QueryWrapper<SignSysUserRole>()
                .eq("role_id",roleId);
        //先判断当前角色是否被使用
        List<SignSysUserRole> signSysUserRoles = signSysUserRoleMapper.selectList(queryWrapper);
        if(signSysUserRoles!=null && !signSysUserRoles.isEmpty()){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1056);
        }

        //系统初始化的角色不让删除
        if("10000".equals(roleId)){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1060);
        }
        signSysRoleMapper.deleteById(roleId);
        return IyinResult.getIyinResult(true);
    }

    @Override
    public IyinResult<List<SignSysRoleRespVO>> getAllRole() {
        QueryWrapper<SignSysRole> queryWrapper = new QueryWrapper<SignSysRole>()
                .eq("is_deleted",0);
        List<SignSysRole> signSysRoleList = signSysRoleMapper.selectList(queryWrapper);
        List<SignSysRoleRespVO> signSysRoleRespVOS = new ArrayList<SignSysRoleRespVO>();
        for(SignSysRole signSysRole :signSysRoleList){
            SignSysRoleRespVO signSysRoleRespVO = new SignSysRoleRespVO();
            BeanUtils.copyProperties(signSysRole,signSysRoleRespVO);
            signSysRoleRespVOS.add(signSysRoleRespVO);
        }
        return IyinResult.getIyinResult(signSysRoleRespVOS);
    }

    @Override
    public IyinResult<List<RoleMenuRespVO>> getRoleMenuList(String roleId) {
        QueryWrapper<SignSysRoleMenu> queryWrapper = new QueryWrapper<SignSysRoleMenu>()
                .eq("`role_id` ",roleId);
        List<SignSysRoleMenu> signSysRoleMenus = signSysRoleMenuMapper.selectList(queryWrapper);
        List<RoleMenuRespVO> roleMenuRespVOS = new ArrayList<RoleMenuRespVO>();
        for(SignSysRoleMenu roleMenu:signSysRoleMenus){
            RoleMenuRespVO respVO = new RoleMenuRespVO();
            respVO.setRoleId(roleMenu.getRoleId());
            respVO.setMenuId(roleMenu.getMenuId());
            roleMenuRespVOS.add(respVO);
        }
        return IyinResult.getIyinResult(roleMenuRespVOS);
    }

    @Override
    public IyinResult<Boolean> configRoleMenu(ConfigRoleMenuReqVO reqVO) {
        QueryWrapper<SignSysRoleMenu> queryWrapper = new QueryWrapper<SignSysRoleMenu>()
                .eq("`role_id` ",reqVO.getRoleId());
        signSysRoleMenuMapper.delete(queryWrapper);
        List<String> menuIds = reqVO.getMenuIds();
        int count =0;
        for(String menuId:menuIds){
            SignSysRoleMenu signSysRoleMenu = new SignSysRoleMenu();
            signSysRoleMenu.setId(SnowflakeIdWorker.getIdToStr());
            signSysRoleMenu.setRoleId(reqVO.getRoleId());
            signSysRoleMenu.setMenuId(menuId);
            signSysRoleMenu.setGmtModified(new Date());
            signSysRoleMenu.setIsDeleted(0);
            signSysRoleMenuMapper.insert(signSysRoleMenu);
            count++;
        }
        if(count ==0){
            throw new BusinessException(SignSysResponseCode.SIGN_SYS_ERRORE_1050);
        }
        return IyinResult.getIyinResult(true);
    }
}
