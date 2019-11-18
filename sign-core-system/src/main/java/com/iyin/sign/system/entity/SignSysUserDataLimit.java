package com.iyin.sign.system.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SignSysUserDataLimit
 * @Description: 用户数据权限范围
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 15:36
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 15:36
 * @Version: 0.0.1
 */
@ApiModel(value = "用户数据权限范围")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysUserDataLimit extends Model<SignSysUserDataLimit> implements Serializable {

    private static final long serialVersionUID = -3391055643679317015L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "0:全部 1:本级 2:本级及子级 3:自定义")
    private String limitType;

    @ApiModelProperty(value = "自定义的权限范围 格式如: 232323,322222,555555")
    private String nodeIds;

    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
