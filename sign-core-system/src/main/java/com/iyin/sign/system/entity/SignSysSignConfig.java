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
 * 签章配置设置表
 * </p>
 *
 * @author wdf
 * @since 2019-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value ="签章配置设置表")
@TableName("sign_sys_sign_config")
public class SignSysSignConfig extends Model<SignSysSignConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 是否启用预盖章0否1是
     */
    @ApiModelProperty(value = "是否启用预盖章0否1是")
    @TableField("pre_sign")
    private Integer preSign;

    /**
     * 是否启用验证码0否1是
     */
    @ApiModelProperty(value = "是否启用验证码0否1是")
    @TableField("verification_code")
    private Integer verificationCode;

    /**
     * 是否启用二维码0否1是
     */
    @ApiModelProperty(value = "是否启用二维码0否1是")
    @TableField("qr_code")
    private Integer qrCode;

    /**
     * 验证失效为0永久有效
     */
    @ApiModelProperty(value = "验证失效为0永久有效")
    @TableField("gmt_verification")
    private Integer gmtVerification;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("gmt_modified")
    private Date gmtModified;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SignSysSignConfig{" +
        "id=" + id +
        ", preSign=" + preSign +
        ", verificationCode=" + verificationCode +
        ", qrCode=" + qrCode +
        ", gmtVerification=" + gmtVerification +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
