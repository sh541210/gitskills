package com.iyin.sign.system.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 白鹤印章管家 申请记录
 * </p>
 *
 * @author wdf
 * @since 2019-10-09
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="白鹤印章管家 申请记录")
@TableName("sign_sys_use_seal")
public class SignSysUseSealDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请单据id
     */
    @ApiModelProperty(value = "申请单据id")
    @TableId("apply_id")
    private String applyId;

    /**
     * 文件code
     */
    @ApiModelProperty(value = "文件code列表，多个逗号分隔")
    @TableField("file_code")
    private String fileCode;

    /**
     * 印章设备地址
     */
    @ApiModelProperty(value = "印章设备地址")
    private String mac;

    /**
     * 印章id
     */
    @ApiModelProperty(value = "印章id")
    private String sealId;

    /**
     * 申请次数
     */
    @ApiModelProperty(value = "申请次数")
    @TableField("apply_count")
    private Integer applyCount;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 来源1合同2文件3用印申请
     */
    @ApiModelProperty(value = "来源1合同2文件3用印申请")
    private Integer source;

    /**
     * 文件标题
     */
    @ApiModelProperty(value = "文件标题")
    @TableField("file_title")
    private String fileTitle;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    /**
     * 盖章数据回传地址
     */
    @ApiModelProperty(value = "盖章数据回传地址")
    private String postbackaddress;

    /**
     * 印章名称
     */
    @ApiModelProperty(value = "印章名称")
    @TableField("seal_name")
    private String sealName;

    /**
     * 申请事由
     */
    @ApiModelProperty(value = "申请事由")
    @TableField("apply_cause")
    private String applyCause;

    /**
     * 是否删除（0：未删除；1：删除）
     */
    @ApiModelProperty(value = "是否删除（0：未删除；1：删除）")
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField("creat_user")
    private String creatUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    @TableField("modify_user")
    private String modifyUser;

    /**
     * 添加用户名称
     */
    @ApiModelProperty(value = "添加用户名称")
    private String createUserName;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String createEnName;

}
