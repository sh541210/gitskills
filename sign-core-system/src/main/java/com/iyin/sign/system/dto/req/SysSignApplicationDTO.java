package com.iyin.sign.system.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author wdf
 * @since 2019-07-05
 */
@ApiModel("应用信息")
@Data
public class SysSignApplicationDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String id;

    /**
     * 分配用户应用key
     */
    @ApiModelProperty("分配用户应用key")
    private String userAppId;

    /**
     * 分配用户应用秘钥
     */
    @ApiModelProperty("分配用户应用秘钥")
    private String userAppSceret;

    /**
     * 0 启用 ；1 禁用
     */
    @ApiModelProperty("0 启用 ；1 禁用")
    private Integer applicationAbled;

    /**
     * 0 未删除 ;1 删除
     */
    @ApiModelProperty("0 未删除 ;1 删除")
    private Integer applicationDelete;

    /**
     * 应用描述
     */
    @ApiModelProperty("应用描述")
    private String applicationDesc;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date modifyTime;

    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    private String applicationName;

    /**
     * 添加用户名称
     */
    @ApiModelProperty(value = "添加用户名称")
    private String createUserName;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String createEnName;

    @Override
    public String toString() {
        return "SysSignApplicationDTO{" +
                "id='" + id + '\'' +
                ", userAppId='" + userAppId + '\'' +
                ", userAppSceret='" + userAppSceret + '\'' +
                ", applicationAbled=" + applicationAbled +
                ", applicationDelete=" + applicationDelete +
                ", applicationDesc='" + applicationDesc + '\'' +
                ", userId='" + userId + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", applicationName='" + applicationName + '\'' +
                ", createUserName='" + createUserName + '\'' +
                ", createEnName='" + createEnName + '\'' +
                '}';
    }
}
