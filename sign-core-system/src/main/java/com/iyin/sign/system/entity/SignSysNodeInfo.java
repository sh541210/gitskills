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
 * @ClassName: SignSysNodeInfo
 * @Description: 签章系统组织结构表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 18:29
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 18:29
 * @Version: 0.0.1
 */
@ApiModel(value = "签章系统组织结构表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysNodeInfo extends Model<SignSysNodeInfo> implements Serializable {

    private static final long serialVersionUID = 8121354889946392428L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "父节点ID")
    private String parentNodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
