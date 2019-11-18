package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 模板控件表
 *
 * @author yuanml
 * @date   2019/09/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("模板控件表")
public class SignSysTemplateField extends Model<SignSysTemplateField> implements Serializable {
    /**
     * snowflake
     */
    @ApiModelProperty(value = "snowflake")
    private String id;

    /**
     * 模板Id（sign_sys_template表中Id）
     */
    @ApiModelProperty(value = "模板Id（sign_sys_template表中Id）")
    private String templateId;

    @ApiModelProperty(value = "名称")
    private String signName;

    /**
     * 位置类型(01:印章；02:签名；)
     */
    @ApiModelProperty(value = "位置类型(01:印章；02:签名；)")
    private String signType;

    /**
     * 签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)
     */
    @ApiModelProperty(value = "签章方式(01：单页签章；02：多页签章；03：骑缝签章；04:连页签章)")
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

    @ApiModelProperty(value = "签章方向0：表示左 1：表示右")
    private String signatureDirection;

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