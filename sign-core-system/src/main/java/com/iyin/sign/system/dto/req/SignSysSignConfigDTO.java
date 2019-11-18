package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Accessors(chain = true)
@ApiModel(value ="签章配置设置")
public class SignSysSignConfigDTO implements Serializable {

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
    private Integer preSign;

    /**
     * 是否启用验证码0否1是
     */
    @ApiModelProperty(value = "是否启用验证码0否1是")
    private Integer verificationCode;

    /**
     * 是否启用二维码0否1是
     */
    @ApiModelProperty(value = "是否启用二维码0否1是")
    private Integer qrCode;

    /**
     * 验证失效为0永久有效
     */
    @ApiModelProperty(value = "验证失效为0永久有效")
    private Integer gmtVerification;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

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
