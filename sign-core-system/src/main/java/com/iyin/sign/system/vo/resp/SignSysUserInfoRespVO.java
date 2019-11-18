package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SignSysUserInfoRespVO
 * @Description: SignSysUserInfoRespVO
 * @Author: luwenxiong
 * @CreateDate: 2019/8/27 15:51
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/27 15:51
 * @Version: 0.0.1
 */
@Data
public class SignSysUserInfoRespVO implements Serializable {
    private static final long serialVersionUID = -7634232424019427339L;

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "账号登录类型 01：手机号 02：邮箱")
    private String loginType;


    @ApiModelProperty(value = "用户名（手机号或邮箱）")
    private String userName;

    @ApiModelProperty(value = "用户别名(如：张三 李四)")
    private String userNickName;

    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 白鹤第三方用户ID
     */
    @ApiModelProperty(value = "白鹤第三方用户ID")
    private String otherId;

    @ApiModelProperty(value = "用户角色信息")
    private UserRoleRespVO userRoleRespVO;

}
