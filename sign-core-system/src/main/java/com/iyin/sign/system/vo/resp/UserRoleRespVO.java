package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserRole
 * @Description: 用户角色
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 16:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 16:39
 * @Version: 0.0.1
 */
@Data
public class UserRoleRespVO implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "角色ID")
    private String roleId;
}
