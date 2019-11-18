package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UpdateUserReqVO
 * @Description: 编辑用户
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 16:45
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 16:45
 * @Version: 0.0.1
 */
@Data
public class UpdateUserReqVO implements Serializable {


    private static final long serialVersionUID = 4638287887960253638L;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;


    @ApiModelProperty(value = "成员名称")
    @NotBlank(message = "成员名称不能为空")
    private String userNickName;

    @ApiModelProperty(value = "节点ID")
    @NotBlank(message = "节点ID不能为空")
    private String nodeId;

    @ApiModelProperty(value = "登录账号  手机号或邮箱")
    @NotBlank(message = "登录账号不能为空")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户角色ID")
    @NotBlank(message = "角色Id不能为空")
    private String roleId;

    @ApiModelProperty(value = "数据权限 0：全部  1 本级  2：本级及子集")
    @NotBlank(message = "数据权限未设置")
    private String dataLimitType;

}
