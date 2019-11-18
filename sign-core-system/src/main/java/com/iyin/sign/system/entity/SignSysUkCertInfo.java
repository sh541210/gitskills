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
 * @ClassName: SignSysUkCertInfo
 * @Description: uk印章中证书信息
 * @Author: luwenxiong
 * @CreateDate: 2019/7/24 17:38
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/24 17:38
 * @Version: 0.0.1
 */
@ApiModel(value = "uk印章中证书信息")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class SignSysUkCertInfo extends Model<SignSysUkCertInfo> implements Serializable {
    private static final long serialVersionUID = -8518213387470670839L;

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "证书标识")
    private String oid;

    @ApiModelProperty(value = "信任服务号")
    private String trustId;

    @ApiModelProperty(value = "单位ID")
    private String enterpriseId;

    @ApiModelProperty(value = "数字证书厂商")
    private String issuer;

    @ApiModelProperty(value = "数字证书有效期开始时间")
    private Date validStart;

    @ApiModelProperty(value = "数字证书有效期结束时间")
    private Date validEnd;

    @ApiModelProperty(value = "数字证书当前状态")
    private String currentStatus;

    private String remark;

    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;



}
