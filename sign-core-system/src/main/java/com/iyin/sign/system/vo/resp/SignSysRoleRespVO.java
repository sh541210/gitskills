package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysRoleRespVO
 * @Description: 角色信息
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 17:04
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 17:04
 * @Version: 0.0.1
 */
@Data
public class SignSysRoleRespVO implements Serializable {
    private static final long serialVersionUID = 4927043305538625067L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleFlag;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date gmtCreate;
}
