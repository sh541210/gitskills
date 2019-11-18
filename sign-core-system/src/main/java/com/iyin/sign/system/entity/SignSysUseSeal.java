package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用印申请表
 *
 * @author yuanml
 * @date   2019/10/23
 */
@ApiModel("用印申请表")
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysUseSeal extends Model<SignSysUseSeal> {
    /**
     * 申请单据id
     */
    @ApiModelProperty(value = "申请单据id")
    private String applyId;

    /**
     * 申请人ID
     */
    @ApiModelProperty(value = "申请人ID")
    private String applyUser;

    /**
     * 文件code列表，多个逗号分隔
     */
    @ApiModelProperty(value = "文件code列表，多个逗号分隔")
    private String fileCode;

    /**
     * 印章id
     */
    @ApiModelProperty(value = "印章id")
    private String sealId;

    /**
     * 申请次数
     */
    @ApiModelProperty(value = "申请次数")
    private Integer applyCount;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private Date expireTime;

    /**
     * 来源1合同2文件3用印申请
     */
    @ApiModelProperty(value = "来源1合同2文件3用印申请")
    private Integer source;

    @ApiModelProperty(value = "业务ID,如合同id")
    private String businessId;

    /**
     * 文件份数
     */
    @ApiModelProperty(value = "文件份数")
    private Integer fileNumber;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 申请文件，限制为PDF，和imgList 参数二选一
     */
    @ApiModelProperty(value = "申请文件，限制为PDF，和imgList 参数二选一")
    private String applyPdf;

    /**
     * 申请文件拍照图片，和applyPdf 参数二选一
     */
    @ApiModelProperty(value = "申请文件拍照图片，和applyPdf 参数二选一")
    private String imgList;

    /**
     * 印章名称
     */
    @ApiModelProperty(value = "印章名称")
    private String sealName;

    /**
     * 申请事由
     */
    @ApiModelProperty(value = "申请事由")
    private String applyCause;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
    @TableField
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creatUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String modifyUser;

}