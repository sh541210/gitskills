package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Accessors(chain = true)
@ApiModel(value ="文件打印记录表")
public class SignSysFilePrintDTO  implements Serializable  {

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
    private String printUser;

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
     * 打印时间
     */
    @ApiModelProperty(value = "打印时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "企业/个人名称")
    private String enterpriseName;

    @Override
    public String toString() {
        return "SignSysFilePrintDTO{" +
                "id='" + id + '\'' +
                ", printUser='" + printUser + '\'' +
                ", userEnterprise='" + userEnterprise + '\'' +
                ", userType=" + userType +
                ", userChannel=" + userChannel +
                ", fileCode='" + fileCode + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", userName='" + userName + '\'' +
                ", enterpriseName='" + enterpriseName + '\'' +
                '}';
    }
}
