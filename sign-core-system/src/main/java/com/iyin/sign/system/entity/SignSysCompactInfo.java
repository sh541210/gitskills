package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 合同信息表
 *
 * @author yuanml
 * @date   2019/08/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactInfo extends Model<SignSysCompactInfo> {
    /**
     * snowflake
     */
    @ApiModelProperty(value = "snowflake")
    @TableId
    private String id;

    /**
     * 用户Id（sign_sys_user_info表中Id）
     */
    @ApiModelProperty(value = "用户Id（sign_sys_user_info表中Id）")
    private String userId;

    /**
     * 合同模板Id（sign_sys_compact_template表中Id）
     */
    @ApiModelProperty(value = "合同模板Id（sign_sys_compact_template表中Id）")
    private String templateId;

    /**
     * 合同主题
     */
    @ApiModelProperty(value = "合同主题")
    private String compactTheme;

    /**
     * 合同有效期开始时间
     */
    @ApiModelProperty(value = "合同有效期开始时间")
    private Date validityStartDate;

    /**
     * 合同有效期结束时间
     */
    @ApiModelProperty(value = "合同有效期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date validityEndDate;

    /**
     * 签署截止日期
     */
    @ApiModelProperty(value = "签署截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date signDeadline;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 合同状态（01：草稿；02：已撤销；03：已拒签；04：签署中；05：签署完成；）
     */
    @ApiModelProperty(value = "合同状态（01：草稿；02：已撤销；03：已拒签；04：签署中；05：签署完成；）")
    private String compactStatus;

    /**
     * 合同发起时间
     */
    @ApiModelProperty(value = "合同发起时间")
    private Date compactStartDate;

    /**
     * 合同完成时间
     */
    @ApiModelProperty(value = "合同完成时间")
    private Date compactEndDate;

    /**
     * 合同拒签时间
     */
    @ApiModelProperty(value = "合同拒签时间")
    private Date compactRefuseDate;

    /**
     * 合同撤销时间
     */
    @ApiModelProperty(value = "合同撤销时间")
    private Date compactRevocationDate;

    /**
     * 签署方式（01：无序签；02：顺序签署；03：每人单独签署；）
     */
    @ApiModelProperty(value = "签署方式（01：无序签；02：顺序签署；03：每人单独签署；）")
    private String signWay;

    /**
     * 签署人方式（01：我需要签署；02：需要其他人签署）
     */
    @ApiModelProperty(value = "签署人方式（01：我需要签署；02：需要其他人签署）")
    private String signatoryWay;

    /**
     * 是否指定签署位置（0：未指定签署位置；1：指定签署位置；）
     */
    @ApiModelProperty(value = "是否指定签署位置（0：未指定签署位置；1：指定签署位置；）")
    private Byte isSiteSign;

    /**
     * 合同包路径
     */
    @ApiModelProperty(value = "合同包路径")
    private String packagePath;

    /**
     * 撤销原因
     */
    @ApiModelProperty(value = "撤销原因")
    private String revocationRemake;

    /**
     * 拒签原因
     */
    @ApiModelProperty(value = "拒签原因")
    private String refuseSignRemake;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
    @TableLogic
    private Byte isDeleted;

    /**
     * 所属组织Id
     */
    @ApiModelProperty(value = "所属组织Id")
    private String orgId;

    /**
     * 归档文件夹Id
     */
    @ApiModelProperty(value = "归档文件夹Id")
    private String folderId;

    @ApiModelProperty(value = "分配的打印次数")
    private String printNum;

    @ApiModelProperty(value = "类型（01:文件管理，02:合同）")
    private String type;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    @ApiModelProperty(value = "验证码有效期")
    private String verificationDate;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
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
    @TableField(fill = FieldFill.UPDATE)
    private Date gmtModified;

    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String modifyUser;

}