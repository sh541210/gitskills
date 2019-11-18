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
 * 文件打印记录表
 * </p>
 *
 * @author wdf
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value ="文件打印记录表")
@TableName("sign_sys_file_print")
public class SignSysFilePrint extends Model<SignSysFilePrint> {

    private static final long serialVersionUID = 1L;

    /**
     * 打印ID
     */
    @ApiModelProperty(value = "打印ID")
    private String id;

    /**
     * 打印用户ID
     */
    @ApiModelProperty(value = "打印用户ID")
    @TableField("print_user")
    private String printUser;

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
     * 打印时间
     */
    @ApiModelProperty(value = "打印时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SignSysFilePrint{" +
                "id=" + id +
                ", printUser=" + printUser +
                ", userEnterprise=" + userEnterprise +
                ", userType=" + userType +
                ", userChannel=" + userChannel +
                ", fileCode=" + fileCode +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
