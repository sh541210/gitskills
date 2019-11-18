package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 签章应用列表 on 2018/12/25 0025.
 */
@Data
public class SysApplicationDto implements Serializable{
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
}
