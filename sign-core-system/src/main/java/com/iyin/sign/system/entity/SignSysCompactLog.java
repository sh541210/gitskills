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
 * @ClassName: SignSysCompactLog.java
 * @Description: 合同文件操作记录
 * @Author: yml
 * @CreateDate: 2019/8/16
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/16
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysCompactLog extends Model<SignSysCompactLog> {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    /**
     * 操作用户ID（sign_sys_user_info表主键）
     */
    @ApiModelProperty(value = "操作用户ID（sign_sys_user_info表主键）")
    private String userId;

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
     * 操作类型 （0查看 1签署 2打印 3下载 4拒签）
     */
    @ApiModelProperty(value = "操作类型 （0查看 1签署 2打印 3下载 4拒签）")
    private String type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateUser;

    /**
     * 逻辑删除标记(0--正常 1--删除)
     */
    @ApiModelProperty(value = "逻辑删除标记(0--正常 1--删除)")
    @TableLogic
    private String delFlag;

}