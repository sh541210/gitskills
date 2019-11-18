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
 * @Description: 用户角色表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 17:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 17:35
 * @Version: 0.0.1
 */
@ApiModel(value = "用户角色表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysUserRole extends Model<SignSysUserRole> implements Serializable{

    private static final long serialVersionUID = -1492054236216827727L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

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
