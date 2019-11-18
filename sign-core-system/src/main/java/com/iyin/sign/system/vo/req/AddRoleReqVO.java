package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: AddRoleReqVO
 * @Description: 添加角色请求VO
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 17:21
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 17:21
 * @Version: 0.0.1
 */
@Data
public class AddRoleReqVO implements Serializable {

    private static final long serialVersionUID = 2278455726936072759L;

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
