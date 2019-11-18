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
 * 合同关联文件资源表
 *
 * @author yuanml
 * @date   2019/08/07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactFile extends Model<SignSysCompactFile> {
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
     * 资源文件编码（sign_sys_file_resource表中file_code）
     */
    @ApiModelProperty(value = "资源文件编码（sign_sys_file_resource表中file_code）")
    private String fileCode;

    /**
     * 资源文件编码（sign_sys_file_resource表中file_code）拒签恢复用
     */
    @ApiModelProperty(value = "资源文件编码（sign_sys_file_resource表中file_code）拒签恢复用",hidden = true)
    private String fileCodeOrigin;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 合同文档类型（01：合同文件；02：合同附件；）
     */
    @ApiModelProperty(value = "合同文档类型（01：合同文件；02：合同附件；）")
    private String fileType;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
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

    /**
     * 页总数
     */
    @ApiModelProperty(value = "页总数")
    private Integer pageTotal;

}