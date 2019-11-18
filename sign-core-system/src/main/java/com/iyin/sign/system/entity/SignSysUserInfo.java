package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 签章系统用户
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 17:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 17:35
 * @Version: 0.0.1
 */
@ApiModel(value = "签章系统用户")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysUserInfo extends Model<SignSysUserInfo> implements Serializable{

    private static final long serialVersionUID = -1492054236216827727L;

    @ApiModelProperty(value = "主键")
    private String id;

   @ApiModelProperty(value = "账号登录类型 01：手机号 02：邮箱")
    private String loginType;


    @ApiModelProperty(value = "用户名（手机号或邮箱）")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户别名(如：张三 李四)")
    private String userNickName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码错误次数")
    private Integer invalidAuthTimes;

    @ApiModelProperty(value = "是否为单位(个人)下第一个创建的账号  01 是  02 否")
    @Deprecated
    private String firstCreate;

    @ApiModelProperty(value = "用户类型 01：企业用户；02：个人用户")
    private String userType;


    @ApiModelProperty(value = "企业或个人表主键ID")
    private String enterpriseOrPersonalId;


    @ApiModelProperty(value = "节点ID")
    private String nodeId;

    /**
     * Ukey账号是否被锁定（0：未锁定；1：锁定）
     */
    private Integer isLocked;


    @ApiModelProperty(value = "是否被禁用（0：未禁用；1：禁用）")
    private Integer isForbid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 来源(0-注册创建  1-管理员创建)
     */
    private String source;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 白鹤第三方用户ID
     */
    @ApiModelProperty(value = "白鹤第三方用户ID")
    private String otherId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
