package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysUserInfo
 * @Description: 角色表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 17:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 17:35
 * @Version: 0.0.1
 */
@ApiModel(value = "角色基础表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysRole extends Model<SignSysRole> implements Serializable{

    private static final long serialVersionUID = -1492054236216827727L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String roleFlag;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
