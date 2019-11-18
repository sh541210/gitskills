package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 节点权限表
 *
 * @author yuanml
 * @date   2019/07/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignSysMenu extends Model<SignSysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @TableId(value = "menu_id", type = IdType.INPUT)
    @ApiModelProperty("节点ID")
    private String menuId;

    /**
     * 父级节点ID
     */
    @NotNull(message = "父级节点ID不能为空")
    @ApiModelProperty("父级节点ID")
    private String parentId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty("标题")
    private String name;

    /**
     * 节点权限标识
     */
    @ApiModelProperty(value = "节点权限标识")
    private String permission;

    /**
     * 前端地址
     */
    @ApiModelProperty("前端地址")
    private String path;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 前端组件
     */
    @ApiModelProperty("前端组件")
    private String component;

    /**
     * 排序值
     */
    @ApiModelProperty("排序值")
    private Integer sort;

    /**
     * 路由缓冲 0-开启，1- 关闭
     */
    @ApiModelProperty("路由缓冲 0-开启，1- 关闭")
    private String keepAlive;

    /**
     * 节点类型 （0菜单 1按钮）
     */
    @NotNull(message = "节点类型不能为空")
    @ApiModelProperty("节点类型 （0菜单 1按钮）")
    private String type;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateUser;

    /**
     * 逻辑删除标记(0--正常 1--删除)
     */
//    @TableLogic
//    @ApiModelProperty("逻辑删除标记(0--正常 1--删除)")
//    private String delFlag;

}