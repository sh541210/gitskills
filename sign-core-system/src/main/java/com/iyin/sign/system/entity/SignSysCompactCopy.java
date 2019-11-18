package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 合同抄送人表
 *
 * @author yuanml
 * @date   2019/08/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactCopy extends Model<SignSysCompactCopy> {
    /**
     * snowflake
     */
    @ApiModelProperty(value = "snowflake")
    @TableId
    private String id;

    /**
     * 合同Id（sign_sys_compact_info表中Id）
     */
    @ApiModelProperty(value = "合同Id（sign_sys_compact_info表中Id）")
    private String compactId;

    /**
     * 抄送人用户Id（sign_sys_user_info表中id）
     */
    @ApiModelProperty(value = "抄送人用户Id（sign_sys_user_info表中id）")
    private String userId;

    /**
     * 签署人的联系方式(手机/邮箱)
     */
    @ApiModelProperty(value = "签署人的联系方式(手机/邮箱)")
    private String signContact;

    /**
     * 签署人名称
     */
    @ApiModelProperty(value = "签署人名称")
    private String signName;

    /**
     * 是否删除（0：未删除；1：删除；）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除；）")
    @TableLogic
    private Byte isDeleted;

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