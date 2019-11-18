package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author wdf
 * @since 2019-07-05
 */
@TableName("sign_sys_application")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SysSignApplication extends Model<SysSignApplication>   implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "应用ID")
    private String id;

    /**
     * 分配用户应用key
     */
    @TableField("user_app_id")
    @ApiModelProperty("分配用户应用key")
    private String userAppId;

    /**
     * 分配用户应用秘钥
     */
    @TableField("user_app_sceret")
    @ApiModelProperty("分配用户应用秘钥")
    private String userAppSceret;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 0 启用 ；1 禁用
     */
    @TableField("application_abled")
    @ApiModelProperty("0 启用 ；1 禁用")
    private Integer applicationAbled;

    /**
     * 0 未删除 ;1 删除
     */
    @TableField("application_delete")
    @ApiModelProperty("0 未删除 ;1 删除")
    private Integer applicationDelete;

    /**
     * 应用描述
     */
    @TableField("application_desc")
    @ApiModelProperty("应用描述")
    private String applicationDesc;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    @ApiModelProperty("修改时间")
    private Date modifyTime;

    /**
     * 扩展字段1
     */
    @ApiModelProperty("扩展字段1")
    private String filed1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty("扩展字段2")
    private String filed2;

    /**
     * 应用名称
     */
    @TableField("application_name")
    @ApiModelProperty("应用名称")
    private String applicationName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysSignApplication{" +
        "id=" + id +
        ", userAppId=" + userAppId +
        ", userAppSceret=" + userAppSceret +
        ", userId=" + userId +
        ", applicationAbled=" + applicationAbled +
        ", applicationDelete=" + applicationDelete +
        ", applicationDesc=" + applicationDesc +
        ", createTime=" + createTime +
        ", modifyTime=" + modifyTime +
        ", filed1=" + filed1 +
        ", filed2=" + filed2 +
        ", applicationName=" + applicationName +
        "}";
    }
}
