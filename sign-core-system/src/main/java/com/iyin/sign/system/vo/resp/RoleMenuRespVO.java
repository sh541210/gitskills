package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RoleMenuRespVO
 * @Description: 角色菜单权限设置
 * @Author: luwenxiong
 * @CreateDate: 2019/7/23 11:16
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/23 11:16
 * @Version: 0.0.1
 */
@Data
public class RoleMenuRespVO implements Serializable {

    private static final long serialVersionUID = 3071018507624817529L;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "菜单ID")
    private String menuId;



}
