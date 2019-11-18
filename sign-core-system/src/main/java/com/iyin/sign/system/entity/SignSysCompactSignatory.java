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
 * 合同签署人表
 *
 * @author yuanml
 * @date   2019/08/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactSignatory extends Model<SignSysCompactSignatory> {
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
     * 签署人Id（sign_sys_user_info表中Id）
     */
    @ApiModelProperty(value = "签署人Id（sign_sys_user_info表中Id）")
    private String signatoryId;

    /**
     * 下一个签署人Id（sign_sys_user_info表中Id）
     */
    @ApiModelProperty(value = "下一个签署人Id（sign_sys_user_info表中Id）")
    private String nextSignatoryId;

    /**
     * 签署状态（01：待我签署；02：待他人签；03：签署通过；04签署不通过）
     */
    @ApiModelProperty(value = "签署状态（01：待我签署；02：待他人签；03：签署通过；04签署不通过）")
    private String signStatus;

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
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer serialNumber;

    /**
     * 签署人是否进行UKey签章（0：否；1：是）
     */
    @ApiModelProperty(value = "签署人是否进行UKey签章（0：否；1：是）")
    private Byte isKeySignature;

    /**
     * 是否删除（0：未删除；1：删除；）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除；）")
    @TableLogic
    private Byte isDeleted;

    /**
     * 是否已转签（0：未转签，1：已转签)
     */
    @ApiModelProperty(value = "是否已转签（0：未转签，1：已转签)")
    private Byte turnSignFlag;

    /**
     * 转签到谁（当前表的id）
     */
    @ApiModelProperty(value = "转签到谁（当前表的id）")
    private String turnToId;

    /**
     * 转签时间
     */
    @ApiModelProperty(value = "转签时间")
    private Date turnDate;

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

    /**
     * 签署时间
     */
    @ApiModelProperty(value = "签署时间")
    private Date signDate;

}