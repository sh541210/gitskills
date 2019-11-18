package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: AdminSystem
 * @Description: 签章系统后台管理员
 * @Author: luwenxiong
 * @CreateDate: 2019/1/17
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2019/1/17
 * @Version: 1.0.0
 */
@ApiModel(value = "签章系统后台管理员")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SignSysAdminUser extends Model<SignSysAdminUser> implements Serializable {

    private static final long serialVersionUID = -576639322360291384L;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
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

    @ApiModelProperty(value = "管理员所属单位ID")
    private String enterpriseId;

    @ApiModelProperty(value = "0：超级管理员 01：单位管理员")
    private String powerLevel;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
