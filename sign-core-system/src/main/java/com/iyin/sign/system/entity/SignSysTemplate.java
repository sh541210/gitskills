package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 模板表
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
@TableName("sign_sys_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysTemplate extends Model<SignSysTemplate>  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编号
     */
    @ApiModelProperty(value = "模板编号")
    private String id;

    /**
     * 模板名称
     */
    @TableField("temp_name")
    @ApiModelProperty(value = "模板名称")
    private String tempName;

    /**
     * 模板用途
     */
    @TableField("temp_purpose")
    @ApiModelProperty(value = "模板用途")
    private String tempPurpose;

    /**
     * 模板状态
     */
    @TableField("temp_status")
    @ApiModelProperty(value = "模板状态")
    private Integer tempStatus;

    /**
     * 模板内容
     */
    @TableField("temp_html")
    @ApiModelProperty(value = "模板内容")
    private String tempHtml;
    /**
     * 模板类型
     */
    @TableField("temp_type")
    @ApiModelProperty(value = "模板类型")
    private String tempType;

    /**
     * 外键关联ID
     */
    @ApiModelProperty(value = "外键关联ID")
    private String relationId;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    @TableField("is_deleted")
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField("gmt_modified")
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

    /**
     * 添加用户
     */
    @ApiModelProperty(value = "添加用户")
    @TableField("create_user")
    private String createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(value = "修改用户")
    @TableField("update_user")
    private String updateUser;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SignSysTemplate{" +
                "id='" + id + '\'' +
                ", tempName='" + tempName + '\'' +
                ", tempPurpose='" + tempPurpose + '\'' +
                ", tempStatus=" + tempStatus +
                ", tempHtml='" + tempHtml + '\'' +
                ", relationId='" + relationId + '\'' +
                ", isDeleted=" + isDeleted +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", createUser='" + createUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}
