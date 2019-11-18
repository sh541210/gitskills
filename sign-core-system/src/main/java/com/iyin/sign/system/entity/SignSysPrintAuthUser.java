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
 * 打印分配表
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value ="打印分配表")
@TableName("sign_sys_print_auth_user")
public class SignSysPrintAuthUser extends Model<SignSysPrintAuthUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 文件编码
     */
    @ApiModelProperty(value = "文件编码")
    @TableField("file_code")
    private String fileCode;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private String userId;

    /**
     * 是否雾化 0否 1是
     */
    @ApiModelProperty(value = "是否雾化 0否 1是")
    @TableField("is_foggy")
    private Integer isFoggy;

    /**
     * 是否脱密 0否 1是
     */
    @ApiModelProperty(value = "是否脱密 0否 1是")
    @TableField("is_grey")
    private Integer isGrey;

    /**
     * 打印次数
     */
    @ApiModelProperty(value = "打印次数")
    @TableField("print_num")
    private Long printNum;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 添加用户
     */
    @ApiModelProperty(value = "添加用户")
    @TableField("create_user")
    private String createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("gmt_modified")
    private Date gmtModified;

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
        return "SignSysPrintAuthUser{" +
                "id=" + id +
                ", fileCode=" + fileCode +
                ", userId=" + userId +
                ", isFoggy=" + isFoggy +
                ", isGrey=" + isGrey +
                ", printNum=" + printNum +
                ", gmtCreate=" + gmtCreate +
                ", createUser=" + createUser +
                ", gmtModified=" + gmtModified +
                ", updateUser=" + updateUser +
                "}";
    }
}
