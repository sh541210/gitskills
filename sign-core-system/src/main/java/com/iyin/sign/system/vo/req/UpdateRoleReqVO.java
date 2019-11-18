package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UpdateRoleReqVO
 * @Description: 编辑角色请求VO
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 17:29
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 17:29
 * @Version: 0.0.1
 */
@Data
public class UpdateRoleReqVO implements Serializable {

    private static final long serialVersionUID = 4761491714905081538L;

    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    @NotBlank(message = "角色标识不能为空")
    private String roleFlag;

    @ApiModelProperty(value = "角色描述")
    @NotBlank(message = "角色描述不能为空")
    private String roleDesc;
}
