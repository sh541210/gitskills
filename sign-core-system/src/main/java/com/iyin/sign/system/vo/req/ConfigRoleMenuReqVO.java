package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ConfigRoleMenuReqVO
 * @Description: 配置菜单角色请求vo
 * @Author: luwenxiong
 * @CreateDate: 2019/7/23 11:41
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/23 11:41
 * @Version: 0.0.1
 */
@Data
public class ConfigRoleMenuReqVO implements Serializable {
    private static final long serialVersionUID = 4810195913923101510L;

    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "分配的菜单ID")
    @NotEmpty(message = "菜单Id不能为空")
    private List<String> menuIds;
}
