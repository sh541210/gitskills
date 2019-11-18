package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同签名域表
 *
 * @author yuanml
 * @date   2019/08/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactField extends Model<SignSysCompactField> {
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
     * 合同文件Id（file_resource表中file_code）
     */
    @ApiModelProperty(value = "合同文件Id（file_resource表中file_code）")
    private String compactFileCode;

    /**
     * 签署人Id（sign_sys_compact_signatory表中Id）
     */
    @ApiModelProperty(value = "签署人Id（sign_sys_compact_signatory表中Id）")
    private String signatoryId;

    /**
     * 签署人名称
     */
    @ApiModelProperty(value = "签署人名称")
    private String signName;

    @ApiModelProperty(value = "签署位置类型")
    private String signType;

    /**
     * 签章方式(01：单页签章；02：多页签章；03：骑缝签章；)
     */
    @ApiModelProperty(value = "签章方式(01：单页签章；02：多页签章；03：骑缝签章；)")
    private String signatureMethod;

    /**
     * 签章页或签章初始页
     */
    @ApiModelProperty(value = "签章页或签章初始页")
    private Integer signatureStartPage;

    /**
     * 签章结束页
     */
    @ApiModelProperty(value = "签章结束页")
    private Integer signatureEndPage;

    /**
     * 骑缝签时每枚章的覆盖页数
     */
    @ApiModelProperty(value = "骑缝签时每枚章的覆盖页数")
    private Integer coverPageNum;

    /**
     * 签章坐标X轴
     */
    @ApiModelProperty(value = "签章坐标X轴")
    private BigDecimal signatureCoordinateX;

    /**
     * 签章坐标Y轴
     */
    @ApiModelProperty(value = "签章坐标Y轴")
    private BigDecimal signatureCoordinateY;

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