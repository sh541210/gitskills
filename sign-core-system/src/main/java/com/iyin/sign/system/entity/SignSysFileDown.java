package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件下载记录表
 * </p>
 *
 * @author wdf
 * @since 2019-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value ="文件下载记录表")
@TableName("sign_sys_file_down")
public class SignSysFileDown extends Model<SignSysFileDown> {

    private static final long serialVersionUID = 1L;

    /**
     * 下载ID
     */
    @ApiModelProperty(value = "下载ID")
    private String id;

    /**
     * 下载用户ID
     */
    @ApiModelProperty(value = "下载用户ID")
    @TableField("down_user")
    private String downUser;

    /**
     * 用户所属单位/个人ID
     */
    @ApiModelProperty(value = "用户所属单位/个人ID")
    @TableField("user_enterprise")
    private String userEnterprise;

    /**
     * 1单位2个人
     */
    @ApiModelProperty(value = "1单位2个人")
    @TableField("user_type")
    private Integer userType;

    /**
     * 1后台用户2前台用户
     */
    @ApiModelProperty(value = "1后台用户2前台用户")
    @TableField("user_channel")
    private Integer userChannel;

    /**
     * 文件code
     */
    @ApiModelProperty(value = "文件code")
    @TableField("file_code")
    private String fileCode;

    /**
     * 下载时间
     */
    @ApiModelProperty(value = "下载时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SignSysFileDown{" +
        "id=" + id +
        ", downUser=" + downUser +
        ", userEnterprise=" + userEnterprise +
        ", userType=" + userType +
        ", userChannel=" + userChannel +
        ", fileCode=" + fileCode +
        ", gmtCreate=" + gmtCreate +
        "}";
    }
}
