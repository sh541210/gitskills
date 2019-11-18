package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

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
@ApiModel(value ="签章配置修改")
public class SignSysSignConfigReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @NotBlank(message = "ID不能为空")
    private String id;

    /**
     * 是否启用预盖章0否1是
     */
    @ApiModelProperty(value = "是否启用预盖章0否1是")
    @Range(min=0,max = 1)
    private Integer preSign;

    /**
     * 是否启用验证码0否1是
     */
    @ApiModelProperty(value = "是否启用验证码0否1是")
    @Range(min=0,max = 1)
    private Integer verificationCode;

    /**
     * 是否启用二维码0否1是
     */
    @ApiModelProperty(value = "是否启用二维码0否1是")
    @Range(min=0,max = 1)
    private Integer qrCode;

    /**
     * 验证失效为0永久有效
     */
    @ApiModelProperty(value = "验证失效为0永久有效")
    @Range(min=0)
    private Integer gmtVerification;

    @Override
    public String toString() {
        return "SignSysSignConfigReqVO{" +
                "id='" + id + '\'' +
                ", preSign=" + preSign +
                ", verificationCode=" + verificationCode +
                ", qrCode=" + qrCode +
                ", gmtVerification=" + gmtVerification +
                '}';
    }
}
