package com.iyin.sign.system.controller;

import com.iyin.sign.system.model.IyinPage;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.service.SignSysRoleService;
import com.iyin.sign.system.vo.req.AddRoleReqVO;
import com.iyin.sign.system.vo.req.ConfigRoleMenuReqVO;
import com.iyin.sign.system.vo.req.SignSysRolePageListReqVO;
import com.iyin.sign.system.vo.req.UpdateRoleReqVO;
import com.iyin.sign.system.vo.resp.RoleMenuRespVO;
import com.iyin.sign.system.vo.resp.SignSysRoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: SignSysRoleManagerController
 * @Description: 角色管理
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 17:00
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 17:00
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("role")
@Slf4j
@Api(tags = "角色管理")
public class SignSysRoleManagerController {

    @Autowired
    SignSysRoleService signSysRoleService;

    @ApiOperation("添加角色")
    @PostMapping("/addRole")
    public IyinResult<Boolean> addRole(@RequestBody @Valid AddRoleReqVO reqVO){
      return  signSysRoleService.addRole(reqVO);
    }

    @ApiOperation("编辑角色")
    @PostMapping("/updateRole")
    public IyinResult<Boolean> updateRole(@RequestBody @Valid UpdateRoleReqVO reqVO){
        return  signSysRoleService.updateRole(reqVO);
    }

    @ApiOperation("删除角色")
    @GetMapping("/delRole/{roleId}")
    public IyinResult<Boolean> delRole(@PathVariable(value = "roleId") String roleId){
        return  signSysRoleService.delRole(roleId);
    }


    @ApiOperation("分页查询角色列表")
    @PostMapping("/pageListRole")
    public IyinResult<IyinPage<SignSysRoleRespVO>> pageListRole(@RequestBody @Valid SignSysRolePageListReqVO reqVO){
       return  signSysRoleService.pageListRole(reqVO);
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/getAllRole")
    public IyinResult<List<SignSysRoleRespVO>> getAllRole(){
        return  signSysRoleService.getAllRole();
    }

    @ApiOperation("查询角色菜单权限")
    @GetMapping("/getRoleMenuList/{roleId}")
    public IyinResult<List<RoleMenuRespVO>> getRoleMenuList(@PathVariable(value = "roleId")String roleId){
        return signSysRoleService.getRoleMenuList(roleId);
    }

    @ApiOperation("配置角色菜单")
    @PostMapping("/configRoleMenu")
    public IyinResult<Boolean> configRoleMenu(@RequestBody @Valid ConfigRoleMenuReqVO reqVO){
        return  signSysRoleService.configRoleMenu(reqVO);
    }

}
