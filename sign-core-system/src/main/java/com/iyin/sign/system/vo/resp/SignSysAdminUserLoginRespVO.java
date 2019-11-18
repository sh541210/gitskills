package com.iyin.sign.system.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysAdminUserLoginRespVO
 * @Description: 管理员用户登录返回
 * @Author: luwenxiong
 * @CreateDate: 2019/6/24 16:26
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/24 16:26
 * @Version: 0.0.1
 */
@Data
public class SignSysAdminUserLoginRespVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty(value = "是否删除：0：否；1：是；")
    private Integer isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "创建者")
    private String createUser;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新者")
    private String updateUser;

    @ApiModelProperty(value = "账号角色")
    private String role;

    @ApiModelProperty(value = "管理员所属单位ID")
    private String enterpriseId;

    @ApiModelProperty(value = "管理员所属单位名称")
    private String enterpriseName;

    @ApiModelProperty(value = "0:超级管理员 01：单位管理员")
    private String powerLevel;

    @ApiModelProperty(value = "管理员昵称 如:张三")
    private String userNickName;

    private String sessionToken;

    @ApiModelProperty(value = "系统配置信息")
    private SignSysDefaultConfigInfoResp configInfoResp;

    @ApiModelProperty(value = "Licence信息")
    private UserLicenceInfo userLicenceInfo;
}
