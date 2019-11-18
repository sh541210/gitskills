package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Accessors(chain = true)
@ApiModel(value ="文件下载记录")
public class SignSysFileDownDto implements Serializable {

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
    private String downUser;

    /**
     * 用户所属单位/个人ID
     */
    @ApiModelProperty(value = "用户所属单位/个人ID")
    private String userEnterprise;

    /**
     * 1单位2个人
     */
    @ApiModelProperty(value = "1单位2个人")
    private Integer userType;

    /**
     * 1后台用户2前台用户
     */
    @ApiModelProperty(value = "1后台用户2前台用户")
    private Integer userChannel;

    /**
     * 文件code
     */
    @ApiModelProperty(value = "文件code")
    private String fileCode;

    /**
     * 下载时间
     */
    @ApiModelProperty(value = "下载时间")
    private Date gmtCreate;

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
