package com.iyin.sign.system.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Accessors(chain = true)
@ApiModel(value ="打印分配表")
public class SignSysPrintAuthUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 文件编码
     */
    @ApiModelProperty(value = "文件编码")
    private String fileCode;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 是否雾化 0否 1是
     */
    @ApiModelProperty(value = "是否雾化 0否 1是")
    private Integer isFoggy;

    /**
     * 是否脱密 0否 1是
     */
    @ApiModelProperty(value = "是否脱密 0否 1是")
    private Integer isGrey;

    /**
     * 打印次数
     */
    @ApiModelProperty(value = "打印次数")
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
    public String toString() {
        return "SignSysPrintAuthUser{" +
                "id=" + id +
                ", fileCode=" + fileCode +
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
