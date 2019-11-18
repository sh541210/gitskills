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
 * @ClassName: SignSysRoleMenu
 * @Description: 角色菜单权限
 * @Author: luwenxiong
 * @CreateDate: 2019/7/23 11:21
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/23 11:21
 * @Version: 0.0.1
 */
@ApiModel(value = "角色菜单权限")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysRoleMenu extends Model<SignSysRoleMenu> implements Serializable {

    private static final long serialVersionUID = -7113664877226409858L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "菜单ID")
    private String menuId;

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
